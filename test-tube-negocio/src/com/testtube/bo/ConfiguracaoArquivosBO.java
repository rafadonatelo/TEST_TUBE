package com.testtube.bo;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;

import com.testtube.dao.ConfiguracaoArquivosDAO;
import com.testtube.entidade.ConfiguracaoDeArquivos;

@Stateless
public class ConfiguracaoArquivosBO extends GenericBO<ConfiguracaoDeArquivos> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private ConfiguracaoArquivosDAO dao;

	@Override
	@PostConstruct
	protected void inicializar() {
		this.genericDAO = dao;
	}

	public void criarConfiguracaoDefault() {
		dao.criarConfiguracaoDefault();
	}

	public ConfiguracaoDeArquivos verificarConfiguracao() {
		return dao.verificarConfiguracao();
	}

	public ConfiguracaoDeArquivos obterConfiguracao() {
		return dao.obterConfiguracao();
	}

	public ConfiguracaoDeArquivos salvarConf(ConfiguracaoDeArquivos c) {
		return this.salvar(c);
	}

}