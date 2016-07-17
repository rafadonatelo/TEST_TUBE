package com.testtube.generics;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.primefaces.component.commandbutton.CommandButton;
import org.primefaces.component.commandlink.CommandLink;
import org.primefaces.event.SelectEvent;

import com.testtube.entidade.IGenericEntity;
import com.testtube.util.FacesUtil;

public abstract class AbstractGenericBean<ENTITY extends IGenericEntity> implements Serializable {

	private static final long serialVersionUID = 1L;

	private static ArrayList<UIComponent> listBotao;

	protected ENTITY entity;

	/** Instância da classe Class da entity **/
	protected Class<ENTITY> entityClass;

	/**
	 * Datamodel que contem o resultado da pesquisa, ou seja, a listagem de
	 * entidades
	 **/
	protected EntityLazyModel dataModel;

	protected Map<String, String> parametrosMapa;

	protected boolean editar;

	protected boolean novoRegisto;

	protected abstract ServiceAdapter service();

	@SuppressWarnings("unchecked")
	public void open() {
		novoRegisto = false;
		editar = false;
		entityClass = (Class<ENTITY>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		parametrosMapa = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		if (getParametrosMapa().get("entityID") != null) {
			setEditar(true);
			entity = (ENTITY) service().buscarPorID(entityClass, Long.valueOf(getParametrosMapa().get("entityID")));
			afterEdit();
		} else if (getParametrosMapa().get("newID") != null) {
			try {
				entity = entityClass.newInstance();
				afterInsert();
				novoRegisto = true;
			} catch (InstantiationException e) {
				System.err.printf("Ocorreu uma excecao na criacao da instancia da classe Entity.", e.getMessage());

			} catch (IllegalAccessException e) {
				System.err.printf("Ocorreu uma excecao na criacao da instancia da classe Entity.", e);
			}
		} else {
			search();
		}
	}

	@SuppressWarnings("unchecked")
	public void openDetail() {
		try {
			novoRegisto = false;
			editar = false;
			entityClass = (Class<ENTITY>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
			entity = entityClass.newInstance();
		} catch (InstantiationException e) {
			System.err.printf("Ocorreu uma excecao na criacao da instancia da classe Entity.", e.getMessage());

		} catch (IllegalAccessException e) {
			System.err.printf("Ocorreu uma excecao na criacao da instancia da classe Entity.", e);
		}

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
			FacesUtil.addInfoMessage("Registro salvo com sucesso!");
			open();
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
			FacesUtil.addInfoMessage("Registro removido com sucesso!");
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
			entity = entityClass.newInstance();
			return prosseguir();
		} catch (InstantiationException e) {
			System.err.printf("Ocorreu uma excecao na criacao da instancia da classe Entity.", e.getMessage());

		} catch (IllegalAccessException e) {
			System.err.printf("Ocorreu uma excecao na criacao da instancia da classe Entity.", e);
		}
		return "";

	}

	public boolean validarObrigatorio() {
		return true;
	}

	@SuppressWarnings("rawtypes")
	public List<IGenericEntity> listSelectEntity(Class entitySelect) {
		return service().findAll(entitySelect);
	}

	public String edit() {
		try {
			entity = entityClass.newInstance();
			return prosseguir();
		} catch (InstantiationException e) {
			System.err.printf("Ocorreu uma excecao na criacao da instancia da classe Entity.", e.getMessage());

		} catch (IllegalAccessException e) {
			System.err.printf("Ocorreu uma excecao na criacao da instancia da classe Entity.", e);
		}
		return "";
	}

	public void rowSelectEdit(SelectEvent event, String formName) {
		FacesContext fc = FacesContext.getCurrentInstance();
		ExternalContext ec = fc.getExternalContext();
		String redirect = ec.getRequestContextPath();
		redirect += prosseguir().replace("xhtml", "jsf");
		redirect += "?entityID=" + ec.getRequestParameterMap().get(formName + ":dtList_selection");
		try {
			entity = entityClass.newInstance();
			ec.redirect(redirect);
		} catch (InstantiationException e) {
			System.err.printf("Ocorreu uma excecao na criacao da instancia da classe Entity.", e.getMessage());
		} catch (IllegalAccessException e) {
			System.err.printf("Ocorreu uma excecao na criacao da instancia da classe Entity.", e);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void beforeSave() {
	}

	public void afterSave() {

	}

	public void afterInsert() {
	}

	public void afterEdit() {
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

	@SuppressWarnings("unused")
	private static void getUIComponentOfId(UIComponent root) {

		// FacesUtilRecurso.getFacesContext().getViewRoot().getChildren().iterator();
		// Iterator<UIComponent> list =
		// FacesUtilRecurso.getFacesContext().getViewRoot().getChildren().iterator();
		// for (Iterator<UIComponent> list = list.iterator(); list.hasNext();) {
		// type type = (type) list.next();
		//
		// }

		if (root.getChildCount() >= 0) {
			for (UIComponent subUiComponent : root.getChildren()) {
				UIComponent returnComponent = getUIComponentOfButton(subUiComponent);
				if (returnComponent != null) {
					if (returnComponent.getClass().equals(CommandLink.class) || returnComponent.getClass().equals(CommandButton.class)) {
						listBotao.add(returnComponent);
					}
				}
			}
		}
	}

	private static UIComponent getUIComponentOfButton(UIComponent root) {
		if (root.getClass().equals(CommandLink.class) || root.getClass().equals(CommandButton.class)) {
			return root;
		}
		if (root.getChildCount() >= 0) {
			for (UIComponent subUiComponent : root.getChildren()) {
				UIComponent returnComponent = getUIComponentOfButton(subUiComponent);
				if (returnComponent.getClass().equals(CommandLink.class) || returnComponent.getClass().equals(CommandButton.class)) {
					return returnComponent;
				}
			}

		}
		return null;
	}

	public Map<String, String> getParametrosMapa() {
		return parametrosMapa;
	}

	public void setParametrosMapa(Map<String, String> parametrosMapa) {
		this.parametrosMapa = parametrosMapa;
	}

	public boolean isEditar() {
		return editar;
	}

	public void setEditar(boolean editar) {
		this.editar = editar;
	}

	public boolean isNovoRegisto() {
		return novoRegisto;
	}

	public void setNovoRegisto(boolean novoRegisto) {
		this.novoRegisto = novoRegisto;
	}

	public boolean isSalvo() {
		return (entity.getId() != null);
	}

}
