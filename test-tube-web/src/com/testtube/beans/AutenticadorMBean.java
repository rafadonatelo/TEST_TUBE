package com.testtube.beans;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import com.testtube.entidade.SimpleValidate;
import com.testtube.entidade.Usuario;
import com.testtube.util.FacesUtil;

@ViewScoped
@Named
public class AutenticadorMBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String user;

	private String senha;

	@PostConstruct
	private void init() {
		user = "";
		senha = "";
	}

	public String autenticar() {
		String outcome = "";
		Usuario usuario = null;
		try {
			if (!SimpleValidate.isNullOrBlank(user) && !SimpleValidate.isNullOrBlank(senha)) {
				if (user.equals("admin") && senha.equals("admin")) {
					usuario = new Usuario();
					usuario.setLogin("admin");
					usuario.setNome("Admin");
					FacesUtil.getSessionScope().setAttribute("usuarioLogado", usuario);
					outcome = UrlRetorno();
					FacesUtil.getResponseScope().sendRedirect(outcome);
				}
				/*	
				usuario = service.validaUsuarioSenha(user, senha);
				
				if (usuario != null) {
					FacesUtil.getSessionScope().setAttribute("usuarioLogado", usuario);
					outcome = UrlRetorno();
					FacesUtil.getResponseScope().sendRedirect(outcome);
				}
				*/
			}
		} catch (Exception e) {
			String error = FacesUtil.getMessageByKey("comum.cadastro.erroLogin");
			FacesUtil.addErrorMessage(error + " " + e.getCause().getMessage());
		}
		return outcome;
	}

	public String UrlRetorno() {
		String path = FacesContext.getCurrentInstance().getExternalContext().getApplicationContextPath();
		return path + "/admin/index.jsf";

	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

}
