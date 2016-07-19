package com.testtube.beans;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import com.testtube.entidade.TipoDocumentos;
import com.testtube.generics.GenericSimpleBeanImpl;

@ViewScoped
@Named
public class TipoDocumentosMBean extends GenericSimpleBeanImpl<TipoDocumentos> {

	private static final long serialVersionUID = 1L;

	@PostConstruct
	public void inicializar() {
		open();
	}
}
