package com.testtube.generics;

import javax.naming.InitialContext;

import com.testtube.entidade.IGenericEntity;
import com.testtube.entidade.Usuario;
import com.testtube.service.SegurancaService;
import com.testtube.util.FacesUtil;

public abstract class GenericSimpleBeanImpl<ENTITY extends IGenericEntity> extends AbstractGenericBean<ENTITY> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected SegurancaService service() {
		try {
			return (SegurancaService) new InitialContext().lookup("java:global/test-tube-ear/test-tube-negocio/SegurancaService");
		} catch (Exception ex) {
			throw new IllegalArgumentException(ex);
		}
	}

	public Usuario getUsuarioLogado() {
		return (Usuario) FacesUtil.getSessionScope().getAttribute("usuarioLogado");
	}
}
