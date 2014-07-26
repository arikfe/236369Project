package com.technion.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.technion.project.dao.UserDao;
import com.technion.project.model.User;

public class BaseController
{

	@Autowired
	protected UserDao userDao;

	public BaseController()
	{
		super();
	}

	protected User getCurrentUser()
	{
		final Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		return userDao.findByUserNameLocalThread(auth.getName());
	}

}