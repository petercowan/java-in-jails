package org.jails.property;

import org.jails.property.handler.BasePropertyHandler;
import org.jails.property.handler.NullNestedPropertyHandler;
import org.jails.property.handler.SimplePropertyHandler;
import org.jails.property.parser.SimplePropertyParser;

public class SimpleMapper extends Mapper {
	public SimpleMapper() {
		super(new SimplePropertyParser(), new SimplePropertyHandler());
	}

	public SimpleMapper(NullNestedPropertyHandler propertyHandler) {
		super(new SimplePropertyParser(), new BasePropertyHandler(propertyHandler));
	}
}
