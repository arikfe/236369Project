package com.technion.project.dao;

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
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Lists;
import com.technion.project.model.Report;
import com.technion.project.model.User;

@Repository
public class ReportDAOImpl extends BaseDAO implements ReportDAO
{
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	DocumentDAO documentDao;

	/*
	 * (non-Javadoc)
	 *
	 * @see com.technion.project.dao.ReportDAO#getAllReports()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Report> getAllReports()
	{
		List<Report> reports = Lists.newArrayList();
		final Session session = sessionFactory.openSession();

		reports = session.createQuery("from Report").list();
		session.close();
		return reports;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.technion.project.dao.ReportDAO#getReportsForUser(com.technion.project
	 * .model.User)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Report> getReportsForUser(final User user)
	{
		final Session session = sessionFactory.openSession();
		final List<Report> reports = session
				.createQuery("from Report where username=?")
				.setParameter(0, user.getUsername()).list();
		session.close();
		return reports;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.technion.project.dao.ReportDAO#getReportByID(int)
	 */
	@Override
	public Report getReportByID(final Long id)
	{
		final Session session = sessionFactory.openSession();
		final Report report = (Report) session.get(Report.class, id);
		session.close();
		return report;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.technion.project.dao.ReportDAO#removeReport(int)
	 */
	@Override
	public void removeReport(final Long id)
	{
		executeQuery(new QueryRunner()
		{
			@Override
			public void execueSafe(final Session session)
			{
				final Report report = (Report) session.get(Report.class, id);
				session.delete(report);
			}
		});
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.technion.project.dao.ReportDAO#removeReport(com.technion.project.
	 * model.Report)
	 */
	@Override
	public void removeReport(final Report report)
	{
		executeQuery(new QueryRunner()
		{

			@Override
			public void execueSafe(final Session session)
			{
				session.delete(report);
			}
		});
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.technion.project.dao.ReportDAO#removeReport(com.technion.project.
	 * model.User)
	 */
	@Override
	public void removeReport(final User user, final Session session)
	{
		final List<Report> reports = getReportsForUser(user);
		for (final Report report : reports)
			session.delete(report);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.technion.project.dao.ReportDAO#addReport(com.technion.project.model
	 * .Report)
	 */
	@Override
	public void addReport(final Report report, final MultipartFile file)
	{
		executeQuery(new QueryRunner()
		{
			@Override
			public void execueSafe(final Session session)
			{
				if (file != null && !file.isEmpty())
					report.setImageId(documentDao.save(file));
				session.saveOrUpdate(report);
			}
		});
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.technion.project.dao.ReportDAO#delete(com.technion.project.model.
	 * User)
	 */

	@SuppressWarnings("unchecked")
	@Override
	public List<Report> getAllReports(final String condition)
	{
		if (condition.isEmpty())
			return getAllReports();
		final Session session = getSession();
		final List<Report> reports = session
				.createQuery(
						"from Report r where str(r.content) like :condition or lower(str(r.title)) like :condition ")
				.setParameter("condition", "%" + condition.toLowerCase() + "%")
				.list();
		session.close();
		return reports;
	}

	@Override
	protected Session getSession()
	{
		return sessionFactory.openSession();
	}

	@Override
	public void clear()
	{
		for (final Report r : getAllReports())
			removeReport(r);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Report> searchReports(final String q)
	{
		final List<Report> reports = Lists.newArrayList();
		final Session session = sessionFactory.openSession();
		try
		{
			final FullTextSession fullTextSession = org.hibernate.search.Search
					.getFullTextSession(session);
			final Transaction tx = fullTextSession.beginTransaction();
			final QueryBuilder qb = fullTextSession.getSearchFactory()
					.buildQueryBuilder().forEntity(Report.class).get();
			final BooleanJunction<BooleanJunction> bool = qb.bool();
			if (q.contains(" "))
				for (final String keyword : q.split(" "))
					bool.must(qb.keyword().fuzzy().withThreshold(0.8f)
							.onFields("title", "address").matching(keyword)
							.createQuery());
			else
				bool.must(qb.keyword().fuzzy().withThreshold(0.8f)
						.onFields("title", "address").matching(q).createQuery());
			// final Query query = qb.keyword().fuzzy().withThreshold(0.8f)
			// .onFields("title", "address").matching(q.replace('+', ' '))
			// .createQuery();
			final org.hibernate.Query hibQuery = fullTextSession
					.createFullTextQuery(bool.createQuery(), Report.class);
			final List<Report> result = hibQuery.list();
			reports.addAll(result);
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
		return reports;
	}
}
