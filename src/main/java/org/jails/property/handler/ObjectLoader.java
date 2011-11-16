package org.jails.property.handler;

import java.util.Map;

public interface ObjectLoader {

	public <T> T load(Class<T> classType, String field, Object value);

	public <T> T load(Class<T> classType, Map<String,Object> args);
}
