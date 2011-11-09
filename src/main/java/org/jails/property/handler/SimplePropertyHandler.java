package org.jails.property.handler;

public class SimplePropertyHandler
	extends BasePropertyHandler {

	public SimplePropertyHandler() {
		super(new NewInstanceNullNestedPropertyHandler());
	}
}
