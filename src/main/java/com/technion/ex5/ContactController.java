package com.technion.ex5;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.technion.ex5.DAO.UserService;
import com.technion.ex5.Data.User;
import com.technion.ex5.Data.UserImpl;

@Controller
public class ContactController
{
	@Autowired
	private UserService userService;

	@RequestMapping("/index")
	public String listContacts(Map<String, Object> map)
	{

		map.put("contact", new UserImpl(12));
		map.put("contactList", userService.listUserss());

		return "contact";
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String addContact(@ModelAttribute("user") User user,
			BindingResult result)
	{

		userService.addUser(user);

		return "redirect:/index";
	}

	@RequestMapping("/delete/{contactId}")
	public String deleteContact(@ModelAttribute("user") User user)
	{

		userService.deleteUser(user);

		return "redirect:/index";
	}
}
