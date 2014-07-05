package com.technion.project.dao;

import java.util.List;

import com.technion.project.model.Report;
import com.technion.project.model.User;

public interface ReportDAO
{

	public List<Report> getAllReports();

	public List<Report> getReportsForUser(User user);

	public Report getReportByID(int id);

	public void removeReport(int id);

	public void removeReport(Report report);

	public void removeReport(User user);

	public void addReport(Report report);

}