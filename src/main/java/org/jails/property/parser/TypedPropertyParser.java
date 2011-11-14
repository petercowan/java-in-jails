package org.jails.property.parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TypedPropertyParser extends SimplePropertyParser {
	private static Logger logger = LoggerFactory.getLogger(TypedPropertyParser.class);

	private static String TYPE_PREFIX = "_";

	public String removeType(String rawProperty) {
		if (rawProperty == null) return null;
		String property = null;
		if (rawProperty.indexOf(DOT) > 0) {
			property = rawProperty.substring(rawProperty.indexOf(DOT) + 1);
		} else if (!rawProperty.startsWith(TYPE_PREFIX)) {
			property = rawProperty;
		}
		return property;

	}
}
