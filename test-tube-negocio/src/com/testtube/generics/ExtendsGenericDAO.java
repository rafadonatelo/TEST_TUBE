package com.testtube.generics;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import com.testtube.dao.GenericDAO;

public abstract class ExtendsGenericDAO<T> extends GenericDAO<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ExtendsGenericDAO(Class<T> persistenceClass) {
		super(persistenceClass);
	}

	@PersistenceContext(unitName = "testtubePU")
	@Override
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	@Resource(mappedName = "jdbc/poolTestTube")
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
}