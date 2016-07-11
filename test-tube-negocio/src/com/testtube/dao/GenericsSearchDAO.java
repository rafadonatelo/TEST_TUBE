package com.testtube.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.Stateless;
import javax.persistence.Query;

import com.testtube.entidade.IGenericEntity;
import com.testtube.entidade.SimpleValidate;

@Stateless
public class GenericsSearchDAO extends GenericDAO<IGenericEntity> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public GenericsSearchDAO() {
		super(IGenericEntity.class);

	}

	@SuppressWarnings({ "unchecked" })
	public List<IGenericEntity> carregarPesquisaLazy(int startingAt, int maxPerPage, String fieldOrder, String order, Map<String, Object> parametros, IGenericEntity entity) {

		String jpql = "SELECT entity FROM " + entity.getClass().getName() + " entity where entity.id > 0";

		// Filtros
		Set<String> listParametros = parametros.keySet();

		int indice = 0;
		for (Iterator<String> parametro = listParametros.iterator(); parametro.hasNext();) {
			String campo = parametro.next();
			String args = "args_" + indice;
			Object valueArgs = parametros.get(campo);
			if (valueArgs.getClass().equals(String.class)) {
				jpql += " AND entity." + campo + " LIKE :" + args;
			} else {
				jpql += " AND entity." + campo + " = :" + args;
			}

			indice++;
		}

		jpql = orderParametroHQL(fieldOrder, order, jpql);

		Query q = getEntityManager().createQuery(jpql);

		q.setFirstResult(startingAt);
		q.setMaxResults(maxPerPage);

		indice = 0;
		for (Iterator<String> parametro = listParametros.iterator(); parametro.hasNext();) {
			String campo = parametro.next();
			String args = "args_" + indice;
			Object valueArgs = parametros.get(campo);
			if (valueArgs.getClass().equals(String.class)) {
				q.setParameter(args, "%" + valueArgs.toString() + "%");
			} else {
				q.setParameter(args, valueArgs);
			}
			indice++;
		}

		List<IGenericEntity> entities = q.getResultList();
		if (SimpleValidate.isNullOrEmpty(entities)) {
			return new ArrayList<IGenericEntity>();
		}
		return entities;

	}

	public int countPesquisaLazy(Map<String, Object> parametros, IGenericEntity entity) {

		String jpql = "SELECT count(entity) FROM " + entity.getClass().getName() + " entity where entity.id > 0";

		// Filtros
		Set<String> listParametros = parametros.keySet();

		int indice = 0;
		for (Iterator<String> parametro = listParametros.iterator(); parametro.hasNext();) {
			String campo = parametro.next();
			String args = "args_" + indice;
			Object valueArgs = parametros.get(campo);
			if (valueArgs.getClass().equals(String.class)) {
				jpql += " AND entity." + campo + " LIKE :" + args;
			} else {
				jpql += " AND entity." + campo + " = :" + args;
			}

			indice++;
		}

		Query q = getEntityManager().createQuery(jpql);

		indice = 0;
		for (Iterator<String> parametro = listParametros.iterator(); parametro.hasNext();) {
			String campo = parametro.next();
			String args = "args_" + indice;
			Object valueArgs = parametros.get(campo);
			if (valueArgs.getClass().equals(String.class)) {
				q.setParameter(args, "%" + valueArgs.toString() + "%");
			} else {
				q.setParameter(args, valueArgs);
			}
			indice++;
		}

		return Long.valueOf(q.getSingleResult().toString()).intValue();
	}

	private static String orderParametroHQL(String fieldOrder, String order, String jpql) {
		if (!fieldOrder.isEmpty())
			jpql += " ORDER BY " + fieldOrder + " " + order;
		return jpql;
	}

}
