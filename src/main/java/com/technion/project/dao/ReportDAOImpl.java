package com.technion.project.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.common.collect.Lists;
import com.technion.project.model.Report;
import com.technion.project.model.User;

@Repository
public class ReportDAOImpl implements ReportDAO
{
	@Autowired
	private SessionFactory sessionFactory;

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

		List<Report> reports = Lists.newArrayList();
		final Session session = sessionFactory.openSession();
		reports = session.createQuery("from Report where username=?")
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
		final Session session = sessionFactory.openSession();
		final Report report = (Report) session.get(Report.class, id);
		session.delete(report);
		session.close();
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
		final Session session = sessionFactory.openSession();
		session.delete(report);
		session.close();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.technion.project.dao.ReportDAO#removeReport(com.technion.project.
	 * model.User)
	 */
	@Override
	public void removeReport(final User user)
	{
		final List<Report> reports = getReportsForUser(user);
		for (final Report report : reports)
			removeReport(report);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.technion.project.dao.ReportDAO#addReport(com.technion.project.model
	 * .Report)
	 */
	@Override
	public void addReport(final Report report)
	{
		final Session session = sessionFactory.openSession();
		session.saveOrUpdate(report);
		session.flush();
		session.close();
	}
}
