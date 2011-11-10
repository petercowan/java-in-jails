package org.jails.form;

import org.jails.property.parser.PropertyParser;
import org.jails.property.parser.SimplePropertyParser;

public class SimpleFormParams {
	protected PropertyParser propertyParser = new SimplePropertyParser();

	public String getParameterName(String property) {
		return property;
		//return "_" + name + "." + property;
	}

	public String getIndexedParameterName(String property, Integer index) {
		return propertyParser.getRootProperty(property) + "[" + index + "]" + propertyParser.getNestedProperty(property);
		//return "_" + name + "[" + index + "]" + "." + property;
	}

	public String getMetaParameterName(String property) {
		return "_" + property;
		//return "_" + name + "_" + property;
	}


}
