package com.technion.project.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.technion.project.Constants;

@Entity
@Table(name = "report", catalog = Constants.SCHEMA)
public class Report implements BaseModel
{
	@Id
	@GeneratedValue
	private long id;
	private String username;
	private String title;
	private String content;
	private Date expiration;
	private float geolat;
	private float geolng;
	private Long imageId;

	public Long getImageId()
	{
		return imageId;
	}

	public void setImageId(final Long imageId)
	{
		this.imageId = imageId;
	}

	public float getGeolat()
	{
		return geolat;
	}

	public void setGeolat(final float geolat)
	{
		this.geolat = geolat;
	}

	public float getGeolng()
	{
		return geolng;
	}

	public void setGeolng(final float geolng)
	{
		this.geolng = geolng;
	}

	public long getId()
	{
		return id;
	}

	public void setId(final long id)
	{
		this.id = id;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(final String username)
	{
		this.username = username;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(final String title)
	{
		this.title = title;
	}

	public String getContent()
	{
		return content;
	}

	public void setContent(final String content)
	{
		this.content = content;
	}

	public Date getExpiration()
	{
		return expiration;
	}

	public void setExpiration(final Date expiration)
	{
		this.expiration = expiration;
	}
}
