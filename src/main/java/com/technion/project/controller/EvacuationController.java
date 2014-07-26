package com.technion.project.controller;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.technion.project.dao.EvacuationDAO;
import com.technion.project.model.EvacuationEvent;
import com.technion.project.model.User;

@Controller
@RequestMapping("/evacuation/*")
public class EvacuationController extends BaseController
{
	@Autowired
	private EvacuationDAO evacuationDAO;

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

	@RequestMapping(value = "join", method = RequestMethod.GET)
	public String join(final long id)
	{
		evacuationDAO.addUserToEvent(getCurrentUser(), id);
		return "redirect:../login";
	}

	@RequestMapping(value = "joinUser", method = RequestMethod.GET)
	public String joinUser(final long id, final String username)
	{
		evacuationDAO.addUserToEvent(
				userDao.findByUserNameLocalThread(username), id);
		return "redirect:../login";
	}

	@RequestMapping(value =
	{ "leave" }, method = RequestMethod.GET)
	public String leave(final long id)
	{
		evacuationDAO.removeUserToEvent(getCurrentUser(), id);
		return "redirect:../login";
	}

	@RequestMapping(value =
	{ "leaveUser" }, method = RequestMethod.GET)
	public String leaveUser(final long id, final String username)
	{
		evacuationDAO.removeUserToEvent(
				userDao.findByUserNameLocalThread(username), id);
		return "redirect:../login";
	}

	@RequestMapping(value =
	{ "list" }, method = RequestMethod.GET)
	public @ResponseBody Set<User> list(final long id)
	{
		return evacuationDAO.getByID(id).getRegisteredUsers();
	}

	@RequestMapping(value = "registeredEvent", method = RequestMethod.GET)
	public @ResponseBody EvacuationEvent registeredEvent()
	{
		return getCurrentUser().getEvent();
	}

	@RequestMapping(value = "closestEvent", method = RequestMethod.GET)
	public @ResponseBody EvacuationEvent getClosestEvent(
			@RequestParam final float lat, @RequestParam final float lng)
	{
		return evacuationDAO.getClosest(lat, lng);
	}

	@RequestMapping(value = "id/{id}", method = RequestMethod.GET)
	public ModelAndView getById(@PathVariable final long id)
	{
		final ModelAndView view = new ModelAndView();
		view.addObject("evacuation", evacuationDAO.getByID(id));
		view.setViewName("evacuation");
		view.addObject("id", id);
		view.addObject("users", userDao.getUserWithNoEvent());
		return view;
	}
}
