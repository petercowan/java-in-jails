package org.jails.property;

import org.jails.property.parser.PropertyParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class PropertiesMap extends HashMap<String, String[]> {
	private static Logger logger = LoggerFactory.getLogger(Mapper.class);

	public PropertiesMap() {
		super();
	}

	public PropertiesMap(int initialCapacity, float loadFactor) {
		super(initialCapacity, loadFactor);
	}

	public PropertiesMap(int initialCapacity) {
		super(initialCapacity);
	}

	public PropertiesMap(Map<? extends String, ? extends String[]> m) {
		super(m);
	}

	public void renameKey(String oldKey, String newKey) {
		String[] value = remove(oldKey);
		put(newKey, value);
	}

	public void put(String key, String value) {
		put(key, new String[]{value});
	}

	public void update(String key, String[] newValues) {
		put(key, newValues);
	}

	public void update(String key, String newValue) {
		put(key, newValue);
	}

	public PropertiesMultiMap toMultiMap(PropertyParser propertyParser) {
		PropertiesMultiMap multiMap = new PropertiesMultiMap();
		PropertiesMap globalParams = null;
		int index = 0;
		for (String param : keySet()) {
			if (param != null && !param.startsWith("_")) {
				String type = propertyParser.getRootProperty(param);
				Integer propertyIndex = propertyParser.getPropertyIndex(type);

				String[] vals = get(param);
				if (propertyIndex != null) {
					PropertiesMap indexedMap = multiMap.get(propertyIndex);
					if (indexedMap == null) {
						logger.info("adding index map: " + propertyIndex);
						indexedMap = new PropertiesMap();
						multiMap.put(propertyIndex, indexedMap);
					}
					indexedMap.put(param, vals);
				} else {
					if  (globalParams == null) globalParams = new PropertiesMap();
					globalParams.put(param, vals);
				}
			}
		}
		if  (globalParams != null) {
			for (PropertiesMap paramMap : multiMap.values()) {
				for (String param : globalParams.keySet()) {
					paramMap.put(param, globalParams.get(param));
				}
			}
		}
		return multiMap;
	}
}
