package com.technion.project.dao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
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

		List<Report> reports = Lists.newArrayList();
		final Session session = sessionFactory.openSession();

		reports = session.createQuery("from Report where username=?")
				.setParameter(0, user.getUsername()).list();
		final ArrayList<Report> reportsDis = new ArrayList(new HashSet(reports));
		session.close();
		return reportsDis;
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
		List<Report> reports = Lists.newArrayList();
		final Session session = getSession();
		reports = session
				.createQuery(
						"from Report r where str(r.content) like :condition or lower(str(r.title)) like :condition ")
				.setParameter("condition", "%" + condition.toLowerCase() + "%")
				.list();
		final ArrayList<Report> reportsDis = new ArrayList(new HashSet(reports));
		session.close();
		return reportsDis;
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
}
