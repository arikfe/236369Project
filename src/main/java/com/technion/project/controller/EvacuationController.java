package com.technion.project.controller;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.technion.project.dao.EvacuationDAO;
import com.technion.project.dao.UserDao;
import com.technion.project.model.EvacuationEvent;
import com.technion.project.model.User;

@Controller
@RequestMapping("/evacuation/*")
public class EvacuationController
{
	@Autowired
	private EvacuationDAO evacuationDAO;

	@Autowired
	private UserDao userDao;

	@RequestMapping(value =
	{ "add" }, method = RequestMethod.GET)
	public String add(final EvacuationEvent event)
	{
		final HashSet<User> hashSet = new HashSet<User>();
		event.setRegisteredUsers(hashSet);
		event.setEstimated(new Date());
		evacuationDAO.addEvecuationEvent(event);
		return "redirect:../admin";
	}

	@RequestMapping(value =
	{ "join" }, method = RequestMethod.GET)
	public String join(final long id)
	{
		final Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		final User user = userDao.findByUserNameLocalThread(auth.getName());
		evacuationDAO.addUserToEvent(user, id);
		return "redirect:../login";
	}

	@RequestMapping(value =
	{ "leave" }, method = RequestMethod.GET)
	public String leave(final long id)
	{
		final Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		final User user = userDao.findByUserNameLocalThread(auth.getName());
		evacuationDAO.removeUserToEvent(user, id);
		return "redirect:../login";
	}

	@RequestMapping(value =
	{ "list" }, method = RequestMethod.GET)
	public @ResponseBody Set<User> list(final long id)
	{
		return evacuationDAO.getByID(id).getRegisteredUsers();
	}

}
