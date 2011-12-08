package org.jails.validation.cloner;

public interface Cloner {
	public <T> T deepCopy(T object);
}
