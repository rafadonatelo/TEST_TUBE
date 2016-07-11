package com.testtube.entidade;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "TB_AUTORIZADOR_TELA")
public class AutorizacaoTela implements IGenericEntity {

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
	@JoinColumn(name = "idAutorizacaoMenu")
	private AutorizacaoMenu autorizacaoMenu;

	@OneToMany(mappedBy = "autorizacaoTela", fetch = FetchType.LAZY)
	private List<AutorizacaoBotao> listAutorizacaoBotao;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getNomePermissao() {
		return nomePermissao;
	}

	public void setNomePermissao(String nomePermissao) {
		this.nomePermissao = nomePermissao;
	}

	public AutorizacaoMenu getAutorizacaoMenu() {
		return autorizacaoMenu;
	}

	public void setAutorizacaoMenu(AutorizacaoMenu autorizacaoMenu) {
		this.autorizacaoMenu = autorizacaoMenu;
	}

}
