package com.technion.project.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.technion.project.Constants;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("report")
@Entity
@Indexed
@Table(name = "report", catalog = Constants.SCHEMA)
public class Report implements BaseModel
{
	@Id
	@GeneratedValue
	private long id;
	private String username;
	@Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
	private String title;
	@Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
	private String address;
	@Field(index = Index.YES, analyze = Analyze.YES, store = Store.NO)
	private String content;
	@DateTimeFormat(iso = ISO.DATE, pattern = "MM/dd/yyyy HH:mm")
	private Date expiration;
	private float geolat;

	public String getAddress()
	{
		return address;
	}

	public void setAddress(final String address)
	{
		this.address = address;
	}

	private float geolng;
	private Long imageId;

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

	public Long getImageId()
	{
		return imageId;
	}

	public void setImageId(final Long imageId)
	{
		this.imageId = imageId;
	}

	@Transient
	public float[] getCordinates()
	{
		final float[] coordinates = new float[2];
		coordinates[0] = geolat;
		coordinates[1] = geolng;
		return coordinates;
	}
}
