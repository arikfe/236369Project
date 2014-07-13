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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.technion.project.dao.EvacuationDAO;
import com.technion.project.dao.ReportDAOImpl;
import com.technion.project.dao.UserDao;
import com.technion.project.model.Report;

@Controller
@RequestMapping("/reports/*")
public class ReportsController
{

	@Autowired
	private ReportDAOImpl reportDao;
	@Autowired
	private UserDao userDAO;

	@Autowired
	private EvacuationDAO evacuationDAO;

	@RequestMapping(value = "{username}", method = RequestMethod.GET)
	public ModelAndView getReportsForUser(@PathVariable final String username)
	{

		return getAllReportsForUser(username);

	}

	@RequestMapping(value =
	{ "" }, method = RequestMethod.GET)
	public ModelAndView defualtView()
	{
		return getAllReportsForUser("");
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
		model.setViewName("allReport");
		final Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		if (!"anonymousUser".equals(auth.getName()))
			model.addObject("user",
					userDAO.findByUserNameLocalThread(auth.getName()));
		return model;
	}

	@RequestMapping(value =
	{ "addReport" }, method = RequestMethod.GET)
	public ModelAndView addReport()
	{
		final Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		final ModelAndView model = new ModelAndView();
		model.addObject("title", "Spring Security + Hibernate Example");
		model.addObject("message", "This is default page!");
		if (!"anonymousUser".equals(auth.getName()))
		{
			model.addObject("fname",
					userDAO.findByUserNameLocalThread(auth.getName())
							.getFname());
			model.addObject("nname",
					userDAO.findByUserNameLocalThread(auth.getName())
							.getLname());
		}
		model.setViewName("addReport");
		return model;
	}

	@RequestMapping(value =
	{ "add" }, method = RequestMethod.GET)
	public String addReportToDB(final Report report)
	{
		report.setExpiration(new Date(
				System.currentTimeMillis() + 1000 * 60 * 60));
		reportDao.addReport(report);
		return "redirect:";
	}

	@RequestMapping(value = "/json/{name}", method = RequestMethod.GET)
	public @ResponseBody Report getShopInJSON(@PathVariable final String name)
	{

		final Report report = reportDao.getReportByID(Long.valueOf(name));
		return report;

	}
}
