package org.jails.property.handler;

import org.jails.property.parser.PropertyParser;

public interface PropertyHandler<T> {

	public <T> boolean acceptsNestedProperties(T object, String property);

	public void handleNullNestedProperty(T object, String property, String nestedProperty, String[] valArray, PropertyParser propertyParser);

}