package org.jails.property.handler;

import org.jails.property.parser.PropertyParser;

public interface PropertyHandler {

	public  boolean acceptsNestedProperties(Object object, String property);

	public Object handleNullNestedProperty(Object object, String property, String nestedProperty, String[] valArray, PropertyParser propertyParser);

}