package org.jails.property.parser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimplePropertyParser implements PropertyParser {
	private static Logger logger = LoggerFactory.getLogger(SimplePropertyParser.class);

	protected static String DOT = ".";
	protected static String OPEN_INDEX = "[";
	protected static String CLOSE_INDEX = "]";


	public String getPropertyName(String rawProperty) {
		String propertyName;
		if (rawProperty != null && rawProperty.indexOf(OPEN_INDEX) >= 0) {
			if (rawProperty.indexOf(CLOSE_INDEX) != rawProperty.length() - 1) {
				propertyName = rawProperty.substring(0, rawProperty.indexOf(OPEN_INDEX))
					+ rawProperty.substring(rawProperty.indexOf(CLOSE_INDEX) + 1);
			} else {
				propertyName = rawProperty.substring(0, rawProperty.indexOf(OPEN_INDEX));
			}

		} else {
			propertyName = rawProperty;
		}
		logger.info("propertyName: " + propertyName);
		return propertyName;
	}

	public boolean hasNestedProperty(String rawProperty) {
		return rawProperty != null && rawProperty.indexOf(DOT) >= 0;
	}

	public String getNestedProperty(String rawProperty) {
		String nestedProperty = (rawProperty.indexOf(DOT) >= 0) ? rawProperty.substring(rawProperty.indexOf(DOT) + 1) : null;
		logger.info("nestedProperty: " + nestedProperty);
		return nestedProperty;
	}

	public String getRootProperty(String rawProperty) {
		String parentProperty = (rawProperty.indexOf(DOT) >= 0)
				? rawProperty.substring(0, rawProperty.indexOf(DOT))
				: rawProperty;
		logger.info("rootProperty: " + parentProperty);
		return parentProperty;
	}

	public Integer getPropertyIndex(String rawProperty) {
		String indexProperty;
		if (hasNestedProperty(rawProperty)) {
			indexProperty = getRootProperty(rawProperty);
		} else {
			indexProperty = rawProperty;
		}

		Integer index;
		try {
			index = (indexProperty.indexOf(OPEN_INDEX) >= 0)
					? Integer.parseInt(
							indexProperty.substring(
									indexProperty.indexOf(OPEN_INDEX) + 1,
									indexProperty.indexOf(CLOSE_INDEX)))
					: null;
		} catch (NumberFormatException e) {
			logger.warn(e.getMessage());
			index = null;
		}
		return index;
	}

}

