package org.jails.property;

import org.jails.property.parser.PropertyParser;
import org.jails.property.parser.SimplePropertyParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class PropertiesMultiMap extends LinkedHashMap<Integer, Map<String, String[]>> {
	private static Logger logger = LoggerFactory.getLogger(PropertiesMultiMap.class);

	public static PropertiesMultiMap getMultiMap(Map<String, String[]> m) {
		return toMultiMap(m, new SimplePropertyParser());
	}

	public static PropertiesMultiMap toMultiMap(Map<String, String[]> m, PropertyParser propertyParser) {
		PropertiesMultiMap multiMap = new PropertiesMultiMap();
		Map<String, String[]> globalParams = null;
		int index = 0;
		for (String param : m.keySet()) {
			if (param != null && !param.startsWith("_")) {
				String type = propertyParser.getRootProperty(param);
				Integer propertyIndex = propertyParser.getPropertyIndex(type);

				String[] vals = m.get(param);
				if (propertyIndex != null) {
					Map<String, String[]> indexedMap = multiMap.get(propertyIndex);
					if (indexedMap == null) {
						logger.info("adding index map: " + propertyIndex);
						indexedMap = new HashMap<String, String[]>();
						multiMap.put(propertyIndex, indexedMap);
					}
					indexedMap.put(param, vals);
				} else {
					if  (globalParams == null) globalParams = new HashMap<String, String[]>();
					globalParams.put(param, vals);
				}
			}
		}
		if  (globalParams != null) {
			for (Map<String, String[]> paramMap : multiMap.values()) {
				for (String param : globalParams.keySet()) {
					paramMap.put(param, globalParams.get(param));
				}
			}
		}
		return multiMap;
	}

}
