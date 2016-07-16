package com.testtube.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.sql.DataSource;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;

import com.testtube.audit.Auditavel;
import com.testtube.entidade.IGenericEntity;

/**
 * Classe DAO generica para acesso aos dados.
 * @author codigosfontes.com.br
 */

public abstract class GenericDAO<T> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected EntityManager entityManager;

	protected DataSource dataSource;

	protected Class<T> persistentClass;

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

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public boolean remove(T obj) {
		try {
			obj = getEntityManager().merge(obj);
			getEntityManager().remove(obj);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
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

	public Long count() {
		try {
			TypedQuery<Long> query = getEntityManager().createQuery("SELECT COUNT(*) FROM " + persistentClass.getName(), Long.class);
			return (Long) query.getSingleResult();
		} catch (NoResultException noResultException) {
			return 0L;
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

	public void detach(IGenericEntity entity) {
		getSession().evict(entity);
	}

	@SuppressWarnings("unchecked")
	public T buscarUmPorCriterio(Criterion... criterion) {
		Criteria crit = getSession().createCriteria(getPersistentClass());
		for (Criterion c : criterion) {
			crit.add(c);
		}
		return (T) crit.uniqueResult();
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
			return getDataSource().getConnection();
		} catch (SQLException ex) {
			return null;
		}
	}

	public EntityManager getEntityManager() {
		if (entityManager == null)
			throw new IllegalStateException("EntityManager não pode setar o DAO antes de ser usado.");
		return entityManager;
	}

	@Auditavel
	public T inserir(T entity) {
		return getEntityManager().merge(entity);
	}

	@Auditavel
	public T atualizar(T entity) {
		return getEntityManager().merge(entity);
	}

	public void remover(Long PK) {
		excluir(buscarPorId(PK));
	}

	protected DataSource getDataSource() {
		return dataSource;
	}

	protected void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

}