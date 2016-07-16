package com.testtube.audit;

import java.util.Date;
import java.util.List;

import com.testtube.entidade.EnumLogicoOperacao;
import com.testtube.entidade.IGenericEntity;

public interface ILogAuditoria extends IGenericEntity {
	public Long getId();

	public void setId(Long id);

	public Date getDataAuditoria();

	public void setDataAuditoria(Date dataAuditoria);

	public EnumLogicoOperacao getOperacao();

	public void setOperacao(EnumLogicoOperacao operacao);

	public String getEntidade();

	public void setEntidade(String entidade);

	public List<? extends IGenericEntity> getListLogCampoAuditoria();

	public void setListLogCampoAuditoria(List<?> listLogCampoAuditoria);

	public String getUsuario();

	public void setUsuario(String usuario);

	public String getIpMaquinaLocal();

	public void setIpMaquinaLocal(String ipMaquinaLocal);

	public String getTelaSistema();

	public void setTelaSistema(String nomeTela);
}
