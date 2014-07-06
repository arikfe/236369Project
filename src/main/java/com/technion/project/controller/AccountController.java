package com.technion.project.controller;

import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
	public String add(final User user)
	{

		final HashSet<UserRole> hashSet = new HashSet<UserRole>();
		hashSet.add(new UserRole(user, "ROLE_USER"));
		user.setUserRole(hashSet);
		user.setEnabled(true);
		userDao.add(user);
		return "redirect:../login";
	}
}
