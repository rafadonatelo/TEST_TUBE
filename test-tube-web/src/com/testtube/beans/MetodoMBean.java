package com.testtube.beans;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.imageio.ImageIO;
import javax.inject.Named;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import com.testtube.entidade.Categoria;
import com.testtube.entidade.ConfiguracaoDeArquivos;
import com.testtube.entidade.Material;
import com.testtube.entidade.Metodo;
import com.testtube.entidade.MetodoMaterial;
import com.testtube.entidade.MetodoVideo;
import com.testtube.entidade.Midias;
import com.testtube.entidade.SimpleValidate;
import com.testtube.entidade.Video;
import com.testtube.enumeration.EnumModoVideo;
import com.testtube.generics.GenericSimpleBeanImpl;
import com.testtube.util.FacesUtil;
import com.testtube.util.ManipuladorFile;

@ViewScoped
@Named
public class MetodoMBean extends GenericSimpleBeanImpl<Metodo> {
	private static final long serialVersionUID = 1L;

	private List<Categoria> listCategoria;

	private List<Material> listSelectedMaterial = new ArrayList<Material>();

	private UploadedFile file;

	private List<MetodoVideo> listVideo = new ArrayList<MetodoVideo>();

	private Categoria categoria;

	private ManipuladorFile mf = new ManipuladorFile();

	private Midias midia;

	private ConfiguracaoDeArquivos conf;

	private MetodoVideo mv;

	@PostConstruct
	public void inicializar() {
		open();
		conf = service().obterConfiguracaoDefault();
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

	// TODO: Midias
	public File handleFileUpload(FileUploadEvent event) {
		FacesContext fc = FacesContext.getCurrentInstance();
		File aux = null;
		try {
			File file = new File(event.getFile().getFileName());
			BufferedOutputStream out;
			BufferedImage bi;
			if (!this.mf.verificaDiretorio(this.conf.getCaminhoUploadVideos() + file.getName())) {
				String path = this.conf.getCaminhoUploadVideos() + file.getName();
				aux = new File(path);

				BufferedInputStream in = new BufferedInputStream(event.getFile().getInputstream());
				if (checkFile(file.getName().substring(aux.getName().lastIndexOf('.') + 1))) {
					bi = ImageIO.read(in);
					ImageIO.write(bi, "png", aux);
				} else {
					out = new BufferedOutputStream(new FileOutputStream(aux));
					while (in.available() != 0) {
						out.write(in.read());
					}
					out.flush();
					out.close();
				}
				in.close();

				fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Arquivo " + file.getName() + "Criado/Salvo ...", "Arquivo " + file.getName() + "Criado/Salvo ..."));
			} else {
				fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "O arquivo " + file.getName() + " já existe/ou não tem permissão de escrita...",
						"O arquivo " + file.getName() + " já existe/ou não tem permissão de escrita..."));
			}
			return aux;
		} catch (Exception ex) {
			fc.addMessage(null, new FacesMessage(ex.getMessage()));
		}
		return null;
	}

	public String criaArquivoESalvar(FileUploadEvent event) {
		FacesContext fc = FacesContext.getCurrentInstance();
		try {
			File aux = handleFileUpload(event);

			midia = new Midias();
			midia.setTitulo(aux.getName());
			midia.setTamanho(aux.length());
			midia.setPath(aux.getAbsolutePath());
			midia.setUrl(aux.getAbsolutePath());
			midia.setExtensao(aux.getName().substring(aux.getName().lastIndexOf('.') + 1));
			midia.setNomeDoArquivo(aux.getName());

			this.mv.setMidia(midia);
			fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Midia salva com sucesso!", "Midia cadastrada com sucesso!"));

			return "";
		} catch (Exception e) {
			fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Falha ao persistir a Midia!", "Falha ao persistir a Midia!"));
		}
		return "";
	}

	public boolean checkFile(String aux) {
		if (aux.contains("jpg") || aux.contains("jpeg") || aux.contains("png") || aux.contains("gif"))
			return true;
		return false;
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
				v.setMidia((Midias) service().salvar(v.getMidia()));
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

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}

	public Midias getMidia() {
		return midia;
	}

	public void setMidia(Midias midia) {
		this.midia = midia;
	}

}
