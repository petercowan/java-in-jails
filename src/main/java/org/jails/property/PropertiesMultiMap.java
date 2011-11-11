package org.jails.property;

import org.jails.property.parser.SimplePropertyParser;

import java.util.LinkedHashMap;
import java.util.Map;

public class PropertiesMultiMap extends LinkedHashMap<Integer, PropertiesMap> {
	public static PropertiesMultiMap getMultiMap(Map<String, String[]> m) {
		if (!(m instanceof PropertiesMap)) {
			m = new PropertiesMap(m);
		}
		return ((PropertiesMap)m).toMultiMap(new SimplePropertyParser());
	}
}
