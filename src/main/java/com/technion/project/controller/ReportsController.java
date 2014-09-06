package com.technion.project.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.technion.project.dao.EvacuationDAO;
import com.technion.project.dao.ReportDAO;
import com.technion.project.dao.UserDao;
import com.technion.project.model.Report;
import com.thoughtworks.xstream.XStream;

@Controller
@RequestMapping("/reports/*")
public class ReportsController
{

	@Autowired
	private ReportDAO reportDao;
	@Autowired
	private UserDao userDAO;

	@Autowired
	private EvacuationDAO evacuationDAO;

	@RequestMapping(value =
	{ "" }, method = RequestMethod.GET)
	public ModelAndView defualtView()
	{
		return getAllReportsForUser("");
	}

	@RequestMapping(value = "", consumes = "application/json", produces = "application/json")
	public @ResponseBody List<Report> getReportsJson()
	{
		return reportDao.getAllReports();
	}

	@RequestMapping(value = "", consumes = "application/xml", produces = "application/xml")
	public @ResponseBody List<Report> getReportsXml()
	{
		return reportDao.getAllReports();
	}

	@RequestMapping(value = "exportXml", method = RequestMethod.GET)
	public @ResponseBody String getReportsXML(final HttpServletResponse response)
	{
		try
		{
			response.setHeader("Content-Disposition",
					"inline;filename=\"reports.xml\"");
			response.setContentType("application/xml");
			final XStream xStream = new XStream();
			xStream.processAnnotations(Report.class);
			final String xml = xStream.toXML(reportDao.getAllReports());
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

	@RequestMapping(value = "exportKml", method = RequestMethod.GET)
	public @ResponseBody String getReportsKML(
			final HttpServletResponse response, final HttpServletRequest request)
	{
		final XStream xStream = new XStream();
		xStream.processAnnotations(Report.class);
		final String xml = xStream.toXML(reportDao.getAllReports());
		response.setHeader("Content-Disposition",
				"inline;filename=\"reports.kml\"");
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
					.getServletContext().getRealPath("/CSS/ReportXsl.xsl"));

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

	@RequestMapping(value = "{id}", consumes = "application/json", produces = "application/json")
	public @ResponseBody Report getReportJson(@PathVariable final Long id)
	{
		return reportDao.getReportByID(id);
	}

	private ModelAndView getAllReportsForUser(final String username)
	{
		final ModelAndView model = new ModelAndView();
		List<Report> allReports = null;
		if (username.isEmpty())
			allReports = reportDao.getAllReports();
		else
			allReports = reportDao.getReportsForUser(userDAO
					.findByUserNameLocalThread(username));
		float middleLat = 0, middleLng = 0;
		for (final Report report : allReports)
		{
			middleLat += report.getGeolat();
			middleLng += report.getGeolng();
		}
		model.addObject("midLat", middleLat / allReports.size());
		model.addObject("midLng", middleLng / allReports.size());
		model.addObject("reports", allReports);
		model.addObject("events", evacuationDAO.getAll());
		model.addObject("reportScript", "allReports");
		model.setViewName("allReport");
		final Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		if (!"anonymousUser".equals(auth.getName()))
			model.addObject("user",
					userDAO.findByUserNameLocalThread(auth.getName()));
		return model;
	}

	@RequestMapping(value =
	{ "" }, method = RequestMethod.POST)
	public String addReportToDB(final Report report,
			@RequestParam("file") final MultipartFile file)

	{
		reportDao.addReport(report, file);
		return "redirect:";
	}

	@RequestMapping(value = "search", method = RequestMethod.GET)
	public @ResponseBody List<Report> getReportsInJSON(
			@RequestParam final String q)
	{
		return reportDao.searchReports(q);
	}

	@RequestMapping(value = "{id}", method = RequestMethod.DELETE)
	public @ResponseBody String delete(@PathVariable final long id)
	{
		reportDao.removeReport(id);
		return String.valueOf(id);
	}

}
