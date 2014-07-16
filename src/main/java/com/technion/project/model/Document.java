package com.technion.project.model;

import java.sql.Blob;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import com.technion.project.Constants;

@Entity
@Table(name = "document", catalog = Constants.SCHEMA)
public class Document
{
	@Id
	@GeneratedValue
	private long id;
	private String contentType;
	@Lob
	private Blob file;
	private String filename;

	public long getId()
	{
		return id;
	}

	public void setId(final long id)
	{
		this.id = id;
	}

	public String getContentType()
	{
		return contentType;
	}

	public void setContentType(final String contentType)
	{
		this.contentType = contentType;
	}

	public Blob getFile()
	{
		return file;
	}

	public void setFile(final Blob file)
	{
		this.file = file;
	}

	public String getName()
	{
		return filename;
	}

	public void setName(final String name)
	{
		this.filename = name;
	}

}
