package org.jails.cloner;

public interface Cloner {
	public <T> T deepCopy(T object);
}
