package com.testtube.bo;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;

import com.testtube.dao.EntityGenericDAO;
import com.testtube.entidade.IGenericEntity;
@SuppressWarnings("rawtypes")
@Stateless
public class EntityGenericBO extends GenericBO<IGenericEntity> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private EntityGenericDAO dao;

	@Override
	@PostConstruct
	protected void inicializar() {
		this.genericDAO = dao;
	}

	public IGenericEntity findObject(IGenericEntity entity, Long id) {
		return dao.findObject(entity, id);
	}

	
	public List<IGenericEntity> carregarPesquisaLazy(int startingAt, int maxPerPage, String fieldOrder, String order, Map<String, Object> parametros, Class entity) {
		return dao.carregarPesquisaLazy(startingAt, maxPerPage, fieldOrder, order, parametros, entity);
	}

	public Long carregarPesquisaLazySize(String fieldOrder, String order, Map<String, Object> parametros, Class entity) {
		return dao.carregarPesquisaLazySize(fieldOrder, order, parametros, entity);
	}

	public List<IGenericEntity> findAll(Class entity) {
		return dao.findAll(entity);
	}

}
