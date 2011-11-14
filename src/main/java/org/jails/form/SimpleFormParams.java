package org.jails.form;

public class SimpleFormParams {

	public String getParameterName(String property) {
		return property;
	}

	public String getFormIndexedParameterName(String formName, String property, Integer index) {
		 return formName + "[" + index + "]" + property;
	}

	public String getMetaParameterName(String property) {
		return "_" + property;
	}


}
