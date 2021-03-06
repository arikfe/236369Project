package com.technion.project.dao;

import java.util.List;

import org.hibernate.Session;
import org.springframework.web.multipart.MultipartFile;

import com.technion.project.model.Document;

public interface DocumentDAO
{
	public long save(MultipartFile file);

	public Document get(long id);

	public List<Document> list();

	public void remove(Long id);

	public void remove(Long imageId, Session session);

	public Long save(byte[] byteArray, String contentType, String name);

	public void clear();
}
