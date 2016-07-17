package com.testtube.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import com.testtube.entidade.MetodoVideo;
import com.testtube.generics.ExtendsGenericDAO;

@Stateless
public class MetodoVideoDAO extends ExtendsGenericDAO<MetodoVideo> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MetodoVideoDAO() {
		super(MetodoVideo.class);
	}

	public List<MetodoVideo> buscarMetodosVideo(Long idMetodo) {

		try {
			StringBuffer jpql = new StringBuffer();
			jpql.append("SELECT entity FROM MetodoVideo entity WHERE entity.metodo.id = :pParam ");
			TypedQuery<MetodoVideo> q = getEntityManager().createQuery(jpql.toString(), MetodoVideo.class);
			q.setParameter("pParam", idMetodo);
			return q.getResultList();
		} catch (NoResultException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
