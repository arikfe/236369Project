package com.technion.project.dao;

import java.util.Collection;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.technion.project.model.User;

public interface UserDao
{

	User findByUserName(String username);

	void add(User user);

	User findByUserNameLocalThread(String username);

	List<User> getAll();

	void delete(User user);

	void toggleEnabled(User user);

	boolean add(User user, MultipartFile file);

	void update(User user);

	boolean resetPassword(String oldpass, String password, User userFromDB);

	Collection<User> getUserWithNoEvent();
}