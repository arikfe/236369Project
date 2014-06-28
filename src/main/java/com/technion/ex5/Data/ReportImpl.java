package com.technion.ex5.Data;

import java.util.Date;

/**
 * @author Arik
 *
 */
public class ReportImpl implements Report
{
	public ReportImpl(long id)
	{
	}

	private long id;
	private User user;
	private Shape shape;
	private String Title;
	private String content;
	private Date expireOn;
	private Picture pic;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.technion.ex5.Data.Report#getUser()
	 */
	public synchronized final User getUser()
	{
		return user;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.technion.ex5.Data.Report#getShape()
	 */
	public synchronized final Shape getShape()
	{
		return shape;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.technion.ex5.Data.Report#getTitle()
	 */
	public synchronized final String getTitle()
	{
		return Title;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.technion.ex5.Data.Report#getContent()
	 */
	public synchronized final String getContent()
	{
		return content;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.technion.ex5.Data.Report#getExpireOn()
	 */
	public synchronized final Date getExpireOn()
	{
		return expireOn;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.technion.ex5.Data.Report#getPic()
	 */
	public synchronized final Picture getPic()
	{
		return pic;
	}

	public long getId()
	{
		return id;
	}

}
