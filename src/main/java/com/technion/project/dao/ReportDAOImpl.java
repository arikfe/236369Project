package com.technion.project.dao;

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
public class ReportDAOImpl implements ReportDAO
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
		session.flush();
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
		session.flush();
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
		final Session session = sessionFactory.openSession();
		if (!file.isEmpty())
			report.setImageId(documentDao.save(file));
		session.saveOrUpdate(report);
		session.flush();
		session.close();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.technion.project.dao.ReportDAO#delete(com.technion.project.model.
	 * User)
	 */
	@Override
	public void delete(final User user)
	{
		final Session session = sessionFactory.getCurrentSession();
		for (final Report report : getReportsForUser(user))
			session.delete(report);

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Report> getAllReports(final String condition)
	{
		if (condition.isEmpty())
			return getAllReports();
		List<Report> reports = Lists.newArrayList();
		final Session session = sessionFactory.openSession();
		reports = session
				.createQuery(
						"from Report r where str(r.content) like :condition or lower(str(r.title)) like :condition ")
				.setParameter("condition", "%" + condition.toLowerCase() + "%")
				.list();
		session.close();
		return reports;
	}
}
