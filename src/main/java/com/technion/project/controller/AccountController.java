package com.technion.project.controller;

import java.util.HashSet;
import java.util.List;

import javax.servlet.annotation.MultipartConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.technion.project.dao.DocumentDAO;
import com.technion.project.dao.EvacuationDAO;
import com.technion.project.dao.ReportDAO;
import com.technion.project.dao.UserDao;
import com.technion.project.model.EvacuationEvent;
import com.technion.project.model.Report;
import com.technion.project.model.User;
import com.technion.project.model.UserRole;

@Controller
@RequestMapping("/accounts/*")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
maxFileSize = 1024 * 1024 * 10, // 10MB
maxRequestSize = 1024 * 1024 * 50)
public class AccountController extends BaseController
{
	@Autowired
	private ReportDAO reportDao;
	@Autowired
	private UserDao userDAO;
	@Autowired
	private EvacuationDAO evacuationDAO;

	@Autowired
	private DocumentDAO documentDao;

	@RequestMapping(value =
	{ "" }, method = RequestMethod.POST)
	public ModelAndView add(@ModelAttribute("User") final User user,
			@RequestParam("file") final MultipartFile file)
	{
		final ModelAndView view = new ModelAndView();
		final HashSet<UserRole> hashSet = new HashSet<UserRole>();
		hashSet.add(new UserRole(user, "ROLE_USER"));
		user.setUserRole(hashSet);
		user.setEnabled(true);
		if (!file.isEmpty())
			user.setImageId(documentDao.save(file));
		if (userDAO.add(user))
			view.setViewName("login");
		else
		{
			view.setViewName("register");
			view.addObject("error", "Fail to register. User allready exists.");
		}
		return view;
	}

	@RequestMapping(value = "", method = RequestMethod.GET, consumes = "application/json", produces = "application/json")
	public @ResponseBody List<User> getUsersJson()
	{
		return userDAO.getAll();
	}

	@RequestMapping(value = "", method = RequestMethod.GET)
	public ModelAndView getUsersView()
	{
		final ModelAndView model = new ModelAndView();
		model.addObject("users", getUsersJson());
		final User currentUser = getCurrentUser();
		model.addObject("adminRight",
				Boolean.valueOf(currentUser.hasAdminPrevilige()));
		model.addObject("user", currentUser);

		model.setViewName("users");
		return model;
	}

	@RequestMapping(value = "/{username}/disable", method = RequestMethod.POST)
	public ModelAndView toggleUser(@PathVariable final String username)
	{

		final ModelAndView model = new ModelAndView();
		if (!canEditAccount(username))
			return unauthorized();
		userDAO.toggleEnabled(userDAO.findByUserNameLocalThread(username));
		model.setViewName("redirect:../");
		return model;
	}

	@RequestMapping(value = "{username}", method = RequestMethod.DELETE)
	public @ResponseBody boolean deleteUser(@PathVariable final String username)
	{
		if (!canEditAccount(username))
			return false;
		userDAO.delete(userDAO.findByUserNameLocalThread(username));
		return true;
	}

	private boolean canEditAccount(final String username)
	{
		final User currentUser = getCurrentUser();
		return currentUser.hasAdminPrevilige()
				|| currentUser.getUsername().equalsIgnoreCase(username);
	}

	@RequestMapping(value = "{username}", method = RequestMethod.PUT)
	public String updateUserDetails(@PathVariable final String username,
			@RequestBody final MultiValueMap<String, String> body)
	{
		final User userFromDB = getCurrentUser();
		if (!userFromDB.getUsername().equalsIgnoreCase(username))
			return "";
		userFromDB.setFname(body.getFirst("fname"));
		userFromDB.setLname(body.getFirst("lname"));
		userDAO.update(userFromDB);

		return "redirect:///236369project//reports";
	}

	@RequestMapping(value = "{username}", method = RequestMethod.GET, consumes = "application/json", produces = "application/json")
	public User getUserJson(@PathVariable final String username)
	{
		return userDAO.findByUserNameLocalThread(username);
	}

	@RequestMapping(value = "{username}", method = RequestMethod.GET)
	public ModelAndView userDetails(@PathVariable final String username)
	{
		final ModelAndView view = new ModelAndView();
		final Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		final User userFromDB = userDAO.findByUserNameLocalThread(auth
				.getName());
		view.setViewName("own");
		view.addObject("user", userFromDB);
		return view;
	}

	@RequestMapping(value = "{username}/reset", method = RequestMethod.POST)
	public ModelAndView resetPassword(@PathVariable final String username,
			@RequestParam final String oldpass,
			@RequestParam final String password)
	{
		final User userFromDB = getCurrentUser();
		if (!username.equalsIgnoreCase(userFromDB.getUsername()))
			return unauthorized();
		final ModelAndView view = new ModelAndView();
		view.setViewName("own");
		view.addObject("user", userFromDB);
		if (userDAO.resetPassword(oldpass, password, userFromDB))
			view.addObject("result", "password updated");
		else
			view.addObject("result", "old password do not match");
		return view;
	}

	@RequestMapping(value = "{username}/reports")
	public ModelAndView getReportsForUsers(@PathVariable final String username)
	{
		return getAllReportsForUser(username).addObject("desiredUser",
				userDAO.findByUserNameLocalThread(username));
	}

	@RequestMapping(value = "{username}/event", method = RequestMethod.GET, consumes = "application/json", produces = "application/json")
	public @ResponseBody EvacuationEvent registeredEvent()
	{
		return getCurrentUser().getEvent();
	}

	/**
	 * used for json
	 *
	 * @param username
	 * @return
	 */
	@RequestMapping(value = "{username}/reports", consumes = "application/json", produces = "application/json")
	public @ResponseBody List<Report> getReportsForUser(
			@PathVariable final String username)
	{
		return reportDao.getReportsForUser(userDAO
				.findByUserNameLocalThread(username));
	}

	private ModelAndView getAllReportsForUser(final String username)
	{
		final ModelAndView model = new ModelAndView();
		final List<Report> allReports = getReportsForUser(username);
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
		model.addObject("subSite", "../accounts/" + username + "/reports");
		model.addObject("userPath", "user/" + username);
		model.setViewName("allReport");
		final Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		if (!"anonymousUser".equals(auth.getName()))
			model.addObject("user",
					userDAO.findByUserNameLocalThread(auth.getName()));
		return model;
	}
}
