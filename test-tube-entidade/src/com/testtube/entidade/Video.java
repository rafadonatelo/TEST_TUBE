package com.testtube.entidade;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.testtube.audit.AuditableEntity;
import com.testtube.enumeration.EnumModoVideo;

@Entity
@Table(name = "TB_VIDEO")
public class Video extends AuditableEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String titulo;

	@Enumerated(EnumType.STRING)
	private EnumModoVideo modo;

	@Column(columnDefinition = "text")
	private String descricao;

	@Column(columnDefinition = "text")
	private String url;

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

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public EnumModoVideo getModo() {
		return modo;
	}

	public void setModo(EnumModoVideo modo) {
		this.modo = modo;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
