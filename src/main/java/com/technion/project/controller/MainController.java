package com.technion.project.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.collect.Lists;
import com.technion.project.dao.EvacuationDAO;
import com.technion.project.dao.ReportDAO;
import com.technion.project.dao.UserDao;
import com.technion.project.model.BaseModel;
import com.technion.project.model.Report;
import com.technion.project.model.User;
import com.thoughtworks.xstream.XStream;

@Controller
public class MainController
{
	@Autowired
	private UserDao userDao;

	@Autowired
	private UserDao userDAO;

	@Autowired
	private ReportDAO reportDao;

	@Autowired
	private EvacuationDAO evacuationDao;

	@RequestMapping(value =
	{ "/", "/welcome**" }, method = RequestMethod.GET)
	public String defaultPage()
	{

		return "redirect:/reports/";
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

	@RequestMapping(value = "", consumes = "application/xml", produces = "application/xml")
	public @ResponseBody List<BaseModel> getBaseXML()
	{
		final List<BaseModel> data = Lists.newLinkedList();
		data.addAll(reportDao.getAllReports());
		// data.addAll(evacuationDao.getAll());
		final XStream xStream = new XStream();
		xStream.processAnnotations(Report.class);
		System.out.println(xStream.toXML(data));
		return data;
	}

	@RequestMapping(value = "/admin**", method = RequestMethod.GET)
	public ModelAndView adminPage()
	{

		final ModelAndView model = new ModelAndView();
		model.addObject("fname", "admin");
		model.setViewName("admin");

		return model;

	}

	@RequestMapping(value = "menu", method = RequestMethod.GET)
	public ModelAndView getMenu()
	{
		final Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		final ModelAndView view = new ModelAndView();
		final User user = userDAO.findByUserNameLocalThread(auth.getName());
		view.setViewName("menu");
		view.addObject("user", user);
		return view;
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView login(
			@RequestParam(value = "error", required = false) final String error,
			@RequestParam(value = "logout", required = false) final String logout,
			final HttpServletRequest request)
	{

		final ModelAndView model = new ModelAndView();
		if (error != null)
			model.addObject("error",
					getErrorMessage(request, "SPRING_SECURITY_LAST_EXCEPTION"));

		if (logout != null)
			model.addObject("msg", "You've been logged out successfully.");
		model.setViewName("login");

		return model;

	}

	// customize the error message
	private String getErrorMessage(final HttpServletRequest request,
			final String key)
	{

		final Exception exception = (Exception) request.getSession()
				.getAttribute(key);

		String error = "";
		if (exception instanceof BadCredentialsException)
			error = "Invalid username and password!";
		else if (exception instanceof LockedException)
			error = exception.getMessage();
		else
			error = "Invalid username and password!";

		return error;
	}

	// for 403 access denied page
	@RequestMapping(value = "/403", method = RequestMethod.GET)
	public ModelAndView accesssDenied()
	{

		final ModelAndView model = new ModelAndView();

		// check if user is login
		final Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken))
		{
			final UserDetails userDetail = (UserDetails) auth.getPrincipal();
			System.out.println(userDetail);

			model.addObject("username", userDetail.getUsername());

		}

		model.setViewName("403");
		return model;

	}

	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public ModelAndView register(
			@RequestParam(value = "error", required = false) final String error,
			final HttpServletRequest request)
	{

		final ModelAndView model = new ModelAndView();

		if (error != null)
			model.addObject("error",
					getErrorMessage(request, "SPRING_SECURITY_LAST_EXCEPTION"));

		model.setViewName("register");
		return model;

	}

	@RequestMapping(value = "/admin/addEventView", method = RequestMethod.GET)
	public ModelAndView addEvacuation()
	{
		final ModelAndView model = new ModelAndView();
		model.setViewName("addEventView");
		return model;
	}

	@RequestMapping(value = "/admin/admin_menu", method = RequestMethod.GET)
	public ModelAndView adminMenu()
	{
		final ModelAndView model = new ModelAndView();
		model.setViewName("admin_menu");
		return model;
	}

}