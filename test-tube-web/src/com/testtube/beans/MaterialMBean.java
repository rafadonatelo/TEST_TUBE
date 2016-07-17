package com.testtube.beans;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import com.testtube.entidade.Material;
import com.testtube.generics.GenericSimpleBeanImpl;

@ViewScoped
@Named
public class MaterialMBean extends GenericSimpleBeanImpl<Material> {
	private static final long serialVersionUID = 1L;

	@PostConstruct
	public void inicializar() {
		openDetail();
		search();
	}

}
