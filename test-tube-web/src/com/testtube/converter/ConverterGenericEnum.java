package com.testtube.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.testtube.util.FacesUtil;

@FacesConverter(value = "converterGenericEnum")
public class ConverterGenericEnum implements Converter {

	private static final String CLASS_ATTRIBUTE = "classPersistent";

	/**
	 * Converte uma string em enum.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String string) {

		if (string == null || string.trim().isEmpty() || string.trim().equals("Selecione...") || string.trim().equals("Selecione....")) {
			return null;
		}

		String[] values = string.split(CLASS_ATTRIBUTE);

		String id = values[0].trim();
		String classe = (values[1]);

		Class<Enum> clazz = null;
		try {
			classe = classe.substring(6);
			clazz = (Class<Enum>) Class.forName(classe);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		Enum retorno = null;

		retorno = FacesUtil.unInternationalizeEnum(clazz, id);

		return retorno;

	}

	/**
	 * Converte um enum em string.
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public String getAsString(FacesContext context, UIComponent component, Object object) {
		if (object == null || !(object instanceof Enum)) {
			return null;
		}

		String enume = FacesUtil.internationalizeEnum(object.getClass(), (Enum) object);

		return enume + CLASS_ATTRIBUTE + object.getClass();
	}

}
