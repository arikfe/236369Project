package com.technion.project.controller;

import java.util.Date;
import java.util.List;

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

	// TODO remove and check
	@RequestMapping(value = "user/{username}", method = RequestMethod.GET)
	public ModelAndView getReportsForUser(@PathVariable final String username)
	{
		return getAllReportsForUser(username).addObject("desiredUser",
				userDAO.findByUserNameLocalThread(username));
	}

	@RequestMapping(value =
	{ "" }, method = RequestMethod.GET)
	public ModelAndView defualtView()
	{
		return getAllReportsForUser("");
	}

	@RequestMapping(value = "", consumes = "application/json", produces = "application/json")
	public @ResponseBody
	List<Report> getReportsJson()
	{
		return reportDao.getAllReports();
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
		report.setExpiration(new Date(
				System.currentTimeMillis() + 1000 * 60 * 60));
		reportDao.addReport(report, file);
		return "redirect:";
	}

	@RequestMapping(value = "search", method = RequestMethod.GET)
	public @ResponseBody
	List<Report> getReportsInJSON(@RequestParam final String q)
	{
		return reportDao.getAllReports(q);
	}

	@RequestMapping(value = "{id}", method = RequestMethod.DELETE)
	public @ResponseBody
	String delete(@PathVariable final long id)
	{
		reportDao.removeReport(id);
		return String.valueOf(id);
	}
}
