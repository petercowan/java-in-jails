package org.jails.validation;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ValidationException extends Exception {
	private Map<Integer, Map<String, List<String>>> errorFieldsMap = new LinkedHashMap<Integer, Map<String, List<String>>>();

	public ValidationException(String message, Map<String, List<String>> errorFields) {
		super(message);
		this.errorFieldsMap.put(0, errorFields);
	}

	public ValidationException(Exception e, Map<String, List<String>> errorFields) {
		super(e);
		this.errorFieldsMap.put(0, errorFields);
	}

	public Map<String, List<String>> getErrorFields(Integer index) {
		return errorFieldsMap.get(index);
	}

	public Map<String, List<String>> getErrorFields() {
		return errorFieldsMap.get(0);
	}

	public Map<Integer, Map<String, List<String>>> getErrorFieldsMap() {
		return errorFieldsMap;
	}

	public void addErrorFields(Map<String, List<String>> errorFields) {
		this.errorFieldsMap.put(errorFields.size(), errorFields);
	}
}
