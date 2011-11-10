package org.jails.form;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public abstract class SimpleForm<T> {
	private static Logger logger = LoggerFactory.getLogger(SimpleForm.class);

	protected SimpleForm() {
	}

	public static <T> SimpleForm<T> validateAs(Class<T> classType) {
		return new SimpleFormBuilder<T>(classType);
	}

	public static <T> SimpleForm<T> bindTo(T... objects) {
		return new SimpleFormBuilder<T>(objects);
	}

	public static <T> SimpleForm<T> fromRequest(HttpServletRequest request, Class<T> classType, String name) {
		return (SimpleForm<T>) request.getAttribute("_" + name + "_form");
	}

	public static <T> SimpleForm<T> fromRequest(HttpServletRequest request, Class<T> classType) {
		return fromRequest(request, classType, classType.getSimpleName());
	}

	public abstract SimpleForm<T> named(String name);

	public abstract SimpleForm<T> identifyBy(String identityField);

	public abstract SimpleForm<T> repeat(Integer repeatCount);

	public abstract SimpleForm<T> inRequest(HttpServletRequest request);

	public abstract String getName();

	public abstract Class getClassType();

	public abstract T getObject(int index);

	public abstract Object getObject();

	public abstract T[] getObjects();

	public abstract Boolean isBound();

	public abstract String getIdentity(int index);

	public abstract String getBeanIdentity();

	public abstract String[] getIdentities();

	public abstract Integer getTimesToRepeat();

	public abstract boolean isRepeatable();

	public abstract boolean isFieldRequired(String paramName);

	public abstract void setErrors(Map<String, List<String>> errorFieldMap);

	public abstract void addErrorFieldsMap(Map<String, List<String>> errorFieldMap);

	public abstract void addError(String fieldName, String errorMessage);

	public abstract void addError(String fieldName, String errorMessage, int index);

	public abstract boolean hasError();

	public abstract boolean fieldHasError(String paramName);

	public abstract boolean fieldHasError(String paramName, int index);

	public abstract String getFieldError(String paramName, String label);

	public abstract String getFieldError(String paramName, String label, int index);

	public abstract String getAction();
}
