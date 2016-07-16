package com.testtube.generics;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;

import com.testtube.entidade.IGenericEntity;
import com.testtube.util.FacesUtil;

public abstract class AbstractComplexGenericBean<ENTITY extends IGenericEntity> implements Serializable {

	private static final long serialVersionUID = 1L;

	protected ENTITY entity;

	/** InstÃ¢ncia da classe Class da entity **/
	protected Class<ENTITY> entityClass;

	/**
	 * Datamodel que contem o resultado da pesquisa, ou seja, a listagem de
	 * entidades
	 **/
	private EntityLazyModel dataModel;

	private Map<String, String> parametrosMapa;

	private boolean editarRegistro;

	private boolean novoRegisto;

	protected abstract ServiceAdapter service();

	@SuppressWarnings("unchecked")
	public void open() {
		novoRegisto = false;
		editarRegistro = false;
		entityClass = (Class<ENTITY>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		search();

	}

	public void search() {
		try {
			entity = entityClass.newInstance();
			dataModel = new EntityLazyModel(entity.getClass(), service());

		} catch (InstantiationException e) {
			System.err.printf("Ocorreu uma excecao na criacao da instancia da classe Entity.", e.getMessage());

		} catch (IllegalAccessException e) {
			System.err.printf("Ocorreu uma excecao na criacao da instancia da classe Entity.", e);
		}

	}

	@SuppressWarnings("unchecked")
	public String save() {

		try {
			beforeSave();
			entity = (ENTITY) service().salvar(entity);
			afterSave();
			open();
			FacesUtil.addInfoMessage("Salvo com sucesso!");
			return back();

		} catch (Exception e) {
			FacesUtil.addErrorMessage(e.getCause().getMessage());
		}
		return "";
	}

	@SuppressWarnings("unchecked")
	public void remove() {
		try {

			entity = (ENTITY) getDataModel().getRowData();
			beforeRemove();
			service().removerEntity(entity);
			afterRemove();
			open();
			FacesUtil.addInfoMessage("Excluído com sucesso!");
		} catch (Exception e) {
			FacesUtil.addErrorMessage(e.getCause().getMessage());
		}

	}

	public EntityLazyModel getDataModel() {

		return dataModel;
	}

	public void setDataModel(EntityLazyModel dataModel) {
		this.dataModel = dataModel;
	}

	public void cancel() {
		open();
	}

	public ENTITY getEntity() {
		return entity;
	}

	public void setEntity(ENTITY entity) {
		this.entity = entity;
	}

	public Class<ENTITY> getEntityClass() {
		return entityClass;
	}

	public void setEntityClass(Class<ENTITY> entityClass) {
		this.entityClass = entityClass;
	}

	public String prosseguir() {
		String page = FacesUtil.getViewRoot();
		String[] pages = page.split("/");
		StringBuffer retornoPage = new StringBuffer();
		for (int i = 0; i < pages.length; i++) {
			if (i == (pages.length - 1)) {
				retornoPage.append("/" + pages[4].replace("consultar", "manter"));
			} else {
				if (i > 0)
					retornoPage.append("/" + pages[i]);
			}
		}
		return retornoPage.toString();
	}

	public String newEntity() {
		try {
			novoRegisto = true;
			entity = entityClass.newInstance();
			afterInsert();
			return prosseguir();
		} catch (InstantiationException e) {
			System.err.printf("Ocorreu uma excecao na criacao da instancia da classe Entity.", e.getMessage());

		} catch (IllegalAccessException e) {
			System.err.printf("Ocorreu uma excecao na criacao da instancia da classe Entity.", e);
		}
		return "";

	}

	@SuppressWarnings("rawtypes")
	public List<IGenericEntity> listSelectEntity(Class entitySelect) {
		return service().findAll(entitySelect);
	}

	@SuppressWarnings("unchecked")
	public String edit() {

		editarRegistro = true;
		beforeEdit();
		entity = (ENTITY) getDataModel().getRowData();
		afterEdit();
		return prosseguir();

	}

	public void afterInsert() {
	}

	public void beforeEdit() {
	}

	public void afterEdit() {
	}

	public void beforeSave() {
	}

	public void afterSave() {
	}

	public void beforeRemove() {
	}

	public void afterRemove() {
	}

	public String back() {
		String page = FacesUtil.getViewRoot();
		String[] pages = page.split("/");
		StringBuffer retornoPage = new StringBuffer();
		for (int i = 0; i < pages.length; i++) {
			if (i == (pages.length - 1)) {
				retornoPage.append("/" + pages[4].replace("manter", "consultar"));
			} else {
				if (i > 0)
					retornoPage.append("/" + pages[i]);
			}
		}

		return retornoPage.toString();
	}

	public Map<String, String> getParametrosMapa() {
		return parametrosMapa;
	}

	public void setParametrosMapa(Map<String, String> parametrosMapa) {
		this.parametrosMapa = parametrosMapa;
	}

	public boolean isEditarRegistro() {
		return editarRegistro;
	}

	public void setEditarRegistro(boolean editar) {
		this.editarRegistro = editar;
	}

	public boolean isNovoRegisto() {
		return novoRegisto;
	}

	public void setNovoRegisto(boolean novoRegisto) {
		this.novoRegisto = novoRegisto;
	};

}
