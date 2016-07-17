package com.testtube.beans;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import com.testtube.entidade.Assinatura;
import com.testtube.entidade.AssinaturaCategoria;
import com.testtube.entidade.Categoria;
import com.testtube.entidade.SimpleValidate;
import com.testtube.generics.GenericSimpleBeanImpl;

@ViewScoped
@Named
public class AssinaturaMBean extends GenericSimpleBeanImpl<Assinatura> {
	private static final long serialVersionUID = 1L;

	private List<Categoria> listCategoria = new ArrayList<>();

	private Categoria categoria;

	@PostConstruct
	public void inicializar() {
		open();
		this.listCategoria.clear();
		for (AssinaturaCategoria c : service().buscarAssinaturaCategoria(this.entity.getId())) {
			this.listCategoria.add(c.getCategoria());
		}
	}

	@Override
	public void afterEdit() {

	}

	@Override
	public void beforeSave() {
		if (this.entity.getId() != null)
			service().limparDadosDeAssinaturaCategoria(this.entity.getId());

	}

	@Override
	public void afterSave() {

		AssinaturaCategoria mm;
		if (!SimpleValidate.isNullOrEmpty(listCategoria)) {
			for (Categoria m : listCategoria) {
				mm = new AssinaturaCategoria();
				mm.setAssinatura(this.entity);
				mm.setCategoria(m);
				service().salvar(mm);
			}
		}
	}

	public List<Categoria> completeCategoria(String query) {
		return service().buscarCategoriaPorNome(query);
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

	public List<Categoria> getListCategoria() {
		return listCategoria;
	}

}
