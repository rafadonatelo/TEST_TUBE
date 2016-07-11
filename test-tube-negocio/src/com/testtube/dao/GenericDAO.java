package com.testtube.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.sql.DataSource;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;

/**
 * Classe DAO generica para acesso aos dados.
 * 
 */
public abstract class GenericDAO<T> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected EntityManager entityManager;

	protected DataSource dataSource;

	private Class<T> persistentClass;

	public GenericDAO(Class<T> persistenceClass) {
		this.persistentClass = persistenceClass;
	}

	public Class<T> getPersistentClass() {
		return this.persistentClass;
	}

	public void excluir(T entity) {
		entity = getEntityManager().merge(entity);
		getEntityManager().remove(entity);
	}

	public T buscarPorId(Long id) {
		try {
			return (T) getEntityManager().find(getPersistentClass(), id);
		} catch (NoResultException noResultException) {
			return null;
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public T buscarPorId(Long id, Class clazz) {
		try {
			return (T) getEntityManager().find(clazz, id);
		} catch (NoResultException noResultException) {
			return null;
		}
	}

	/**
	 * Recupera o objeto dando join em todos os relacionamentos
	 */
	@SuppressWarnings("unchecked")
	public T buscarUmPorCampoCompleto(String campo, Object valor) {
		try {
			return (T) getEntityManager().createQuery("SELECT obj FROM " + persistentClass.getName() + " obj " + "FETCH ALL PROPERTIES " + "WHERE " + campo + " = :_valor ")
					.setParameter("_valor", valor).setMaxResults(1).getSingleResult();
		} catch (NoResultException noResultException) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<T> buscarTodos() {
		return getSession().createCriteria(persistentClass).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
	}

	public T salvar(T entity) {
		entity = getEntityManager().merge(entity);
		return entity;

	}

	@SuppressWarnings("unchecked")
	public List<T> buscarListaPorCriterio(Criterion... criterion) {
		Criteria crit = getSession().createCriteria(getPersistentClass());
		for (Criterion c : criterion) {
			crit.add(c);
		}
		crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return crit.list();
	}

	@SuppressWarnings("unchecked")
	public List<T> buscarListaPorCriterio(Order order, Criterion... criterion) {
		Criteria crit = getSession().createCriteria(getPersistentClass());
		for (Criterion c : criterion) {
			crit.add(c);
		}

		crit.addOrder(order);
		crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		return crit.list();
	}

	@SuppressWarnings("unchecked")
	public T buscarUmPorCriterio(Criterion... criterion) {
		Criteria crit = getSession().createCriteria(getPersistentClass());
		for (Criterion c : criterion) {
			crit.add(c);
		}
		return (T) crit.uniqueResult();
	}

	public Long count() {
		Query query = entityManager.createQuery("SELECT COUNT(entity) " + "FROM " + getPersistentClass().getName() + " entity ");
		return (Long) query.getSingleResult();
	}

	/**
	 * Retorna uma Session do hibernate retirada do EntityManager do JPA
	 */
	public Session getSession() {
		return (Session) getEntityManager().unwrap(Session.class);
	}

	/**
	 * Retorna a conexao JDBC com o Postgres
	 */
	public Connection getConnection() {
		try {
			return dataSource.getConnection();
		} catch (SQLException ex) {
			return null;
		}
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setPersistentClass(Class<T> persistentClass) {
		this.persistentClass = persistentClass;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

}