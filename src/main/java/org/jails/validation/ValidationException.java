package org.jails.validation;

import java.util.List;
import java.util.Map;

public class ValidationException extends Exception {
	private Map<String,List<String>> errorFields;


	public ValidationException(String message, Map<String, List<String>> errorFields) {
		super(message);
		this.errorFields = errorFields;
	}

	public ValidationException(Exception e, Map<String, List<String>> errorFields) {
		super(e);
		this.errorFields = errorFields;
	}

	public Map<String, List<String>> getErrorFields() {
		return errorFields;
	}
}
