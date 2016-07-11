package com.testtube.entidade;

import java.util.Collection;

public class SimpleValidate {

	public static boolean isNullOrBlank(String str) {

		if (str != null) {
			str = str.trim();
		}

		return (str == null || "".equals(str) || "null".equals(str));
	}

	@SuppressWarnings("rawtypes")
	public static boolean isNullOrEmpty(Collection collection) {
		return (collection == null || collection.isEmpty());
	}
}
