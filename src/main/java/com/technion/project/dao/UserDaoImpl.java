package com.technion.project.dao;

import java.util.Collection;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.technion.project.UserSecurityConfig;
import com.technion.project.model.User;

@Repository
public class UserDaoImpl extends BaseDAO implements UserDao
{

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private ReportDAO reportDao;
	@Autowired
	private DocumentDAO documentDao;

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
		executeQuery(new QueryRunner()
		{
			@Override
			public void execueSafe(final Session session)
			{
				final UserSecurityConfig sc = new UserSecurityConfig();
				final PasswordEncoder encoder = sc.passwordEncoder();
				user.setEnabled(true);
				user.setPassword(encoder.encode(user.getPassword()));
				session.saveOrUpdate(user);
			}
		});
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

	@Override
	public void delete(final User user)
	{
		executeQuery(new QueryRunner()
		{
			@Override
			public void execueSafe(final Session session)
			{
				reportDao.removeReport(user, session);
				final Long imageId = user.getImageId();
				session.delete(user);
				documentDao.remove(imageId, session);
			}
		});
	}

	@Override
	public void toggleEnabled(final User user)
	{
		executeQuery(new QueryRunner()
		{
			@Override
			public void execueSafe(final Session session)
			{
				user.setEnabled(!user.isEnabled());
				session.update(user);
			}
		});
	}

	@Override
	public boolean add(final User user, final MultipartFile file)
	{
		return executeQuery(new QueryRunner()
		{

			@Override
			public void execueSafe(final Session session)
			{
				if (!file.isEmpty())
					user.setImageId(documentDao.save(file));
				final UserSecurityConfig sc = new UserSecurityConfig();
				final PasswordEncoder encoder = sc.passwordEncoder();
				user.setEnabled(true);
				user.setPassword(encoder.encode(user.getPassword()));
				session.saveOrUpdate(user);

			}
		});
	}

	@Override
	public void update(final User user)
	{
		executeQuery(new QueryRunner()
		{
			@Override
			public void execueSafe(final Session session)
			{
				session.update(user);
			}
		});

	}

	@Override
	public boolean resetPassword(final String oldpass, final String password,
			final User user)
	{
		final UserSecurityConfig sc = new UserSecurityConfig();
		final PasswordEncoder encoder = sc.passwordEncoder();
		if (!encoder.matches(oldpass, user.getPassword()))
			return false;
		return executeQuery(new QueryRunner()
		{

			@Override
			public void execueSafe(final Session session)
			{
				user.setPassword(encoder.encode(password));
				session.update(user);
			}
		});

	}

	@Override
	public Collection<User> getUserWithNoEvent()
	{
		final List<User> allUsers = getAll();
		return Collections2.filter(allUsers, new Predicate<User>()
		{

			@Override
			public boolean apply(final User u)
			{
				return u.getEvent() == null;
			}
		});
	}

	@Override
	protected Session getSession()
	{
		return sessionFactory.openSession();
	}
}