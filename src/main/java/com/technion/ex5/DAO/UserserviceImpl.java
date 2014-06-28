package com.technion.ex5.DAO;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.technion.ex5.Data.User;

@Service("userService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class UserserviceImpl implements UserService
{
	@Autowired
	private UserDAO employeeDao;

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void addUser(User User)
	{
		employeeDao.addContact(User);

	}

	public List<User> listUserss()
	{
		return employeeDao.listUsers();
	}

	public User getUser(long id)
	{
		return employeeDao.getUser(id);
	}

	public void deleteUser(User user)
	{
		employeeDao.removeUser(user);

	}

}
