package com.technion.ex5.DAO;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.technion.ex5.Data.User;
import com.technion.ex5.Data.UserImpl;

@Repository
public class UserDAOImpl implements UserDAO
{
	@Autowired
	private SessionFactory sessionFactory;

	public void addContact(User contact)
	{
		sessionFactory.getCurrentSession().saveOrUpdate(contact);

	}

	@SuppressWarnings("unchecked")
	public List<User> listUsers()
	{
		return sessionFactory.getCurrentSession()
				.createCriteria(UserImpl.class).list();
	}

	public User getUser(long id)
	{
		return (User) sessionFactory.getCurrentSession()
				.get(UserImpl.class, id);
	}

	public void removeUser(User user)
	{
		sessionFactory.getCurrentSession().delete(user);
	}

	public boolean exist(long id)
	{
		return sessionFactory.getCurrentSession().get(UserImpl.class, id) != null;
	}

}
