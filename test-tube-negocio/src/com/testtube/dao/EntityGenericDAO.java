package com.testtube.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.testtube.entidade.IGenericEntity;
import com.testtube.entidade.SimpleValidate;
import com.testtube.generics.ExtendsGenericDAO;

@Stateless
public class EntityGenericDAO extends ExtendsGenericDAO<IGenericEntity> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EntityGenericDAO() {
		super(IGenericEntity.class);
	}

	public IGenericEntity findObject(IGenericEntity entity, Long id) {
		try {
			Query q = getEntityManager().createQuery("SELECT entity FROM " + entity.getClass().getName() + " entity  WHERE entity.id = " + id);
			return (IGenericEntity) q.getSingleResult();
		} catch (NoResultException noResultException) {
			return null;
		}

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<IGenericEntity> findAll(Class clazz) {
		try {
			Query q = getEntityManager().createQuery("SELECT entity FROM " + clazz.getName() + " entity  ");
			return q.getResultList();
		} catch (NoResultException noResultException) {
			return null;
		}

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<IGenericEntity> carregarPesquisaLazy(int startingAt, int maxPerPage, String fieldOrder, String order, Map<String, Object> parametros, Class entity) {

		String jpql = "SELECT entity FROM " + entity.getName() + " entity where entity.id > 0";

		// Filtros
		Set<String> listParametros = parametros.keySet();

		int indice = 0;
		for (Iterator<String> parametro = listParametros.iterator(); parametro.hasNext();) {
			String campo = parametro.next();
			String args = "args_" + indice;
			Object valueArgs = parametros.get(campo);
			if (valueArgs != null) {
				if (valueArgs.getClass().equals(String.class)) {
					jpql += " AND entity." + campo + " LIKE :" + args;
					indice++;
				} else if (valueArgs.getClass().equals(java.util.ArrayList.class)) {
					if (valueArgs.getClass().equals(java.util.ArrayList.class)) {
						List lista = (List) valueArgs;
						if (!lista.isEmpty()) {
							jpql += " AND entity." + campo + " IN :" + args;
							indice++;
						}
					}
				} else {
					jpql += " AND entity." + campo + " = :" + args;
					indice++;
				}

			}

		}

		jpql = orderParametroEntityHQL(fieldOrder, order, jpql);

		Query q = getEntityManager().createQuery(jpql);

		q.setFirstResult(startingAt);
		q.setMaxResults(maxPerPage);

		indice = 0;
		for (Iterator<String> parametro = listParametros.iterator(); parametro.hasNext();) {
			String campo = parametro.next();
			String args = "args_" + indice;
			Object valueArgs = parametros.get(campo);
			if (valueArgs != null) {
				if (valueArgs.getClass().equals(String.class)) {
					q.setParameter(args, "%" + valueArgs.toString() + "%");
					indice++;
				} else if (valueArgs.getClass().equals(java.util.ArrayList.class)) {
					List lista = (List) valueArgs;
					if (!lista.isEmpty()) {
						q.setParameter(args, valueArgs);
						indice++;
					}
				} else {
					q.setParameter(args, valueArgs);
					indice++;
				}

			}
		}

		List<IGenericEntity> entities = q.getResultList();
		if (SimpleValidate.isNullOrEmpty(entities))

		{
			return new ArrayList<IGenericEntity>();
		}
		return entities;

	}

	@SuppressWarnings("rawtypes")
	public Long carregarPesquisaLazySize(String fieldOrder, String order, Map<String, Object> parametros, Class entity) {
		try {
			String jpql = "SELECT count(*) FROM " + entity.getName() + " entity where entity.id > 0";

			// Filtros
			Set<String> listParametros = parametros.keySet();

			int indice = 0;
			for (Iterator<String> parametro = listParametros.iterator(); parametro.hasNext();) {
				String campo = parametro.next();
				String args = "args_" + indice;
				Object valueArgs = parametros.get(campo);
				if (valueArgs != null) {
					if (valueArgs.getClass().equals(String.class)) {
						jpql += " AND entity." + campo + " LIKE :" + args;
						indice++;
					} else if (valueArgs.getClass().equals(java.util.ArrayList.class)) {
						List lista = (List) valueArgs;
						if (!lista.isEmpty()) {
							jpql += " AND entity." + campo + " IN :" + args;
							indice++;
						}
					} else {
						jpql += " AND entity." + campo + " = :" + args;
						indice++;
					}
				}
			}

			// jpql = orderParametroHQL(fieldOrder, order, jpql);

			Query q = getEntityManager().createQuery(jpql);

			indice = 0;
			for (Iterator<String> parametro = listParametros.iterator(); parametro.hasNext();) {
				String campo = parametro.next();
				String args = "args_" + indice;
				Object valueArgs = parametros.get(campo);
				if (valueArgs != null) {
					if (valueArgs.getClass().equals(String.class)) {
						q.setParameter(args, "%" + valueArgs.toString() + "%");
						indice++;
					} else if (valueArgs.getClass().equals(java.util.ArrayList.class)) {
						List lista = (List) valueArgs;
						if (!lista.isEmpty()) {
							q.setParameter(args, valueArgs);
							indice++;
						}
					} else {
						q.setParameter(args, valueArgs);
						indice++;
					}
				}
			}

			Long size = (Long) q.getSingleResult();

			return size;

		} catch (NoResultException e) {
			return 0l;
		} catch (Exception e) {
			return 0l;
		}

	}

	/*
	 * private static String orderParametroHQL(String fieldOrder, String order,
	 * String jpql) { if (!fieldOrder.isEmpty()) jpql += " ORDER BY " +
	 * fieldOrder + " " + order;
	 * 
	 * return jpql; }
	 */
	private static String orderParametroEntityHQL(String fieldOrder, String order, String jpql) {
		if (!fieldOrder.isEmpty()) {
			jpql += " ORDER BY " + fieldOrder + " " + order;
		} else {
			jpql += " ORDER BY entity.id desc";
		}
		return jpql;
	}

}
