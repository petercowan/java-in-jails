package org.jails.property;

import org.jails.property.parser.SimplePropertyParser;

public class SimpleBeanToMap extends BeanToMap {
	public SimpleBeanToMap() {
		super(new SimplePropertyParser());
	}
}
