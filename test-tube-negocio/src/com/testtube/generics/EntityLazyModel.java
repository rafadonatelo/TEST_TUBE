package com.testtube.generics;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.testtube.entidade.IGenericEntity;

public class EntityLazyModel extends LazyDataModel<IGenericEntity> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("rawtypes")
	private Class entityClass;

	private Map<String, Object> parametros;

	private List<IGenericEntity> listEntity;

	private Object[] argumentos;

	protected ServiceAdapter service;

	@SuppressWarnings("rawtypes")
	public EntityLazyModel(Class entityClass, ServiceAdapter service, Object... argumentos) {
		this.entityClass = entityClass;
		this.service = service;
		this.argumentos = argumentos;

	}

	@SuppressWarnings("unchecked")
	@Override
	public IGenericEntity getRowData(String rowKey) {
		List<IGenericEntity> entities = (List<IGenericEntity>) getWrappedData();

		for (IGenericEntity entity : entities) {
			if (entity.getId().equals(rowKey))
				return entity;
		}
		return null;
	}

	@Override
	public Object getRowKey(IGenericEntity object) {

		return object.getId();
	}

	@Override
	public List<IGenericEntity> load(int startingAt, int maxPerPage, String sortField, SortOrder sortOrder, Map<String, Object> filters) {

		String orderField = "", order = "";
		parametros = new HashMap<String, Object>();
		setListEntity(new ArrayList<IGenericEntity>());

		for (Iterator<String> parametro = filters.keySet().iterator(); parametro.hasNext();) {
			String campo = parametro.next();
			parametros.put(campo, filters.get(campo));
		}

		for (int i = 0; i < argumentos.length; i = i + 2) {
			if (i + 1 == argumentos.length) {
				continue;
			}
			String campo = argumentos[i].toString();
			Object valorArgumento = argumentos[i + 1];
			parametros.put(campo, valorArgumento);
		}

		if (sortField != null)
			orderField = sortField;
		if (sortOrder != null) {
			order = sortOrder.toString().equals("ASCENDING") ? "ASC" : "DESC";
		}

		setListEntity(service.carregarPesquisaLazy(startingAt, maxPerPage, orderField, order, parametros, entityClass));
		Long total = service.carregarPesquisaLazySize(orderField, order, parametros, entityClass);

		int dataSize = 0;
		setRowCount(0);
		if (!com.testtube.entidade.SimpleValidate.isNullOrEmpty(getListEntity())) {
			setRowCount(total.intValue());
			dataSize = total.intValue();
		}

		if (dataSize > maxPerPage)
			this.setPageSize(startingAt + (dataSize % maxPerPage));
		else
			this.setPageSize(maxPerPage);

		return getListEntity();
	}

	public List<IGenericEntity> getListEntity() {
		return listEntity;
	}

	public void setListEntity(List<IGenericEntity> listEntity) {
		this.listEntity = listEntity;
	}

	public Object[] getArgumentos() {
		return argumentos;
	}

	public void setArgumentos(Object[] argumentos) {
		this.argumentos = argumentos;
	}

}
