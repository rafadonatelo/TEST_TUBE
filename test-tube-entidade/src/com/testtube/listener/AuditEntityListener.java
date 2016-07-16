package com.testtube.listener;

import java.util.Date;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import com.testtube.audit.AuditableEntity;
import com.testtube.entidade.UsuarioPadrao;
import com.testtube.util.FacesUtil;

public class AuditEntityListener {

	@PreUpdate
	public void auditUpdate(AuditableEntity entity) {

		entity.setUsuarioUltimaAlteracao(UsuarioLogado());
		entity.setDataUltimaAlteracao(new Date());
	}

	@PrePersist
	public void auditPersist(AuditableEntity entity) {
		entity.setUsuarioCriacao(UsuarioLogado());
		entity.setDataCriacao(new Date());
		entity.setUsuarioUltimaAlteracao(UsuarioLogado());
		entity.setDataUltimaAlteracao(new Date());
	}

	private static String UsuarioLogado() {
		if (FacesUtil.getSessionScope().getAttribute("usuarioLogado") == null) {
			return null;
		}
		return ((UsuarioPadrao) FacesUtil.getSessionScope().getAttribute("usuarioLogado")).usuarioLogado();
	}
}
