package com.technion.project.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;
import com.technion.project.UserSecurityConfig;
import com.technion.project.model.User;

@Repository
public class UserDaoImpl implements UserDao
{

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private ReportDAO reportDao;

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
		// for (final UserRole userRole : user.getUserRole())
		// {
		// userRole.setUser(user);
		// currentSession.saveOrUpdate(userRole);
		// }
		currentSession.flush();
		currentSession.close();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> getAll()
	{
		List<User> users = Lists.newArrayList();

		final Session session = sessionFactory.openSession();

		users = session.createQuery("from User").list();
		session.close();
		return users;

	}

	// TODO add lockes here
	@Override
	public void delete(final User user)
	{
		// TODO add locks
		reportDao.removeReport(user);
		final Session session = sessionFactory.openSession();
		final Transaction transaction = session.getTransaction();
		transaction.begin();
		session.delete(user);
		transaction.commit();
		session.flush();
		session.close();

	}

	@Override
	public void toggleEnabled(final User user)
	{
		user.setEnabled(!user.isEnabled());
		final Session session = sessionFactory.openSession();
		final Transaction transaction = session.getTransaction();
		transaction.begin();
		session.update(user);
		transaction.commit();
		session.close();
	}

}