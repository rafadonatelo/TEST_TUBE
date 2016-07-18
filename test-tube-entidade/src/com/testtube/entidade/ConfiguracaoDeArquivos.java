package com.testtube.entidade;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.testtube.audit.AuditableEntity;



/**
 * 
 * @author Equipe de desenvolvimento DPGE-MS.
 * @since JDK8
 * @version 1.0 - 2016
 * 
 */
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
	private String caminhoUpload;// Caminho do diretório fisíco p/ UPLOAD
	private String urlMidias;// URL principal dos arquivos
	private String diretorioFotosPerfil;// nome do diretório onde serão salvas
										// as fotos de perfil
	private Integer configDefault;// Coluna que identifica a configuração válida

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public Long getId() {
		return id;
	}

	public String getCaminhoUpload() {
		return caminhoUpload;
	}

	public void setCaminhoUpload(String caminhoUpload) {
		this.caminhoUpload = caminhoUpload;
	}

	public String getUrlMidias() {
		return urlMidias;
	}

	public void setUrlMidias(String urlMidias) {
		this.urlMidias = urlMidias;
	}

	public Integer getConfigDefault() {
		return configDefault;
	}

	public void setConfigDefault(Integer configDefault) {
		this.configDefault = configDefault;
	}

	public String getDiretorioFotosPerfil() {
		return diretorioFotosPerfil;
	}

	public void setDiretorioFotosPerfil(String diretorioFotosPerfil) {
		this.diretorioFotosPerfil = diretorioFotosPerfil;
	}

}
