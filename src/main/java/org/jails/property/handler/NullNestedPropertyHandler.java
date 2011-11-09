package org.jails.property.handler;

import org.jails.property.parser.PropertyParser;

public interface NullNestedPropertyHandler {
	public void handleProperty(Object object, String property, String nestedProperty, String[] valArray, PropertyParser propertyParser);
}
