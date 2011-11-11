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
	
	public PropertiesMultiMap toMultiMap(PropertyParser propertyParser) {
		PropertiesMultiMap multiMap = new PropertiesMultiMap();
		int index = 0;
		for (String param : keySet()) {
			Integer propertyIndex = propertyParser.getPropertyIndex(param);

			if (propertyIndex != null) {
				PropertiesMap indexedMap = multiMap.get(propertyIndex);
				if (indexedMap == null) {
					logger.info("adding index map: " + propertyIndex);
					indexedMap = new PropertiesMap();
					multiMap.put(propertyIndex, indexedMap);
				}
				indexedMap.put(propertyParser.getPropertyName(param), get(param));
			}
		}
		return multiMap;
	}
}
