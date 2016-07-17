package com.testtube.bo;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;

import com.testtube.dao.MetodoVideoDAO;
import com.testtube.entidade.MetodoVideo;

@Stateless
public class MetodoVideoBO extends GenericBO<MetodoVideo> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private MetodoVideoDAO dao;

	@Override
	@PostConstruct
	protected void inicializar() {
		this.genericDAO = dao;

	}

	public List<MetodoVideo> buscarMetodosVideo(Long idMetodo) {
		return dao.buscarMetodosVideo(idMetodo);
	}

}
