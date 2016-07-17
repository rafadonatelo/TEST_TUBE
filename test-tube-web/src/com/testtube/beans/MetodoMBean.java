package com.testtube.beans;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import com.testtube.entidade.Categoria;
import com.testtube.entidade.Material;
import com.testtube.entidade.Metodo;
import com.testtube.entidade.MetodoMaterial;
import com.testtube.entidade.MetodoVideo;
import com.testtube.entidade.SimpleValidate;
import com.testtube.entidade.Video;
import com.testtube.enumeration.EnumModoVideo;
import com.testtube.generics.GenericSimpleBeanImpl;
import com.testtube.util.FacesUtil;

@ViewScoped
@Named
public class MetodoMBean extends GenericSimpleBeanImpl<Metodo> {
	private static final long serialVersionUID = 1L;

	private List<Categoria> listCategoria;

	private List<Material> listSelectedMaterial = new ArrayList<Material>();

	private List<MetodoVideo> listVideo = new ArrayList<MetodoVideo>();

	private Categoria categoria;

	private MetodoVideo mv;

	@PostConstruct
	public void inicializar() {
		open();

	}

	@Override
	public void afterEdit() {
		this.categoria = (Categoria) service().buscarPorID(Categoria.class, this.entity.getIdCategoria());
		this.listSelectedMaterial.clear();
		carregarMetodosVideo();
		for (MetodoMaterial mm : service().buscarMetodosMateriais(this.entity.getId())) {
			this.listSelectedMaterial.add(mm.getMaterial());
		}

	}

	public void carregarMetodosVideo() {
		this.listVideo.clear();
		this.listVideo = service().buscarMetodosVideo(this.entity.getId());
	}

	public void adicionarVideos() {
		this.mv = new MetodoVideo();
		this.mv.setVideo(new Video());
	}

	@Override
	public void beforeSave() {
		this.entity.setIdCategoria(this.categoria.getId());
		this.entity.setNomeCategoria(this.categoria.getNome());
		if (this.entity.getId() != null)
			service().limparDadosDeMaterial(this.entity.getId());
	}

	@Override
	public void afterSave() {
		MetodoMaterial mm;
		if (!SimpleValidate.isNullOrEmpty(listSelectedMaterial)) {
			for (Material m : listSelectedMaterial) {
				mm = new MetodoMaterial();
				mm.setMaterial(m);
				mm.setMetodo(this.entity);
				service().salvar(mm);
			}
		}
		if (!SimpleValidate.isNullOrEmpty(listVideo)) {
			for (MetodoVideo v : listVideo) {
				v.setVideo((Video) service().salvar(v.getVideo()));
				v.setMetodo(this.entity);
				service().salvar(v);
			}
			carregarMetodosVideo();
		}
	}

	@SuppressWarnings("unchecked")
	public List<Categoria> getListCategoria() {
		if (listCategoria == null) {
			listCategoria = (List<Categoria>) (List<?>) service().findAll(Categoria.class);
		}
		return listCategoria;

	}

	public void salvarVideo() {
		if (mv.getId() != null) {
			int indice = listVideo.indexOf(mv);
			listVideo.set(indice, mv);
		} else
			listVideo.add(mv);
		FacesUtil.addInfoMessage("Vídeo salvo com sucesso!");
	}

	public void removerVideo() {
		this.listVideo.remove(this.mv);
		service().removerEntity(this.mv);
		service().removerEntity(this.mv.getVideo());
		this.mv = new MetodoVideo();
		carregarMetodosVideo();
		FacesUtil.addInfoMessage("Vídeo removido com sucesso!");
	}

	public List<EnumModoVideo> getModosVideo() {
		return Arrays.asList(EnumModoVideo.values());
	}

	public List<Material> completeMaterial(String query) {
		return service().buscarMaterialPorNome(query);
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

	public List<Material> getListSelectedMaterial() {
		return listSelectedMaterial;
	}

	public void setListSelectedMaterial(List<Material> listSelectedMaterial) {
		this.listSelectedMaterial = listSelectedMaterial;
	}

	public List<MetodoVideo> getListVideo() {
		return listVideo;
	}

	public void setListVideo(List<MetodoVideo> listVideo) {
		this.listVideo = listVideo;
	}

	public MetodoVideo getMv() {
		return mv;
	}

	public void setMv(MetodoVideo mv) {
		this.mv = mv;
	}

}
