package org.jails.property;

import org.jails.property.handler.BasePropertyHandler;
import org.jails.property.handler.NullNestedPropertyHandler;
import org.jails.property.handler.SimplePropertyHandler;
import org.jails.property.parser.SimplePropertyParser;

public class SimpleMapToBean extends MapToBean {
	public SimpleMapToBean() {
		super(new SimplePropertyParser(), new SimplePropertyHandler());
	}

	public SimpleMapToBean(NullNestedPropertyHandler propertyHandler) {
		super(new SimplePropertyParser(), new BasePropertyHandler(propertyHandler));
	}
}
