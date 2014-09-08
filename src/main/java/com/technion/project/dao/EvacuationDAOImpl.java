package com.technion.project.dao;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.query.dsl.BooleanJunction;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;
import com.technion.project.model.EvacuationEvent;
import com.technion.project.model.User;

@Repository
public class EvacuationDAOImpl extends BaseDAO implements EvacuationDAO
{
	private final class DistanceComperator implements
			Comparator<EvacuationEvent>
	{
		private final float lat;
		private final float lng;

		private DistanceComperator(final float lat, final float lng)
		{
			this.lat = lat;
			this.lng = lng;
		}

		@Override
		public int compare(final EvacuationEvent o1, final EvacuationEvent o2)
		{
			return new Double(getDistanceToEvent(lat, lng, o1))
					.compareTo(getDistanceToEvent(lat, lng, o2));

		}

		private double getDistanceToEvent(final float lat, final float lng,
				final EvacuationEvent o1)
		{
			return Math.sqrt(Math.pow(lat - o1.getGeolat(), 2)
					+ Math.pow(lng - o1.getGeolng(), 2));
		}
	}

	@Autowired
	SessionFactory sessionFactory;

	/*
	 * (non-Javadoc)
	 *
	 * @see com.technion.project.dao.EvacuationDAO#getByID(java.lang.Long)
	 */
	@Override
	public EvacuationEvent getByID(final Long id)
	{
		final Session session = getSession();
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
		executeQuery(new QueryRunner()
		{
			@Override
			public void execueSafe(final Session session)
			{
				session.save(event);
			}
		});
	}

	@Override
	public void update(final EvacuationEvent event)
	{
		executeQuery(new QueryRunner()
		{
			@Override
			public void execueSafe(final Session session)
			{
				session.update(event);
			}
		});
	}

	@Override
	public boolean addUserToEvent(final User user, final long id)
	{
		if (user.getEvent() != null)
			return false;
		return executeQuery(new QueryRunner()
		{
			@Override
			public void execueSafe(final Session session)
			{
				final EvacuationEvent evacuationEvent = (EvacuationEvent) session
						.get(EvacuationEvent.class, id);
				if (evacuationEvent.getEstimated().before(new Date()))
					return;
				evacuationEvent.addUser(user);
				session.update(evacuationEvent);
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<EvacuationEvent> getAll()
	{
		List<EvacuationEvent> evacuationEvents = Lists.newArrayList();
		final Session session = getSession();
		evacuationEvents = session.createQuery("from EvacuationEvent").list();
		session.close();
		return evacuationEvents;
	}

	@Override
	public boolean removeUserToEvent(final User user, final long id)
	{
		return executeQuery(new QueryRunner()
		{

			@Override
			public void execueSafe(final Session session)
			{
				final EvacuationEvent evacuationEvent = (EvacuationEvent) session
						.get(EvacuationEvent.class, id);
				evacuationEvent.removeUser(user);
				session.update(evacuationEvent);
			}
		});
	}

	@Override
	public EvacuationEvent getClosest(final float lat, final float lng)
	{
		final List<EvacuationEvent> allEvents = getAll();
		Collections.sort(allEvents, new DistanceComperator(lat, lng));
		return allEvents.isEmpty() ? null : allEvents.get(0);
	}

	@Override
	public boolean delete(final long id)
	{

		return executeQuery(new QueryRunner()
		{
			@Override
			public void execueSafe(final Session session)
			{
				final EvacuationEvent event = getByID(id);
				for (final User u : event.getRegisteredUsers())
				{
					u.setEvent(null);
					session.update(u);
				}
				session.delete(event);
			}
		});
	}

	@Override
	protected Session getSession()
	{
		return sessionFactory.openSession();
	}

	@Override
	public void clear()
	{
		for (final EvacuationEvent e : getAll())
			delete(e.getId());

	}

	@Override
	public List<EvacuationEvent> searchEvents(final String q)
	{
		final List<EvacuationEvent> events = Lists.newArrayList();
		final Session session = sessionFactory.openSession();
		try
		{
			final FullTextSession fullTextSession = org.hibernate.search.Search
					.getFullTextSession(session);
			final Transaction tx = fullTextSession.beginTransaction();
			final QueryBuilder qb = fullTextSession.getSearchFactory()
					.buildQueryBuilder().forEntity(EvacuationEvent.class).get();
			final BooleanJunction<BooleanJunction> bool = qb.bool();
			if (q.contains(" "))
				for (final String keyword : q.split(" "))
					bool.must(qb.keyword().fuzzy().withThreshold(0.8f)
							.onFields("means", "address").matching(keyword)
							.createQuery());
			else
				bool.must(qb.keyword().fuzzy().withThreshold(0.8f)
						.onFields("means", "address").matching(q).createQuery());
			final org.hibernate.Query hibQuery = fullTextSession
					.createFullTextQuery(bool.createQuery(),
							EvacuationEvent.class);
			final List result = hibQuery.list();
			events.addAll(result);
			tx.commit();
		} catch (final HibernateException e)
		{
			e.printStackTrace();

		} catch (final Exception e)
		{
			e.printStackTrace();
		} finally
		{
			session.close();
		}
		return events;
	}

}
