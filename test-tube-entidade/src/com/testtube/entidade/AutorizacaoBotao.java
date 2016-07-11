package com.testtube.entidade;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "TB_AUTORIZACAO_BOTAO")
public class AutorizacaoBotao implements IGenericEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String descricao;

	private String nomePermissao;

	@ManyToOne
	@JoinColumn(name = "idAutorizacaoTela")
	private AutorizacaoTela autorizacaoTela;

	@Override
	public Long getId() {
		return id;
	}

	public String getNomePermissao() {
		return nomePermissao;
	}

	public void setNomePermissao(String nomePermissao) {
		this.nomePermissao = nomePermissao;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public AutorizacaoTela getAutorizacaoTela() {
		return autorizacaoTela;
	}

	public void setAutorizacaoTela(AutorizacaoTela autorizacaoTela) {
		this.autorizacaoTela = autorizacaoTela;
	}

}
