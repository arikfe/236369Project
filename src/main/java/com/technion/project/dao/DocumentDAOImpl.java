package com.technion.project.dao;

import java.io.IOException;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Lists;
import com.google.common.io.ByteStreams;
import com.technion.project.model.BaseModel;
import com.technion.project.model.Document;

@Repository
public class DocumentDAOImpl implements DocumentDAO
{
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public long save(final MultipartFile file)
	{
		final Session session = sessionFactory.openSession();
		final Transaction transaction = session.getTransaction();
		final Document document = new Document();
		document.setContentType(file.getContentType());
		document.setName(file.getOriginalFilename());
		try
		{
			document.setFile(Hibernate.getLobCreator(session).createBlob(
					ByteStreams.toByteArray(file.getInputStream())));
		} catch (final IOException e)
		{
			throw new RuntimeException("File was not recied properly");
		}
		transaction.begin();
		session.saveOrUpdate(document);
		transaction.commit();
		session.close();
		return document.getId();
	}

	@Override
	public Long save(final byte[] byteArray, final String contentType,
			final String name)
	{
		final Session session = sessionFactory.openSession();
		final Transaction transaction = session.getTransaction();
		final Document document = new Document();
		document.setContentType(contentType);
		document.setName(name);
		document.setFile(Hibernate.getLobCreator(session).createBlob(byteArray));
		transaction.begin();
		session.saveOrUpdate(document);
		transaction.commit();
		session.close();
		return document.getId();

	}

	@Override
	public Document get(final long id)
	{
		final Session session = sessionFactory.openSession();
		final Document document = (Document) session.get(Document.class, id);
		session.close();
		return document;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Document> list()
	{
		List<Document> documents = Lists.newArrayList();
		final Session session = sessionFactory.openSession();
		documents = session.createQuery("from document").list();
		session.close();
		return documents;

	}

	@Override
	public void remove(final Long id)
	{
		if (id == null)
			return;
		final BaseModel document = get(id);
		final Session session = sessionFactory.openSession();
		session.delete(document);
		session.flush();
		session.close();

	}

	@Override
	public void remove(final Long imageId, final Session session)
	{
		session.delete(get(imageId));

	}

}
