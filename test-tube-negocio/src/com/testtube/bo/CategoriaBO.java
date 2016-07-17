package com.testtube.bo;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;

import com.testtube.dao.CategoriaDAO;
import com.testtube.entidade.AssinaturaCategoria;
import com.testtube.entidade.Categoria;

@Stateless
public class CategoriaBO extends GenericBO<Categoria> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private CategoriaDAO dao;

	@Override
	@PostConstruct
	protected void inicializar() {
		this.genericDAO = dao;

	}

	public List<Categoria> buscarCategoriaPorNome(String nome) {
		return dao.buscarCategoriaPorNome(nome);
	}

	public void limparDadosDeAssinaturaCategoria(Long idAss) {
		dao.limparDadosDeAssinaturaCategoria(idAss);
	}

	public List<AssinaturaCategoria> buscarAssinaturaCategoria(Long idAss) {
		return dao.buscarAssinaturaCategoria(idAss);
	}

}
