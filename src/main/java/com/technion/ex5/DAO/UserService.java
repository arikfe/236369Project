package com.technion.ex5.DAO;

import java.util.List;

import com.technion.ex5.Data.User;

public interface UserService
{
	public void addUser(User User);

	public List<User> listUserss();

	public User getUser(long empid);

	public void deleteUser(User User);
}
