package com.technion.ex5.Data;

import java.util.Date;

public class Report {
	private long id;
	private User user;
	private Shape shape;
	private String Title;
	private String content;
	private Date expireOn;
	private Picture pic;

	/**
	 * @return the id
	 */
	public synchronized final long getId() {
		return id;
	}

	/**
	 * @return the user
	 */
	public synchronized final User getUser() {
		return user;
	}

	/**
	 * @return the shape
	 */
	public synchronized final Shape getShape() {
		return shape;
	}

	/**
	 * @return the title
	 */
	public synchronized final String getTitle() {
		return Title;
	}

	/**
	 * @return the content
	 */
	public synchronized final String getContent() {
		return content;
	}

	/**
	 * @return the expireOn
	 */
	public synchronized final Date getExpireOn() {
		return expireOn;
	}

	/**
	 * @return the pic
	 */
	public synchronized final Picture getPic() {
		return pic;
	}

}
