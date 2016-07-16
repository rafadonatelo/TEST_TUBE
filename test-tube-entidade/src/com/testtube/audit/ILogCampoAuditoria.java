package com.testtube.audit;

import com.testtube.entidade.IGenericEntity;

public interface ILogCampoAuditoria extends IGenericEntity {

	public Long getId();

	public void setId(Long id);

	public String getCampo();

	public void setCampo(String campo);

	public String getCampoAnterior();

	public void setCampoAnterior(String valorAnterior);

	public String getCampoAtual();

	public void setCampoAtual(String valorAtual);

	public Object getLogAuditoria();

	public void setLogAuditoria(Object logAuditoria);

}
