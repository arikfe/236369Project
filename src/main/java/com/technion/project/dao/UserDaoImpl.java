package com.technion.project.dao;

import java.util.Collection;
import java.util.List;

import javax.swing.JOptionPane;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
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
public class UserDaoImpl implements UserDao
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
		final Session currentSession = sessionFactory.openSession();
		addUserForSession(user, currentSession);
		currentSession.flush();
		currentSession.close();
	}

	private void addUserForSession(final User user, final Session currentSession)
	{
		final UserSecurityConfig sc = new UserSecurityConfig();
		final PasswordEncoder encoder = sc.passwordEncoder();
		user.setEnabled(true);
		user.setPassword(encoder.encode(user.getPassword()));
		currentSession.saveOrUpdate(user);
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

	// TODO add locks here
	@Override
	public void delete(final User user)
	{
		reportDao.removeReport(user);
		final Session session = sessionFactory.openSession();
		final Transaction transaction = session.getTransaction();
		transaction.begin();
		final Long imageId = user.getImageId();
		session.delete(user);
		transaction.commit();
		session.flush();
		session.close();
		documentDao.remove(imageId);
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

	@Override
	public boolean add(final User user, final MultipartFile file)
	{
		final Session currentSession = sessionFactory.openSession();
		if (!file.isEmpty())
			user.setImageId(documentDao.save(file));
		try
		{
			addUserForSession(user, currentSession);
			currentSession.flush();
			currentSession.close();
		} catch (final Exception e)
		{
			JOptionPane.showMessageDialog(null,
					"Fail to perform update. Try again", "Error",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}

	@Override
	public void update(final User user)
	{
		final Session currentSession = sessionFactory.openSession();
		currentSession.update(user);
		currentSession.flush();
		currentSession.close();

	}

	@Override
	public boolean resetPassword(final String oldpass, final String password,
			final User user)
	{
		final UserSecurityConfig sc = new UserSecurityConfig();
		final PasswordEncoder encoder = sc.passwordEncoder();

		if (!encoder.matches(oldpass, user.getPassword()))
			return false;
		user.setPassword(encoder.encode(password));
		update(user);
		return true;

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
}