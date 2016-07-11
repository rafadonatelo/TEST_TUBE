package com.testtube.entidade;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @version 1.0
 */
@Entity
@Table(name = "TB_AUTORIZADOR_TELA_USUARIO")
public class AutorizacaoTelaUsuario implements IGenericEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4203923037923920251L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "idAutorizacaoTela")
	private AutorizacaoTela autorizacaoTela;

	@ManyToOne
	@JoinColumn(name = "idUsuario")
	private Usuario usuario;

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public Long getId() {
		return id;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public AutorizacaoTela getAutorizacaoTela() {
		return autorizacaoTela;
	}

	public void setAutorizacaoTela(AutorizacaoTela autorizacaoTela) {
		this.autorizacaoTela = autorizacaoTela;
	}

}