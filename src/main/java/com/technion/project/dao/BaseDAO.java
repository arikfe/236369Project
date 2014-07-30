package com.technion.project.dao;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public abstract class BaseDAO
{

	public BaseDAO()
	{
		super();
	}

	protected boolean executeQuery(final QueryRunner runner)
	{
		final Session session = getSession();
		final Transaction transaction = session.getTransaction();
		transaction.begin();
		try
		{
			runner.execueSafe(session);
			transaction.commit();
			session.flush();
		} catch (final HibernateException e)
		{
			transaction.rollback();
			return false;
		} finally
		{
			session.close();
		}
		return true;
	}

	protected abstract Session getSession();

}