package com.technion.project.controller;

import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.collect.Maps;
import com.google.common.io.ByteStreams;
import com.technion.project.dao.DocumentDAO;
import com.technion.project.dao.EvacuationDAO;
import com.technion.project.dao.ReportDAO;
import com.technion.project.dao.UserDao;
import com.technion.project.model.User;

@Controller
public class MainController
{
	@Autowired
	private UserDao userDao;

	@Autowired
	private ReportDAO reportDAO;

	@Autowired
	private EvacuationDAO evacuationDAO;

	@Autowired
	private DocumentDAO documentDao;

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
					userDao.findByUserNameLocalThread(auth.getName())
							.getFname());
			model.addObject("nname",
					userDao.findByUserNameLocalThread(auth.getName())
							.getLname());
		}
		model.setViewName("addReport");
		return model;
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
		final User user = userDao.findByUserNameLocalThread(auth.getName());
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

	public String importData(@RequestParam("file") final MultipartFile file)
	{
		final StringWriter writer = new StringWriter();
		try
		{
			IOUtils.copy(file.getInputStream(), writer, "UTF-8");
		} catch (final IOException e)
		{
			e.printStackTrace();
		}
		final String theString = writer.toString();
		final ObjectMapper mapper = new ObjectMapper();
		try
		{
			final JsonNode actualObj = mapper.readTree(theString);
			final ArrayNode users = (ArrayNode) actualObj.get("users");
			userDao.clear();
			final Map<String, Long> pathToImage = getImages(users);
			for (int i = 0; i < users.size(); i++)
			{
				final User user = new User();
				user.setEnabled(true);
				user.setFname(users.get(i).get("name").getTextValue());
				user.setUsername(users.get(i).get("username").getTextValue());
				user.setPassword(users.get(i).get("password").getTextValue());
				user.setImageId(pathToImage.get(users.get(i).get("picture")
						.getTextValue()));
			}

		} catch (final JsonProcessingException e)
		{
			e.printStackTrace();
		} catch (final IOException e)
		{
			e.printStackTrace();
		}
		return "redirect:";
	}

	private Map<String, Long> getImages(final ArrayNode users)
			throws IOException
	{
		final Map<String, Long> pathToImage = Maps.newHashMap();
		for (int i = 0; i < users.size(); i++)
		{
			URLConnection uCon = null;
			final String path = users.get(i).get("picture").getTextValue();
			if (pathToImage.containsKey(path))
				continue;
			final URL url = new URL(path);
			uCon = url.openConnection();
			uCon.getContentType();
			pathToImage.put(path, documentDao.save(
					ByteStreams.toByteArray(uCon.getInputStream()),
					uCon.getContentType(),
					FilenameUtils.getBaseName(users.get(i).get("password")
							.getTextValue())));
		}
		return pathToImage;
	}
}