package org.jails.property.handler;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractObjectLoader
		implements ObjectLoader {
	public <T> T load(Class<T> classType, String field, Object value) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(field, value);
		return load(classType, map);
	}
}
