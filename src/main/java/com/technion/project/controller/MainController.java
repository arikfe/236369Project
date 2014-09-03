package com.technion.project.controller;

import java.io.IOException;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.List;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.ByteStreams;
import com.technion.project.dao.DocumentDAO;
import com.technion.project.dao.EvacuationDAO;
import com.technion.project.dao.ReportDAO;
import com.technion.project.dao.UserDao;
import com.technion.project.model.BaseModel;
import com.technion.project.model.EvacuationEvent;
import com.technion.project.model.Report;
import com.technion.project.model.User;
import com.technion.project.model.UserRole;
import com.thoughtworks.xstream.XStream;

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

	@RequestMapping(value = "", consumes = "application/xml", produces = "application/xml")
	public @ResponseBody List<BaseModel> getBaseXML()
	{
		final List<BaseModel> data = Lists.newLinkedList();
		data.addAll(reportDAO.getAllReports());
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

	@RequestMapping(value = "/admin/importfile", method = RequestMethod.POST)
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
			final SimpleDateFormat dateFormat = new SimpleDateFormat(
					"yyyy-MM-dd'T'HH:mm:ss XXX");
			final JsonNode actualObj = mapper.readTree(theString);
			final ArrayNode users = (ArrayNode) actualObj.get("users");
			final ArrayNode reports = (ArrayNode) actualObj.get("reports");
			final ArrayNode events = (ArrayNode) actualObj
					.get("evacuationEvents");
			userDao.clear();
			reportDAO.clear();
			evacuationDAO.clear();
			// documentDao.clear();
			final Map<String, Long> pathToImage = getImages(users, reports);
			for (int i = 0; i < users.size(); i++)
			{
				final User user = new User();
				user.setEnabled(true);
				user.setFname(users.get(i).get("name").getTextValue());
				user.setLname("");
				user.setUsername(users.get(i).get("username").getTextValue());
				user.setPassword(users.get(i).get("password").getTextValue());
				user.setImageId(pathToImage.get(users.get(i).get("photo")
						.getTextValue()));
				final HashSet<UserRole> hashSet = new HashSet<UserRole>();
				hashSet.add(new UserRole(user, "ROLE_USER"));
				user.setUserRole(hashSet);
				userDao.add(user);
			}
			for (int i = 0; i < reports.size(); i++)
			{
				final Report report = new Report();
				report.setUsername(reports.get(i).get("user").getTextValue());
				report.setContent(reports.get(i).get("content").getTextValue());
				report.setImageId(pathToImage.get(reports.get(i).get("picture")
						.getTextValue()));
				report.setExpiration(dateFormat.parse(reports.get(i)
						.get("expirationTime").getTextValue()));
				report.setGeolat((float) reports.get(i).get("geometry")
						.get("coordinates").get(0).getDoubleValue());
				report.setGeolng((float) reports.get(i).get("geometry")
						.get("coordinates").get(1).getDoubleValue());
				report.setTitle(reports.get(i).get("title").getTextValue());
				reportDAO.addReport(report, null);
			}
			for (int i = 0; i < events.size(); i++)
			{
				final EvacuationEvent event = new EvacuationEvent();
				event.setGeolat((float) events.get(i).get("geometry")
						.get("coordinates").get(0).getDoubleValue());
				event.setGeolat((float) events.get(i).get("geometry")
						.get("coordinates").get(1).getDoubleValue());
				event.setEstimated(dateFormat.parse(events.get(i)
						.get("estimatedTime").getTextValue()));
				event.setMeans(events.get(i).get("meanOfEvacuation")
						.getTextValue());
				event.setCapacity(events.get(i).get("capacity").getIntValue());
				evacuationDAO.addEvecuationEvent(event);
			}

		} catch (final JsonProcessingException e)
		{
			e.printStackTrace();
		} catch (final IOException e)
		{
			e.printStackTrace();
		} catch (final ParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "redirect:";
	}

	private Map<String, Long> getImages(final ArrayNode users,
			final ArrayNode reports) throws IOException
	{
		final Map<String, Long> pathToImage = Maps.newHashMap();
		for (int i = 0; i < users.size(); i++)
			downloadImage(pathToImage, users.get(i), "photo");
		for (int i = 0; i < reports.size(); i++)
			downloadImage(pathToImage, reports.get(i), "picture");
		return pathToImage;
	}

	private void downloadImage(final Map<String, Long> pathToImage,
			final JsonNode jsonNode, final String pictureJsonString)
			throws MalformedURLException, IOException
	{
		URLConnection uCon = null;
		final String path = jsonNode.get(pictureJsonString).getTextValue();
		if (pathToImage.containsKey(path))
			return;
		final URL url = new URL(path);
		uCon = url.openConnection();
		uCon.getContentType();
		pathToImage.put(path, documentDao.save(ByteStreams.toByteArray(uCon
				.getInputStream()), uCon.getContentType(), FilenameUtils
				.getBaseName(jsonNode.get(pictureJsonString).getTextValue())));
	}
}
