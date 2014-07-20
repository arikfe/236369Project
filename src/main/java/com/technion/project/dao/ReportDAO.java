package com.technion.project.dao;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.technion.project.model.Report;
import com.technion.project.model.User;

public interface ReportDAO
{

	public List<Report> getAllReports();

	public List<Report> getAllReports(String condition);

	public List<Report> getReportsForUser(User user);

	public Report getReportByID(Long id);

	public void removeReport(Long id);

	public void removeReport(Report report);

	public void removeReport(User user);

	public void addReport(Report report, MultipartFile file);

	/**
	 * use only in created Session - session is committed
	 *
	 * @param user
	 */
	public void delete(User user);

}