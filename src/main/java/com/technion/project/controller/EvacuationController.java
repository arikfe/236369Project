package com.technion.project.controller;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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
}
