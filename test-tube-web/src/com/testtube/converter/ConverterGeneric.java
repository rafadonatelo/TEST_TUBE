package com.testtube.converter;

import java.io.Serializable;
import java.lang.reflect.Method;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import com.testtube.entidade.IGenericEntity;
import com.testtube.entidade.SimpleValidate;
import com.testtube.service.SegurancaService;

@FacesConverter(value = "converterGeneric")
public class ConverterGeneric implements Converter {

	@Inject
	private SegurancaService service;

	private static final String CLASS_ATTRIBUTE = "classPersistent";

	/**
	 * Converte uma string em objeto.
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String string) {
		Object ob = string;

		try {
			if (string == null || SimpleValidate.isNullOrBlank(string) || string.startsWith("Selecio")) {
				return null;
			}
			// quebra a string resultande de 'getAsString'
			String[] values = string.split(CLASS_ATTRIBUTE);

			if (values != null && values.length == 2) {

				// separa o id e a classe desta entrada no converter
				String id = values[0].trim();
				String classe = (values[1]);

				// retorna a classe a partir da string
				Class<IGenericEntity> clazz = null;
				try {
					// substring necessario pq de inicio a string vem como
					// 'class br.com.gconv....'
					classe = classe.substring(6);
					clazz = (Class<IGenericEntity>) Class.forName(classe);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}

				// retorna o pacote para comparacao entre os Services da
				// aplicação
				Package pacote = clazz.getPackage();
				if (clazz.getSuperclass().equals(Enum.class)) {
					ob = id;
				} else {

					try {
						ob = service.buscarPorID(clazz, new Long(id));
					} catch (Exception e) {
						ob = service.buscarPorID(clazz, new Long(id));
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ob;
	}

	/**
	 * Converte um objeto em string.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public String getAsString(FacesContext context, UIComponent component, Object object) {

		String retorno = "";

		try {

			if (object == null) {
				return null;
			}

			retorno = object.toString();

			if (retorno == null || SimpleValidate.isNullOrBlank(retorno)) {
				return null;
			}

			if (object instanceof Serializable && !(object instanceof Number)) {

				Class clazz = object.getClass();

				Class[] interfaces = clazz.getSuperclass().getInterfaces();
				if (interfaces.length == 0) {
					interfaces = clazz.getSuperclass().getSuperclass().getInterfaces();
				}
				if (interfaces.length == 0) {
					interfaces = clazz.getSuperclass().getSuperclass().getSuperclass().getInterfaces();
				}
				if (interfaces.length == 0) {
					interfaces = clazz.getSuperclass().getSuperclass().getSuperclass().getSuperclass().getInterfaces();
				}

				if (interfaces != null && interfaces.length > 0 && interfaces[0].getName().equalsIgnoreCase("com.testtube.entidade.IGenericEntity")) {

					Method metId = clazz.getMethod("getId", null);

					if (metId != null) {
						Object idObj = metId.invoke(object, new Object[] {});

						if (idObj instanceof Long) {
							Long id = (Long) idObj;

							// concatena para cada entrada do converter o id +
							// constante + class.
							// com isso podemos saber exatamente a classe e id
							// de cada entidade para retorno dos dados.
							retorno = String.valueOf(id + CLASS_ATTRIBUTE + object.getClass());
						}
					}

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return retorno;

	}

}
