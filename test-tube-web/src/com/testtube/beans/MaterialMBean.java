package com.testtube.beans;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.event.SelectEvent;

import com.testtube.entidade.Material;
import com.testtube.generics.GenericSimpleBeanImpl;

@ViewScoped
@Named
public class MaterialMBean extends GenericSimpleBeanImpl<Material> {
	private static final long serialVersionUID = 1L;

	private List<Material> listSelectedMaterial = new ArrayList<Material>();

	@PostConstruct
	public void inicializar() {
		openDetail();
		search();
	}

	public void onRowSelect(SelectEvent event) {
		this.listSelectedMaterial.add((Material) event.getObject());
	}

	public List<Material> getListSelectedMaterial() {
		return listSelectedMaterial;
	}

	public void setListSelectedMaterial(List<Material> listSelectedMaterial) {
		this.listSelectedMaterial = listSelectedMaterial;
	}

}
