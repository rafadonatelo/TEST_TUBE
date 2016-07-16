package com.testtube.beans;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.testtube.entidade.Categoria;
import com.testtube.generics.GenericSimpleBeanImpl;

@ViewScoped
@ManagedBean
public class CategoriaMBean extends GenericSimpleBeanImpl<Categoria> {
	private static final long serialVersionUID = 1L;

	@PostConstruct
	public void inicializar() {
		open();

	}
}
