package org.jails.property.parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TypedPropertyParser extends SimplePropertyParser {
	private static Logger logger = LoggerFactory.getLogger(TypedPropertyParser.class);

	private static String TYPE_PREFIX = "_";

	public String getType(String rawProperty) {
		String type = (rawProperty != null
				&& rawProperty.startsWith(TYPE_PREFIX)
				&& rawProperty.indexOf(DOT) > 0)
				? rawProperty.substring(0, rawProperty.indexOf(DOT)) : null;
		logger.info("type: " + type);

		return type;
	}

	public String getPropertyName(String rawProperty) {
		String property = removeType(rawProperty);
		if (property != null) return super.getPropertyName(property);
		else return property;
	}

	@Override
	public boolean hasNestedProperty(String rawProperty) {
		return super.hasNestedProperty(removeType(rawProperty));
	}

	@Override
	public String getNestedProperty(String rawProperty) {
		return super.getNestedProperty(removeType(rawProperty));
	}

	@Override
	public String getRootProperty(String rawProperty) {
		return super.getRootProperty(removeType(rawProperty));
	}

	private String removeType(String rawProperty) {
		if (rawProperty == null) return null;
		String property = null;
		if (rawProperty.startsWith(TYPE_PREFIX) && rawProperty.indexOf(DOT) > 0) {
			property = rawProperty.substring(rawProperty.indexOf(DOT) + 1);
		} else if (!rawProperty.startsWith(TYPE_PREFIX)) {
			property = rawProperty;
		}
		return property;

	}
}
