package com.technion.project.dao;

import org.hibernate.Session;

public interface QueryRunner
{
	void execueSafe(Session session);
}
