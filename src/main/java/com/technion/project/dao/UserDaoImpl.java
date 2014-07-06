package com.technion.project.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;
import com.technion.project.UserSecurityConfig;
import com.technion.project.model.User;
import com.technion.project.model.UserRole;

@Repository
public class UserDaoImpl implements UserDao
{

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	@SuppressWarnings("unchecked")
	public User findByUserName(final String username)
	{

		List<User> users = Lists.newArrayList();

		final Session session = sessionFactory.getCurrentSession();
		users = session.createQuery("from User where username=?")
				.setParameter(0, username).list();
		if (users.size() > 0)
			return users.get(0);
		else
			return null;

	}

	@Override
	@SuppressWarnings("unchecked")
	public User findByUserNameLocalThread(final String username)
	{

		List<User> users = Lists.newArrayList();

		final Session session = sessionFactory.openSession();
		users = session.createQuery("from User where username=?")
				.setParameter(0, username).list();
		session.close();
		if (users.size() > 0)
			return users.get(0);
		else
			return null;

	}

	@Override
	public void add(final User user)
	{
		final UserSecurityConfig sc = new UserSecurityConfig();
		user.setEnabled(true);
		final PasswordEncoder encoder = sc.passwordEncoder();
		final Session currentSession = sessionFactory.openSession();
		user.setPassword(encoder.encode(user.getPassword()));
		currentSession.saveOrUpdate(user);
		for (final UserRole userRole : user.getUserRole())
		{
			userRole.setUser(user);
			currentSession.saveOrUpdate(userRole);
		}
		currentSession.flush();
		currentSession.close();
	}

}