package com.testtube.bo;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;

import com.testtube.dao.MaterialDAO;
import com.testtube.entidade.Material;
import com.testtube.entidade.MetodoMaterial;

@Stateless
public class MaterialBO extends GenericBO<Material> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private MaterialDAO dao;

	@Override
	@PostConstruct
	protected void inicializar() {
		this.genericDAO = dao;

	}

	public List<Material> buscarMaterialPorNome(String nome) {
		return dao.buscarMaterialPorNome(nome);
	}

	public void limparDadosDeMaterial(Long idMetodo) {
		dao.limparDadosDeMaterial(idMetodo);
	}

	public List<MetodoMaterial> buscarMetodosMateriais(Long idMetodo) {
		return dao.buscarMetodosMateriais(idMetodo);
	}

}
