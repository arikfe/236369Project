package com.technion.project.controller;

import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.technion.project.dao.UserDao;
import com.technion.project.model.User;
import com.technion.project.model.UserRole;

@Controller
@RequestMapping("/accounts/*")
public class AccountController
{

	@Autowired
	private UserDao userDao;

	@RequestMapping(value =
	{ "add" }, method = RequestMethod.POST)
	public String add(final User user, final MultipartFile file)
	{

		final HashSet<UserRole> hashSet = new HashSet<UserRole>();
		hashSet.add(new UserRole(user, "ROLE_USER"));
		user.setUserRole(hashSet);
		user.setEnabled(true);
		userDao.add(user);
		return "redirect:../login";
	}

	@RequestMapping(value = "users", method = RequestMethod.GET)
	public ModelAndView users()
	{
		final ModelAndView model = new ModelAndView();
		model.addObject("users", userDao.getAll());
		final Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();

		final User currentUser = userDao.findByUserNameLocalThread(auth
				.getName());
		model.addObject("adminRight",
				Boolean.valueOf(currentUser.hasAdminPrevilige()));
		model.addObject("user", currentUser);

		model.setViewName("users");
		return model;
	}

	@RequestMapping(value = "disable/{username}", method = RequestMethod.GET)
	public ModelAndView toggleUser(@PathVariable final String username)
	{

		final Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		final ModelAndView model = new ModelAndView();
		final User user = userDao.findByUserNameLocalThread(auth.getName());
		if (!user.hasAdminPrevilige())
		{
			model.setViewName("403");
			return model;
		}
		userDao.toggleEnabled(userDao.findByUserNameLocalThread(username));
		model.setViewName("redirect:../");
		return model;
	}

	@RequestMapping(value = "delete/{username}", method = RequestMethod.GET)
	public ModelAndView deleteUser(@PathVariable final String username)
	{

		final Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		final ModelAndView model = new ModelAndView();
		final User user = userDao.findByUserNameLocalThread(auth.getName());
		if (!user.hasAdminPrevilige())
		{
			model.setViewName("403");
			return model;
		}
		userDao.delete(userDao.findByUserNameLocalThread(username));
		model.setViewName("redirect:../");
		return model;
	}

	@RequestMapping(value = "deleteself", method = RequestMethod.GET)
	public ModelAndView deleteSelf()
	{

		final Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		final ModelAndView model = new ModelAndView();
		final User user = userDao.findByUserNameLocalThread(auth.getName());
		userDao.delete(user);
		model.setViewName("redirect:../");
		return model;
	}
}
