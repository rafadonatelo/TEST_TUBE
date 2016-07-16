package com.testtube.beans;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import com.testtube.entidade.Categoria;
import com.testtube.entidade.Metodo;
import com.testtube.generics.GenericSimpleBeanImpl;
import com.testtube.util.FacesUtil;

@ViewScoped
@Named
public class MetodoMBean extends GenericSimpleBeanImpl<Metodo> {
	private static final long serialVersionUID = 1L;

	private List<Categoria> listCategoria;

	private Categoria categoria;

	private MaterialMBean materialMBean;

	@PostConstruct
	public void inicializar() {
		open();
		materialMBean = FacesUtil.findManagedBean("materialMBean", MaterialMBean.class);
	}

	@Override
	public void afterEdit() {
		this.categoria = (Categoria) service().buscarPorID(Categoria.class, this.entity.getIdCategoria());
	}

	@Override
	public void beforeSave() {
		this.entity.setIdCategoria(this.categoria.getId());
		this.entity.setNomeCategoria(this.categoria.getNome());
		materialMBean.getListSelectedMaterial();
	}

	@SuppressWarnings("unchecked")
	public List<Categoria> getListCategoria() {
		if (listCategoria == null) {
			listCategoria = (List<Categoria>) (List<?>) service().findAll(Categoria.class);
		}
		return listCategoria;

	}

	public void setListCategoria(List<Categoria> listCategoria) {
		this.listCategoria = listCategoria;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

}
