package org.jails.validation.cloner;

import com.thoughtworks.xstream.XStream;

public class XStreamCloner implements Cloner {
	private static XStream xStream = new XStream();

	public <T> T deepCopy(T object) {
		T copy = (T) xStream.fromXML(xStream.toXML(object));

		return copy;
	}
}
