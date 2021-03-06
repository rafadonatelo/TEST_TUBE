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
@Table(name = "TB_AUTORIZADOR_MENU_USUARIO")
public class AutorizacaoMenuUsuario implements IGenericEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4203923037923920251L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "idAutorizacaoMenu")
	private AutorizacaoMenu autorizacaoMenu;

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

	public AutorizacaoMenu getAutorizacaoMenu() {
		return autorizacaoMenu;
	}

	public void setAutorizacaoMenu(AutorizacaoMenu autorizacaoMenu) {
		this.autorizacaoMenu = autorizacaoMenu;
	}

}