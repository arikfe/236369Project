package com.technion.ex5.Data;

import java.util.Date;

public interface Report
{

	/**
	 * @return the user
	 */
	public User getUser();

	/**
	 * @return the shape
	 */
	public Shape getShape();

	/**
	 * @return the title
	 */
	public String getTitle();

	/**
	 * @return the content
	 */
	public String getContent();

	/**
	 * @return the expireOn
	 */
	public Date getExpireOn();

	/**
	 * @return the pic
	 */
	public Picture getPic();

}