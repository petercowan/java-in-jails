package org.jails.form;

import org.apache.commons.beanutils.BeanUtils;
import org.jails.property.parser.PropertyParser;
import org.jails.property.parser.SimplePropertyParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jails.util.StringUtil;
import org.jails.validation.BeanConstraints;
import org.jails.validation.RequiredChecks;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SimpleForm<T> {

	private static Logger logger = LoggerFactory.getLogger(SimpleForm.class);

	protected Class classType;
	protected String name;
	protected String identityField;

	protected T[] beanArray;
	protected String[] beanIdentities;
	private List<Map<String, List<String>>> errorFieldMaps = new ArrayList<Map<String, List<String>>>();
	private Integer repeatCount;

	protected PropertyParser propertyParser = new SimplePropertyParser();

	protected SimpleForm() {
	}

	public SimpleForm(Class classType) {
		if (classType == null)
			throw new IllegalArgumentException("classType must not be null");
		this.classType = classType;
		named(classType.getSimpleName());
		validateAs(classType);
	}

	public SimpleForm(T bean) {
		if (bean == null)
			throw new IllegalArgumentException("bean must not be null");
		bindTo(bean);
	}

	public SimpleForm(T[] beanArray) {
		if (beanArray == null || beanArray.length == 0)
			throw new IllegalArgumentException("beanArray may not be null or empty.");
		bindTo(beanArray);
	}

	public SimpleForm<T> bindTo(T formBean) {
		if (classType != null && formBean.getClass() != classType)
			throw new IllegalArgumentException("Binding object must be the same type as the class used to validate this form");
		beanArray = (T[]) Array.newInstance(classType, 1);
		beanArray[0] = formBean;
		validateAs(formBean.getClass());
		if (identityField != null) identifyBy(identityField);
		return this;
	}

	public SimpleForm<T> bindTo(T[] formBeans) {
		if (classType != null && formBeans.length > 0 && formBeans[0].getClass() != classType)
			throw new IllegalArgumentException("Binding object must be the same type as the class used to validate this form");
		if (repeatCount != null && formBeans.length != repeatCount)
			throw new IllegalArgumentException("Repeat count must be the same as the number of form beans");
		this.beanArray = formBeans;
		validateAs(formBeans[0].getClass());
		if (identityField != null) identifyBy(identityField);
		return repeat(beanArray.length);
	}

	public SimpleForm<T> identifyBy(String identityField) {
		this.identityField = identityField;
		if (isBound()) {
			setIdentities(identityField);
		}
		return this;
	}

	protected void setIdentities(String idField) {
		beanIdentities = new String[beanArray.length];
		int index = 0;
		for (T formBean : beanArray) {
			try {
				String beanIdentity = BeanUtils.getProperty(formBean, idField);
				logger.info("Bind object with identity: " + beanIdentity);
				beanIdentities[index++] = beanIdentity;
			} catch (Exception e) {
				throw new IllegalArgumentException("id field cannot be accessed in " + formBean.getClass());
			}
		}
	}

	public SimpleForm<T> repeat(Integer repeatCount) {
		logger.info("repeat form " + repeatCount + " times");
		this.repeatCount = repeatCount;
		return this;
	}

	public SimpleForm<T> inRequest(HttpServletRequest request) {
		String simpleFormParam = (name == null) ? "_form" : "_" + name + "_form";
		logger.info("Setting SimpleForm: " + simpleFormParam);

		request.setAttribute(simpleFormParam, this);
		return this;
	}

	protected void validateAs(Class classType) {
		this.name = StringUtil.toCamelCase(classType.getSimpleName());
		this.classType = classType;
	}

	public SimpleForm<T> named(String name) {
		this.name = StringUtil.toCamelCase(name);
		return this;
	}

	public String getName() {
		return name;
	}

	public Class getClassType() {
		return classType;
	}

	public T getObject(int index) {
		return beanArray[index];
	}

	public Object getObject() {
		return getObject(0);
	}

	public T[] getObjects() {
		return beanArray;
	}

	public String getIdentity(int index) {
		return beanIdentities[index];
	}

	public String getBeanIdentity() {
		return getIdentity(0);
	}

	public String[] getIdentities() {
		return beanIdentities;
	}

	public Integer getTimesToRepeat() {
		return repeatCount;
	}

	public boolean isRepeatable() {
		return  repeatCount > 0;
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

	public Boolean isBound() {
		return beanArray != null && beanArray.length > 0;
	}

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

	public boolean isFieldRequired(String paramName) {
		return isFieldRequired(getObject(0), paramName);
	}

	protected Map<String, List<String>> getErrorFieldMap(int index) {
		if (index <= repeatCount && errorFieldMaps != null) {
			Map<String, List<String>> errorFieldMap = errorFieldMaps.get(index);
			if (errorFieldMap == null) {
				errorFieldMap = new HashMap<String, List<String>>();
				errorFieldMaps.add(errorFieldMap);
			}
			return errorFieldMap;
		}
		return null;
	}

	public void setErrors(Map<String, List<String>> errorFieldMap) {
		errorFieldMaps.set(0, errorFieldMap);
	}

	public void addErrorFieldsMap(Map<String, List<String>> errorFieldMap) {
		errorFieldMaps.add(errorFieldMap);
	}

	public void addError(String fieldName, String errorMessage) {
		addError(fieldName, errorMessage, 0);
	}

	public void addError(String fieldName, String errorMessage, int index) {
		addError(getErrorFieldMap(index), fieldName, errorMessage);
	}

	public boolean hasError() {
		return errorFieldMaps != null
				&& errorFieldMaps.size() > 0
				&& !errorFieldMaps.get(0).isEmpty();
	}

	public boolean fieldHasError(String paramName) {
		return fieldHasError(paramName, 0);
	}

	public boolean fieldHasError(String paramName, int index) {
		if (hasError()) {
			Map<String, List<String>> errorFieldMap = getErrorFieldMap(index);
			boolean fieldHasError = errorFieldMap != null && errorFieldMap.get(paramName) != null;
			logger.info(paramName + " hasError? " + fieldHasError);
			return fieldHasError;
		} else {
			return false;
		}
	}

	public String getFieldError(String paramName, String label) {
		return getFieldError(paramName, label, 0);
	}

	public String getFieldError(String paramName, String label, int index) {
		if (fieldHasError(paramName, index)) {
			StringBuffer errorMessage = new StringBuffer();
			Map<String, List<String>> errorFieldMap = getErrorFieldMap(index);
			if (errorFieldMap != null) {
				for (String error : errorFieldMap.get(paramName)) {
					logger.info(label + ": " + error);
					errorMessage.append(label).append(" ").append(error).append("<br />");
				}
			}
			return errorMessage.toString();
		} else {
			return null;
		}
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

	public String getAction() {
		String baseAction = "/" + StringUtil.flattenCamelCase(name, "_") + "/";
		String action;
		if (beanIdentities != null) {
			if (beanIdentities.length == 0) {
				action = baseAction + getIdentity(0) + "/" + SimpleFormRouter.ACTION_EDIT;
			} else {
				action = baseAction + StringUtil.join(",", beanIdentities)
											+ "/" + SimpleFormRouter.ACTION_EDIT;
			}
		} else {
			action = baseAction + SimpleFormRouter.ACTION_NEW;
		}
		return action;
	}
}
