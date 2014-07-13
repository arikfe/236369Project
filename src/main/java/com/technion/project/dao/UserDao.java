package com.technion.project.dao;

import java.util.List;

import com.technion.project.model.User;

public interface UserDao
{

	User findByUserName(String username);

	void add(User user);

	User findByUserNameLocalThread(String username);

	List<User> getAll();

	void delete(User user);

	void toggleEnabled(User user);
}