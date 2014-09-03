package com.technion.project.dao;

import java.util.Collection;
import java.util.List;

import com.technion.project.model.User;

public interface UserDao
{

	User findByUserName(String username);

	boolean add(User user);

	User findByUserNameLocalThread(String username);

	List<User> getAll();

	void delete(User user);

	void toggleEnabled(User user);

	void update(User user);

	boolean resetPassword(String oldpass, String password, User userFromDB);

	Collection<User> getUserWithNoEvent();

	void clear();

}