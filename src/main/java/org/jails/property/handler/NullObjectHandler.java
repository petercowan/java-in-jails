package org.jails.property.handler;

import org.jails.property.parser.PropertyParser;

public interface NullObjectHandler {
	public Object handleProperty(Object object, String property, String nestedProperty, String[] valArray, PropertyParser propertyParser);
}
