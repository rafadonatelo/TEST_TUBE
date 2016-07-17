package com.testtube.service;

import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.testtube.audit.AuditavelFolha;
import com.testtube.bo.EntityGenericBO;
import com.testtube.bo.MaterialBO;
import com.testtube.bo.MetodoVideoBO;
import com.testtube.dao.CategoriaDAO;
import com.testtube.entidade.AssinaturaCategoria;
import com.testtube.entidade.Categoria;
import com.testtube.entidade.IGenericEntity;
import com.testtube.entidade.Material;
import com.testtube.entidade.MetodoMaterial;
import com.testtube.entidade.MetodoVideo;
import com.testtube.generics.ServiceAdapter;

@Stateless
public class SegurancaService extends ServiceAdapter {

	@Inject
	private EntityGenericBO entityGenericBO;

	@Inject
	private MaterialBO materialBO;

	@Inject
	private MetodoVideoBO metodoVideoBO;

	@Inject
	private CategoriaDAO categoriaDAO;

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

	public List<Material> buscarMaterialPorNome(String nome) {
		return materialBO.buscarMaterialPorNome(nome);
	}

	public void limparDadosDeMaterial(Long idMetodo) {
		materialBO.limparDadosDeMaterial(idMetodo);
	}

	public List<MetodoMaterial> buscarMetodosMateriais(Long idMetodo) {
		return materialBO.buscarMetodosMateriais(idMetodo);
	}

	public List<MetodoVideo> buscarMetodosVideo(Long idMetodo) {
		return metodoVideoBO.buscarMetodosVideo(idMetodo);
	}

	public List<Categoria> buscarCategoriaPorNome(String nome) {
		return categoriaDAO.buscarCategoriaPorNome(nome);
	}

	public void limparDadosDeAssinaturaCategoria(Long idAss) {
		categoriaDAO.limparDadosDeAssinaturaCategoria(idAss);
	}

	public List<AssinaturaCategoria> buscarAssinaturaCategoria(Long idAss) {
		return categoriaDAO.buscarAssinaturaCategoria(idAss);
	}

}
