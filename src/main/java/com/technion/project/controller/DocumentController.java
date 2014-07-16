package com.technion.project.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.technion.project.dao.DocumentDAO;
import com.technion.project.model.Document;

@Controller
@RequestMapping("/download/*")
public class DocumentController
{
	@Autowired
	private DocumentDAO documentDAO;

	@RequestMapping(value =
	{ "{id}" }, method = RequestMethod.GET)
	public String download(@PathVariable("id") final long id,
			final HttpServletResponse response)
	{

		final Document doc = documentDAO.get(id);
		try
		{
			response.setHeader("Content-Disposition", "inline;filename=\""
					+ doc.getName() + "\"");
			response.setContentType(doc.getContentType());
			final OutputStream out = response.getOutputStream();
			response.setContentType(doc.getContentType());
			IOUtils.copy(doc.getFile().getBinaryStream(), out);
			out.flush();
			out.close();
		} catch (final IOException e)
		{
			e.printStackTrace();
		} catch (final SQLException e)
		{
			e.printStackTrace();
		}
		return null;
	}
}
