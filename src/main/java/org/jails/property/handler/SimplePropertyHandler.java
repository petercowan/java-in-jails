package org.jails.property.handler;

import org.jails.property.Identity;
import org.jails.property.parser.PropertyParser;

public class SimplePropertyHandler
		extends BasePropertyHandler {

	protected NewInstanceObjectHandler newInstanceObjectHandler;
	protected LoadObjectHandler loadObjectHandler;

	public SimplePropertyHandler() {
		newInstanceObjectHandler = new NewInstanceObjectHandler();
	}

	public SimplePropertyHandler(LoadObjectHandler loadObjectHandler) {
		this();
		this.loadObjectHandler = loadObjectHandler;
	}

	@Override
	public Object handleNullNestedProperty(Object object, String property,
										   String nestedProperty, String[] valArray,
										   PropertyParser propertyParser) {
		if (loadObjectHandler != null && Identity.identifyBy(object, property, nestedProperty)) {
			return loadObjectHandler.handleProperty(object, property,
					nestedProperty, valArray, propertyParser);
		} else {
			return newInstanceObjectHandler.handleProperty(object, property,
					nestedProperty, valArray, propertyParser);
		}
	}
}
