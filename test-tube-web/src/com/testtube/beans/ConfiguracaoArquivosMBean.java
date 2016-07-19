package com.testtube.beans;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.testtube.entidade.ConfiguracaoDeArquivos;
import com.testtube.generics.GenericSimpleBeanImpl;
import com.testtube.service.SegurancaService;

@ViewScoped
@ManagedBean
public class ConfiguracaoArquivosMBean extends GenericSimpleBeanImpl<ConfiguracaoDeArquivos> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EJB
	private SegurancaService service;

	private ConfiguracaoDeArquivos configuracaoDefault;

	public ConfiguracaoArquivosMBean() {

	}

	@PostConstruct
	public void init() {
		this.configuracaoDefault = this.service.obterConfiguracaoDefault();
		open();
	}

	@Override
	public String edit() {
		// TODO Auto-generated method stub
		return super.edit();
	}

	public ConfiguracaoDeArquivos getConfiguracaoDefault() {
		return configuracaoDefault;
	}

	public void setConfiguracaoDefault(ConfiguracaoDeArquivos configuracaoDefault) {
		this.configuracaoDefault = configuracaoDefault;
	}

}
