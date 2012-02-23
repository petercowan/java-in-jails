package org.jails.property.handler;

public interface ObjectLoader {

	public <T> T load(Class<T> classType, String field, Object value);
}
