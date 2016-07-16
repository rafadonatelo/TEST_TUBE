package com.testtube.bo;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;

import com.testtube.dao.GenericDAO;
import com.testtube.entidade.IGenericEntity;

/**
 * @author codigosfontes.com.br
 * BO genérico com as operações básicas
 */
public abstract class GenericBO<E> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected GenericDAO<E> genericDAO;

	@PostConstruct
	protected abstract void inicializar();

	public E salvar(E entidade) {
		return getGenericDAO().salvar(entidade);
	}

	public void excluir(E entidade) {
		getGenericDAO().excluir(entidade);
	}

	public void remover(Long id) {
		getGenericDAO().excluir(getGenericDAO().buscarPorId(id));
	}

	public boolean remover(E entidade) {
		return getGenericDAO().remove(entidade);
	}

	public List<E> buscarTodos(String ordenarPor) {
		return getGenericDAO().buscarListaPorCriterio(Order.asc(ordenarPor));
	}

	public List<E> listarTodos(String ordenarPor) {
		return getGenericDAO().buscarListaPorCriterio(Order.asc(ordenarPor));
	}

	public List<E> buscarTodos() {
		return getGenericDAO().buscarTodos();
	}

	public List<E> listarTodos() {
		return getGenericDAO().buscarTodos();
	}

	public E buscarPorId(Long id) {
		return (E) getGenericDAO().buscarPorId(id);
	}

	public void detach(IGenericEntity entity) {
		getGenericDAO().detach(entity);
	}

	@SuppressWarnings("rawtypes")
	public E buscarPorId(Long id, Class clazz) {
		return (E) getGenericDAO().buscarPorId(id, clazz);
	}

	public E buscarUmPorCampoCompleto(String campo, Object valor) {
		return (E) getGenericDAO().buscarUmPorCampoCompleto(campo, valor);
	}

	public List<E> buscarListaPorCriterio(Criterion... criterion) {
		return getGenericDAO().buscarListaPorCriterio(criterion);
	}

	public List<E> buscarListaPorCriterio(Order order, Criterion... criterion) {
		return getGenericDAO().buscarListaPorCriterio(order, criterion);
	}

	public E buscarUmPorCriterio(Criterion... criterion) {
		return getGenericDAO().buscarUmPorCriterio(criterion);
	}

	public GenericDAO<E> getGenericDAO() {
		return genericDAO;
	}

	public void setGenericDAO(GenericDAO<E> genericDAO) {
		this.genericDAO = genericDAO;
	}

	public Long count() {
		return getGenericDAO().count();
	}
}