package com.testtube.service;

import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.testtube.audit.AuditavelFolha;
import com.testtube.bo.EntityGenericBO;
import com.testtube.entidade.IGenericEntity;
import com.testtube.generics.ServiceAdapter;

@Stateless
public class SegurancaService extends ServiceAdapter {

	@Inject
	private EntityGenericBO entityGenericBO;

	@SuppressWarnings("rawtypes")
	public IGenericEntity buscarPorID(Class clazz, Long id) {
		return entityGenericBO.buscarPorId(id, clazz);
	}

	@AuditavelFolha
	public IGenericEntity salvar(IGenericEntity entity) {
		return entityGenericBO.salvar(entity);
	}

	public IGenericEntity salvarSemAuditoria(IGenericEntity entity) {
		return entityGenericBO.salvar(entity);
	}

	@SuppressWarnings("rawtypes")
	public List<IGenericEntity> carregarPesquisaLazy(int startingAt, int maxPerPage, String fieldOrder, String order, Map<String, Object> parametros, Class entity) {
		return entityGenericBO.carregarPesquisaLazy(startingAt, maxPerPage, fieldOrder, order, parametros, entity);
	}

	public void removerEntity(Long id) {
		entityGenericBO.remover(id);
	}

	public void removerEntity(IGenericEntity entity) {
		entityGenericBO.remover(entity);
	}

	@SuppressWarnings("rawtypes")
	public List<IGenericEntity> findAll(Class entity) {
		return entityGenericBO.findAll(entity);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Long carregarPesquisaLazySize(String fieldOrder, String order, Map<String, Object> parametros, Class entity) {
		return entityGenericBO.carregarPesquisaLazySize(fieldOrder, order, parametros, entity);
	}
}
