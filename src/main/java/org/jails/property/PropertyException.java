package org.jails.property;

public class PropertyException
	extends Exception {
	public PropertyException() {
	}

	public PropertyException(String message) {
		super(message);
	}

	public PropertyException(String message, Throwable cause) {
		super(message, cause);
	}

	public PropertyException(Throwable cause) {
		super(cause);
	}
}
