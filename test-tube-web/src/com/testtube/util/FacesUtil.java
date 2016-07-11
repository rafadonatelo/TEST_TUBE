package com.testtube.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Esta classe contem um conjunto de metodos amplamente utilizados para
 * manipular componentes do JavaServer Faces.
 */
public class FacesUtil {

	/**
	 * Retorna o contexto do servlet.
	 * 
	 * @return O contexto do servlet
	 */
	public static ServletContext getServletContext() {
		return (ServletContext) getFacesContext().getExternalContext().getContext();
	}

	/**
	 * Retorna a request do contexto externo do faces.
	 * 
	 * @return Request do contexto externo do faces.
	 */
	public static HttpServletRequest getRequestScope() {
		return (HttpServletRequest) getFacesContext().getExternalContext().getRequest();
	}

	/**
	 * Retorna escopo Flash
	 * 
	 * @return
	 */
	public static Flash getFlashScope() {
		return FacesContext.getCurrentInstance().getExternalContext().getFlash();
	}

	/**
	 * Retorna um parametro armazenado no flashScope
	 * 
	 * @param parameter
	 * @return
	 */
	public static Object getFlashParameter(Object parameter) {
		return FacesContext.getCurrentInstance().getExternalContext().getFlash().get(parameter);
	}

	/**
	 * Retorna o response do contexto externo do faces.
	 * 
	 * @return Request do contexto externo do faces.
	 */
	public static HttpServletResponse getResponseScope() {
		return (HttpServletResponse) getFacesContext().getExternalContext().getResponse();
	}

	/**
	 * Retorna os parametros da requisicao da URL.
	 * 
	 * @return Map com par de chaves e valores da requisicao.
	 */
	public static Map<String, String> getRequestParam() {
		return getFacesContext().getExternalContext().getRequestParameterMap();
	}

	/**
	 * Retorna a sessao do contexto externo do faces.
	 * 
	 * @return HttpSession do contexto externo do faces.
	 */
	public static HttpSession getSessionScope() {
		return getRequestScope().getSession();
	}

	public static FacesContext getFacesContext() {
		return FacesContext.getCurrentInstance();
	}

	public static String getViewRoot() {
		return getFacesContext().getViewRoot().getViewId();
	}

	/**
	 * Devolve um array de objetos SelectItem (usados em combos e selects) a
	 * partir de uma lista de objetos de entidades. Obs: para o perfeito
	 * funcionamento deste método é nencessário que as classes de entidades
	 * implementem o metodo toString corretamente.
	 * 
	 * @author <a href="mailto:dayani.oliveira@sensedia.com.br">Dayani de
	 *         Oliveira</a>
	 * @param entities
	 *            Lista de objetos de entidades.
	 * @param selectOne
	 *            Habilita ou nao que o primeiro elemento da combo seja vazio.
	 * @return Array de objetos do SelectItem.
	 */
	public static SelectItem[] getSelectItems(List<?> entities, boolean selectOne) {
		int size = selectOne ? entities.size() + 1 : entities.size();
		SelectItem[] items = new SelectItem[size];
		int i = 0;
		if (selectOne) {
			items[0] = new SelectItem("", "---");
			i++;
		}
		for (Object x : entities) {
			items[i++] = new SelectItem(x, x.toString());
		}
		return items;
	}

	/**
	 * Adiciona uma mensagem de informacao.
	 * 
	 * @param key
	 *            de bundle da mensagem.
	 */
	public static void addInfoMessage(String key) {
		addInfoMessage(null, key);
	}

