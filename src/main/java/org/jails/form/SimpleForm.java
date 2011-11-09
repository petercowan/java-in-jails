package org.jails.form;

import org.jails.property.parser.PropertyParser;
import org.jails.property.parser.SimplePropertyParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jails.util.StringUtil;
import org.jails.validation.BeanConstraints;
import org.jails.validation.RequiredChecks;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class SimpleForm<T> {

	private static Logger logger = LoggerFactory.getLogger(SimpleForm.class);

	protected Class classType;
	protected String name;
	protected String identityField;
	protected HttpServletRequest request;
	protected PropertyParser propertyParser = new SimplePropertyParser();
	protected SimpleForm() {
	}

	protected SimpleForm(Class classType) {
		if (classType == null)
			throw new IllegalArgumentException("classType must not be null");
		this.classType = classType;
		usingName(classType.getSimpleName());
	}

	protected SimpleForm(T bean) {
		if (bean == null)
			throw new IllegalArgumentException("bean must not be null");
		this.classType = bean.getClass();
		usingName(classType.getSimpleName());
	}

	public SimpleForm<T> identifyWith(String identityField) {
		this.identityField = identityField;
		if (isBoundToObject()) setIdentity(identityField);
		return this;
	}

	protected abstract void setIdentity(String idField);

	public SimpleForm<T> setFormInRequest(HttpServletRequest request) {
		this.request = request;
		String simpleFormParam = (name == null) ? "_form" : "_" + name + "_form";
		logger.info("Setting SimpleForm: " + simpleFormParam);

		this.request.setAttribute(simpleFormParam, this);
		return this;
	}

	protected void validateWith(Class classType) {
		this.name = StringUtil.toCamelCase(classType.getSimpleName());
		this.classType = classType;
	}

	public SimpleForm<T> usingName(String name) {
		this.name = StringUtil.toCamelCase(name);
		return this;
	}

	public abstract boolean hasError();

	public String getName() {
		return name;
	}

	public Class getClassType() {
		return classType;
	}

	public String getParameterName(String property) {
		return property;
		//return "_" + name + "." + property;
	}

	public String getIndexedParameterName(String property, Integer index) {
		return propertyParser.getRootProperty(property) + "[" + index + "]" + propertyParser.getNestedProperty(property);
		//return "_" + name + "[" + index + "]" + "." + property;
	}

	public String getMetaParameterName(String property) {
		return "_" + name + "_" + property;
		//return "_" + name + "_" + property;
	}

	public abstract String getAction();

	public abstract Boolean isBoundToObject();

	protected boolean isFieldRequired(T bean, String paramName) {
		if (bean == null) return false;
		logger.info("isFieldRequired? " + paramName + " of " + bean.getClass());

		Set<Class<?>> constraints = BeanConstraints.getInstance()
				.getConstraintGroups(classType, paramName);
		if (constraints != null) {
			for (Class<?> group : constraints) {
				logger.info("Checking group " + group);
				if (RequiredChecks.class.equals(group)) return true;
			}
		}
		return false;
	}

	protected void addError(Map<String, List<String>> errorFieldMap, String fieldName, String errorMessage) {
		if (errorFieldMap != null) {
			List<String> fieldErrors = errorFieldMap.get(fieldName);
			if (fieldErrors == null) {
				fieldErrors = new ArrayList<String>();
				errorFieldMap.put(fieldName, fieldErrors);
			}
			fieldErrors.add(errorMessage);
		}
	}

	public abstract boolean isFieldRequired(String paramName);
}