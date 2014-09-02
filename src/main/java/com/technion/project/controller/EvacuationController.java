package com.technion.project.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.technion.project.dao.EvacuationDAO;
import com.technion.project.model.EvacuationEvent;
import com.technion.project.model.User;
import com.thoughtworks.xstream.XStream;

@Controller
@RequestMapping("/evacuation/*")
public class EvacuationController extends BaseController
{
	@Autowired
	private EvacuationDAO evacuationDAO;

	@RequestMapping(value =
	{ "" }, method = RequestMethod.POST)
	public String add(final EvacuationEvent event)
	{
		final HashSet<User> hashSet = new HashSet<User>();
		event.setRegisteredUsers(hashSet);
		event.setEstimated(new Date());
		evacuationDAO.addEvecuationEvent(event);
		return "redirect:../";
	}

	@RequestMapping(value = "id/{id}/join", method = RequestMethod.PUT)
	public @ResponseBody boolean join(@PathVariable final long id)
	{
		return evacuationDAO.addUserToEvent(getCurrentUser(), id);
	}

	@RequestMapping(value = "id/{id}/joinUser", method = RequestMethod.PUT)
	public @ResponseBody boolean joinUser(@PathVariable final long id,
			@RequestBody final MultiValueMap<String, String> body)
	{
		return evacuationDAO.addUserToEvent(
				userDao.findByUserNameLocalThread(body.getFirst("username")),
				id);
	}

	@RequestMapping(value = "", consumes = "application/xml", produces = "application/xml")
	public @ResponseBody List<EvacuationEvent> getEvacuationsXML()
	{
		return evacuationDAO.getAll();
	}

	@RequestMapping(value = "", consumes = "application/json", produces = "application/json")
	public @ResponseBody List<EvacuationEvent> getEvacuationsJson()
	{
		return evacuationDAO.getAll();
	}

	@RequestMapping(value =
	{ "id/{id}/leave" }, method = RequestMethod.PUT)
	public @ResponseBody boolean leave(@PathVariable final long id)
	{
		return evacuationDAO.removeUserToEvent(getCurrentUser(), id);
	}

	@RequestMapping(value =
	{ "id/{id}/users" }, method = RequestMethod.GET)
	public @ResponseBody Set<User> getUsers(@PathVariable final long id)
	{
		return evacuationDAO.getByID(id).getRegisteredUsers();
	}

	@RequestMapping(value =
	{ "id/{id}/leaveUser" }, method = RequestMethod.PUT)
	public @ResponseBody boolean leaveUser(@PathVariable final long id,
			@RequestBody final MultiValueMap<String, String> body)
	{
		return evacuationDAO.removeUserToEvent(
				userDao.findByUserNameLocalThread(body.getFirst("username")),
				id);
	}

	@RequestMapping(value =
	{ "id/{id}" }, method = RequestMethod.GET, consumes = "application/json", produces = "application/json")
	public @ResponseBody EvacuationEvent list(@PathVariable final long id)
	{
		return evacuationDAO.getByID(id);
	}

	@RequestMapping(value = "closestEvent", method = RequestMethod.GET)
	public @ResponseBody EvacuationEvent getClosestEvent(
			@RequestParam final float lat, @RequestParam final float lng)
	{
		return evacuationDAO.getClosest(lat, lng);
	}

	@RequestMapping(value = "id/{id}", method = RequestMethod.GET)
	public ModelAndView getById(@PathVariable final long id)
	{
		final ModelAndView view = new ModelAndView();
		view.addObject("evacuation", evacuationDAO.getByID(id));
		view.setViewName("evacuation");
		view.addObject("id", id);
		view.addObject("users", userDao.getUserWithNoEvent());
		return view;
	}

	@RequestMapping(value = "id/{id}", method = RequestMethod.DELETE)
	public @ResponseBody boolean deleteEvent(@PathVariable final long id)
	{
		return evacuationDAO.delete(id);
	}

	@RequestMapping(value = "exportEvacuationXml", method = RequestMethod.GET)
	public @ResponseBody String getEvacuationXML(
			final HttpServletResponse response)
	{
		try
		{
			response.setHeader("Content-Disposition",
					"inline;filename=\"evacuation.xml\"");
			response.setContentType("application/xml");
			final XStream xStream = new XStream();
			xStream.processAnnotations(EvacuationEvent.class);
			final String xml = xStream.toXML(evacuationDAO.getAll());
			final OutputStream out = response.getOutputStream();
			out.write(xml.getBytes());
			out.flush();
			out.close();
		} catch (final IOException e)
		{
			e.printStackTrace();
		}
		return "";
	}

	@RequestMapping(value = "exportEvacuationKml", method = RequestMethod.GET)
	public @ResponseBody String getReportsKML(
			final HttpServletResponse response, final HttpServletRequest request)
	{
		final XStream xStream = new XStream();
		xStream.processAnnotations(EvacuationEvent.class);
		final String xml = xStream.toXML(evacuationDAO.getAll());
		response.setHeader("Content-Disposition",
				"inline;filename=\"evacuation.kml\"");
		response.setContentType("application/xml");
		Result output = null;
		try
		{
			output = new StreamResult(response.getOutputStream());
		} catch (final IOException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try
		{
			final Source input = new StreamSource(new ByteArrayInputStream(
					xml.getBytes()));
			final Source xsl = new StreamSource(request.getSession()
					.getServletContext().getRealPath("/CSS/evacuation.xsl"));
			final TransformerFactory factory = TransformerFactory.newInstance();
			final Transformer transformer = factory.newTransformer(xsl);
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.transform(input, output);
		} catch (final TransformerException te)
		{
			System.out.println("Transformer exception: " + te.getMessage());
		}

		return "";
	}
}
