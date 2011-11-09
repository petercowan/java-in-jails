package org.jails.property.handler;

public class SimplePropertyHandler
	extends BasePropertyHandler<Object> {

	public SimplePropertyHandler() {
		super(new NewInstanceNullNestedPropertyHandler());
	}
}
