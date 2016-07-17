package com.testtube.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.testtube.entidade.Material;
import com.testtube.entidade.MetodoMaterial;
import com.testtube.generics.ExtendsGenericDAO;

@Stateless
public class MaterialDAO extends ExtendsGenericDAO<Material> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MaterialDAO() {
		super(Material.class);
	}

	public List<Material> buscarMaterialPorNome(String nome) {

		try {
			StringBuffer jpql = new StringBuffer();
			jpql.append("SELECT entity FROM Material entity WHERE entity.nome LIKE :pParam ");
			TypedQuery<Material> q = getEntityManager().createQuery(jpql.toString(), Material.class);
			q.setParameter("pParam", "%" + nome + "%");
			q.setMaxResults(10);
			List<Material> list = q.getResultList();
			return list;
		} catch (NoResultException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void limparDadosDeMaterial(Long idMetodo) {
		try {
			Query q = getEntityManager().createQuery("DELETE FROM MetodoMaterial m where m.metodo.id = :id");
			q.setParameter("id", idMetodo);
			q.executeUpdate();
		} catch (Exception e) {

		}
	}

	public List<MetodoMaterial> buscarMetodosMateriais(Long idMetodo) {

		try {
			StringBuffer jpql = new StringBuffer();
			jpql.append("SELECT entity FROM MetodoMaterial entity WHERE entity.metodo.id = :pParam ");
			TypedQuery<MetodoMaterial> q = getEntityManager().createQuery(jpql.toString(), MetodoMaterial.class);
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
