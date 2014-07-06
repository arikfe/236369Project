package com.technion.project.dao;

import com.technion.project.model.User;

public interface UserDao
{

	User findByUserName(String username);

	void add(User user);

	User findByUserNameLocalThread(String username);

}