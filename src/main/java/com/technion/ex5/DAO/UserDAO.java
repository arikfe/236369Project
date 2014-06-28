package com.technion.ex5.DAO;

import java.util.List;

import com.technion.ex5.Data.User;

public interface UserDAO
{
	public void addContact(User user);

	public List<User> listUsers();

	public void removeUser(User user);

	public boolean exist(long id);

	public User getUser(long id);
}
