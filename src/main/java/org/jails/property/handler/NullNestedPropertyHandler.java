package org.jails.property.handler;

import org.jails.property.parser.PropertyParser;

public interface NullNestedPropertyHandler<T> {
	public void handleProperty(T object, String property, String nestedProperty, String[] valArray, PropertyParser propertyParser);
}
