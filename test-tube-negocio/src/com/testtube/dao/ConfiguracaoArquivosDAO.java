package com.testtube.dao;

import javax.annotation.PostConstruct;
import javax.ejb.Startup;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import com.testtube.entidade.ConfiguracaoDeArquivos;
import com.testtube.generics.ExtendsGenericDAO;

@Startup
@javax.inject.Singleton
public class ConfiguracaoArquivosDAO extends ExtendsGenericDAO<ConfiguracaoDeArquivos> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ConfiguracaoArquivosDAO() {
		super(ConfiguracaoDeArquivos.class);
	}

	@PostConstruct
	public void iniciando() {
		verificarConfiguracao();
	}

	/**
	 * Cria a config default
	 */
	public void criarConfiguracaoDefault() {
		ConfiguracaoDeArquivos conf = new ConfiguracaoDeArquivos();
		conf.setConfigDefault(1);
		this.getEntityManager().persist(conf);
	}

	/**
	 * Checa a configuração default
	 * 
	 * @return <code>ConfiguracaoDeArquivos</code> - ConfiguracaoDeArquivos
	 */
	public ConfiguracaoDeArquivos verificarConfiguracao() {
		TypedQuery<Long> query = this.getEntityManager().createQuery("select count(c) from ConfiguracaoDeArquivos c", Long.class);
		long valor = (long) query.getSingleResult();
		if (valor == 0)
			criarConfiguracaoDefault();
		return obterConfiguracao();
	}

	/**
	 * Retorna a configuração default.
	 * 
	 * @return <code>ConfiguracaoDeArquivos</code> - ConfiguracaoDeArquivos
	 */

	public ConfiguracaoDeArquivos obterConfiguracao() {
		try {
			TypedQuery<ConfiguracaoDeArquivos> query = this.getEntityManager().createQuery("select c from ConfiguracaoDeArquivos c where c.configDefault = 1", ConfiguracaoDeArquivos.class);
			return query.getResultList().get(0);
		} catch (NoResultException e) {

		}
		return null;

	}

	public ConfiguracaoDeArquivos salvarConf(ConfiguracaoDeArquivos c) {
		return this.salvar(c);
	}

}
