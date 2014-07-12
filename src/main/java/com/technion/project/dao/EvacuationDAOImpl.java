package com.technion.project.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;
import com.technion.project.model.EvacuationEvent;
import com.technion.project.model.User;

@Repository
public class EvacuationDAOImpl implements EvacuationDAO
{
	@Autowired
	private SessionFactory sessionFactory;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.technion.project.dao.EvacuationDAO#getByID(java.lang.Long)
	 */
	@Override
	public EvacuationEvent getByID(final Long id)
	{
		final Session session = sessionFactory.openSession();
		final EvacuationEvent evacuationEvent = (EvacuationEvent) session.get(
				EvacuationEvent.class, id);
		session.close();
		return evacuationEvent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.technion.project.dao.EvacuationDAO#addEvecuationEvent(com.technion
	 * .project.model.EvacuationEvent)
	 */
	@Override
	public void addEvecuationEvent(final EvacuationEvent event)
	{
		final Session session = sessionFactory.openSession();
		session.save(event);
		session.flush();
		session.close();
	}

	@Override
	public void update(final EvacuationEvent event)
	{
		final Session session = sessionFactory.openSession();
		session.update(event);
		session.flush();
		session.close();
	}

	@Override
	public void addUserToEvent(final User user, final long id)
	{

		final Session session = sessionFactory.openSession();
		final EvacuationEvent evacuationEvent = (EvacuationEvent) session.get(
				EvacuationEvent.class, id);
		evacuationEvent.addUser(user);
		session.update(evacuationEvent);
		session.flush();
		session.close();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<EvacuationEvent> getAll()
	{
		List<EvacuationEvent> evacuationEvents = Lists.newArrayList();
		final Session session = sessionFactory.openSession();
		evacuationEvents = session.createQuery("from EvacuationEvent").list();
		session.close();
		return evacuationEvents;
	}

	@Override
	public void removeUserToEvent(final User user, final long id)
	{
		final Session session = sessionFactory.openSession();
		final EvacuationEvent evacuationEvent = (EvacuationEvent) session.get(
				EvacuationEvent.class, id);
		evacuationEvent.removeUser(user);
		session.update(evacuationEvent);
		session.flush();
		session.close();

	}
}
