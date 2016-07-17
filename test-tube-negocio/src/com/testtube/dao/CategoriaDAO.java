package com.testtube.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.testtube.entidade.AssinaturaCategoria;
import com.testtube.entidade.Categoria;
import com.testtube.generics.ExtendsGenericDAO;

@Stateless
public class CategoriaDAO extends ExtendsGenericDAO<Categoria> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CategoriaDAO() {
		super(Categoria.class);
	}

	public List<Categoria> buscarCategoriaPorNome(String nome) {

		try {
			StringBuffer jpql = new StringBuffer();
			jpql.append("SELECT entity FROM Categoria entity WHERE entity.nome LIKE :pParam ");
			TypedQuery<Categoria> q = getEntityManager().createQuery(jpql.toString(), Categoria.class);
			q.setParameter("pParam", "%" + nome + "%");
			q.setMaxResults(10);
			List<Categoria> list = q.getResultList();
			return list;
		} catch (NoResultException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void limparDadosDeAssinaturaCategoria(Long idAss) {
		try {
			Query q = getEntityManager().createQuery("DELETE FROM AssinaturaCategoria m where m.assinatura.id = :id");
			q.setParameter("id", idAss);
			q.executeUpdate();
		} catch (Exception e) {

		}
	}

	public List<AssinaturaCategoria> buscarAssinaturaCategoria(Long idAss) {

		try {
			StringBuffer jpql = new StringBuffer();
			jpql.append("SELECT entity FROM AssinaturaCategoria entity WHERE entity.assinatura.id = :pParam ");
			TypedQuery<AssinaturaCategoria> q = getEntityManager().createQuery(jpql.toString(), AssinaturaCategoria.class);
			q.setParameter("pParam", idAss);
			return q.getResultList();
		} catch (NoResultException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
