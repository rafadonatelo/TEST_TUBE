package com.testtube.entidade;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.testtube.audit.AuditableEntity;

@Entity
@Table(name = "TB_CONFIGURACAO_ARQUIVOS")
public class ConfiguracaoDeArquivos extends AuditableEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4203923037923920251L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String caminhoUploadAnexos;// Caminho do diretório fisíco p/ UPLOAD

	private String caminhoUploadVideos;// URL principal dos arquivos

	private Integer configDefault;// Coluna que identifica a configuração válida

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public Long getId() {
		return id;
	}

	public String getCaminhoUploadAnexos() {
		return caminhoUploadAnexos;
	}

	public void setCaminhoUploadAnexos(String caminhoUploadAnexos) {
		this.caminhoUploadAnexos = caminhoUploadAnexos;
	}

	public String getCaminhoUploadVideos() {
		return caminhoUploadVideos;
	}

	public void setCaminhoUploadVideos(String caminhoUploadVideos) {
		this.caminhoUploadVideos = caminhoUploadVideos;
	}

	public Integer getConfigDefault() {
		return configDefault;
	}

	public void setConfigDefault(Integer configDefault) {
		this.configDefault = configDefault;
	}

}