	/**
	 * Adiciona uma mensagem de informacao especifica para um componente.
	 * 
	 * @param clientId
	 *            O client id do componente.
	 * @param key
	 *            de bundle da mensagem.
	 */
	public static void addInfoMessage(String clientId, String key) {
		String keyvalue = getMessageByKey(key);
		FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, keyvalue, keyvalue);
		FacesContext.getCurrentInstance().addMessage(null, facesMsg);
	}

	/**
	 * Adiciona uma mensagem de erro.
	 * 
	 * @param key
	 *            de bundle da mensagem.
	 */
	public static void addErrorMessage(String key) {
		addErrorMessage(null, key);
	}

	/**
	 * Adiciona uma mensagem de erro especifica para um componente.
	 * 
	 * @param clientId
	 *            O client id do componente.
	 * @param key
	 *            de bundle da mensagem.
	 */
	public static void addErrorMessage(String clientId, String key, Object... params) {
		String keyvalue = getMessageByKey(key);
		// params);
		FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, keyvalue, keyvalue);
		FacesContext.getCurrentInstance().addMessage(clientId, facesMsg);
	}

	/**
	 * Adiciona uma mensagem de warning.
	 * 
	 * @param key
	 *            de bundle da mensagem.
	 */
	public static void addWarningMessage(String key) {
		addWarningMessage(null, key);
	}

	/**
	 * Adiciona uma mensagem de warning especifica para um componente.
	 * 
	 * @param clientId
	 *            O client id do componente.
	 * @param key
	 *            de bundle da mensagem.
	 */
	public static void addWarningMessage(String clientId, String key) {
		String keyvalue = "";
		getMessageByKey(key);
		FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_WARN, keyvalue, keyvalue);
		FacesContext.getCurrentInstance().addMessage(clientId, facesMsg);
	}

	/**
	 * Recupera a localidade da aplicacao, ou seja, se atualmente estah sendo
	 * utilizada a lingua portugues, ingles ou espanhol, por exemplo.
	 * 
	 * @return Retorna a localidade corrente.
	 */
	public static Locale getLocale() {
		return FacesContext.getCurrentInstance().getViewRoot().getLocale();
	}

	/**
	 * Pega a instancia do managed bean.
	 * 
	 * @param varName
	 *            Nome do managed-bean SEM as marcas da expressao EL #{}.
	 * @return Instancia do managed-bean.
	 */
	public static Object getELVar(String varName) {
		FacesContext ctx = FacesContext.getCurrentInstance();
		ELContext el = ctx.getELContext();
		ValueExpression ve = createVE(varName, Object.class);
		Object o = ve.getValue(el);
		return o;
	}

	/**
	 * Seta o valor de um objeto no managed bean.
	 * 
	 * @param varName
	 *            Nome do managed bean SEM as marcacoes da expressao EL #{}.
	 * @param newValue
	 *            Objeto que sera setado no managed-bean
	 */
	public static void setELVar(String varName, Object newValue) {
		FacesContext ctx = FacesContext.getCurrentInstance();
		ELContext el = ctx.getELContext();
		ValueExpression ve = createVE(varName, Object.class);
		ve.setValue(el, newValue);
	}

	/**
	 * Limpa todos os valores preenchido nos campos da view corrente (formulario
	 * corrente).
	 */
	public static void reset() {
		FacesContext.getCurrentInstance().getViewRoot().getChildren().clear();
	}

	/**
	 * Metodo privado usado para auxiliar os metodos de setELVar e getELVar.
	 * 
	 * @param varName
	 *            Nome do managed bean SEM as marcacoes da expressao EL #{}
	 * @param retorno
	 *            Classe do managed bean
	 * @return Um expressao EL
	 */
	private static ValueExpression createVE(String varName, Class<Object> retorno) {
		FacesContext ctx = FacesContext.getCurrentInstance();
		ExpressionFactory expf = ctx.getApplication().getExpressionFactory();
		ValueExpression ve = expf.createValueExpression(ctx.getELContext(), "#{" + varName + "}", retorno);
		return ve;
	}

	/**
	 * Metodo responsavel pela internacionalizacao de uma key passa como
	 * parametro. Este metodo utiliza tambem a classe utilitaria de message
	 * RsourceBundleUtil
	 * 
	 * @author lpache
	 * @param key
	 * @return valor I18n
	 */
	public static String getMessageByKey(String key) {
		try {
			String keyValue = ResourceBundle.getBundle("ApplicationMessages").getString(key);
			return keyValue;
		} catch (Exception e) {
			return key;
		}
	}

	/**
	 * Cria uma lista de selectItens onde o value = valor do enum, e o label =
	 * valor I18n do enum
	 * 
	 * @author lpache
	 * @param Classe
	 *            Enumerated
	 */
	@SuppressWarnings("rawtypes")
	public static List<SelectItem> getSelectItems(Class<? extends Enum> type) {
		ArrayList<SelectItem> result = new ArrayList<SelectItem>();
		for (Enum e : type.getEnumConstants()) {
			result.add(new SelectItem(e, FacesUtil.getMessageByKey(type.getSimpleName() + "." + e.name())));
		}
		return result;
	}

	/**
	 * Recebe uma lista de beans como parametro e devolve uma lista de
	 * selectItem do JSF
	 * 
	 * @param lista
	 *            = Uma lista de beans qualquer.
	 * @param fieldLabel
	 *            = O atributo dos beans que serao exibidos no label do
	 *            selectItem
	 * @return Retorna uma lista de SelectItem para ser usado em paginas JSF
	 */
	public static List<SelectItem> getSelectItems(List<? extends Object> lista, String... fieldLabel) {
		return createSelectItemListwithSeparator(lista, " ", fieldLabel);
	}

	/**
	 * Recebe uma lista de beans como parametro e devolve uma lista de
	 * selectItem do JSF
	 * 
	 * @param lista
	 *            = Uma lista de beans qualquer.
	 * @param fieldLabel
	 *            = O atributo dos beans que serao exibidos no label do
	 *            selectItem
	 * @param separator
	 *            = O caracter de separacao entre os atributos exibidos,
	 *            exemplos: ' - ' ou ' : ' ou ' / '
	 * 
	 * @return Retorna uma lista de SelectItem para ser usado em paginas JSF
	 */
	@SuppressWarnings("unused")
	public static List<SelectItem> createSelectItemListwithSeparator(List<? extends Object> lista, String separator, String... fieldLabel) {

		List<SelectItem> retorno = new ArrayList<SelectItem>();
		Iterator<? extends Object> iter = lista.iterator();

		while (iter.hasNext()) {
			Object element = iter.next();

			try {
				SelectItem selectItem = null;
				List<String> fields = new ArrayList<String>();
				for (String field : fieldLabel) {
					// BeanUtils.getNestedProperty(element, field);

					Object label = "";// BeanUtils.getNestedProperty(element,
										// field);
					if (label != null) {
						fields.add(String.valueOf(label));
					} else {
						fields.add("");
					}
				}
				StringBuffer concat = new StringBuffer();
				for (String field : fields) {
					concat.append(field + separator);
				}
				String pronta = concat.substring(0, concat.length() - separator.length());
				if (element != null) {
					selectItem = new SelectItem(element, pronta);
				} else {
					selectItem = new SelectItem("", "");
				}

				retorno.add(selectItem);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return retorno;
	}

	/**
	 * Este metodo recebe uma classe de enum e um valor key e retorna o valor
	 * I18n.
	 * 
	 * @param clazz
	 * @param key
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static String internationalizeEnum(Class<?> clazz, Enum key) {
		return FacesUtil.getMessageByKey(clazz.getSimpleName() + "." + key);
	}

	/**
	 * Este metodo recebe uma classe de enum e um valor I18n e retorna o enum
	 * correspondente. Funciona ao contrario do metodo internacionalize Enum.
	 * Sera util quando o combo foi populado com o metodo
	 * enumSelectItemsWithI18nValue.
	 */
	@SuppressWarnings("rawtypes")
	public static Enum unInternationalizeEnum(Class<? extends Enum> type, String I18nValue) {
		for (Enum e : type.getEnumConstants()) {
			if (I18nValue.equals(FacesUtil.getMessageByKey(type.getSimpleName() + "." + e.name()))) {
				return e;
			}
		}
		return null;
	}

	public static <T> T findManagedBean(String beanName, Class<T> beanClass) {
		FacesContext faces = FacesContext.getCurrentInstance();
		String expression = "#{" + beanName + "}";
		T managedBean = (T) faces.getApplication().evaluateExpressionGet(faces, expression, beanClass);
		return managedBean;
	}

	/**
	 * Redireciona o usuario para a pagina alvo
	 * 
	 * @param page
	 *            alvo
	 */
	public static void goToPage(String page) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
		HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest();
		String url = request.getContextPath();
		try {
			response.sendRedirect(url + page);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Metodo para fazer download de arquivos.s
	 * 
	 * @param file
	 * @param contentType
	 * @param fileName
	 * @return
	 */

	public String downloadFile(byte[] file, String contentType, String fileName) {
		try {
			FacesContext facesContext = javax.faces.context.FacesContext.getCurrentInstance();
			HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
			byte[] anexoBytes = file;
			response.setContentType(contentType);

			response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "" + "\";");
			response.setContentLength(anexoBytes.length);
			ServletOutputStream ouputStream = response.getOutputStream();
			ouputStream.write(anexoBytes, 0, anexoBytes.length);
			ouputStream.flush();
			ouputStream.close();

			facesContext.responseComplete();
		} catch (IOException e) {
			e.getMessage();
		}
		return "";
	}

	public static boolean existsErrorMessage() {
		return FacesMessage.SEVERITY_ERROR.equals(FacesContext.getCurrentInstance().getMaximumSeverity());
	}

	public static ELContext getElContext() {
		return getFacesContext().getELContext();
	}

	public static ExpressionFactory getExpressionFactory() {
		return getFacesContext().getApplication().getExpressionFactory();
	}

}
