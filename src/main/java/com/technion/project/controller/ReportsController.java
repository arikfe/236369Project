package com.technion.project.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.technion.project.dao.ReportDAOImpl;
import com.technion.project.model.Report;

@Controller
@RequestMapping("/reports/*")
public class ReportsController
{

	@Autowired
	private ReportDAOImpl reportDao;

	@RequestMapping(value =
	{ "" }, method = RequestMethod.GET)
	public ModelAndView defualtView()
	{
		final ModelAndView model = new ModelAndView();
		model.addObject("title", "Spring Security + Hibernate Example");
		model.addObject("message", "This is default page!");
		model.addObject("reports", reportDao.getAllReports());
		model.setViewName("allReport");
		return model;
	}

	@RequestMapping(value =
	{ "addReport" }, method = RequestMethod.GET)
	public ModelAndView addReport()
	{
		final ModelAndView model = new ModelAndView();
		model.addObject("title", "Spring Security + Hibernate Example");
		model.addObject("message", "This is default page!");
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
}
