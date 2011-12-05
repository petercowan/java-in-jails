package org.jails.form;

import org.apache.commons.beanutils.BeanUtils;
import org.jails.form.controller.SimpleFormRouter;
import org.jails.property.IdentifyBy;
import org.jails.property.Identity;
import org.jails.property.ReflectionUtil;
import org.jails.property.parser.PropertyParser;
import org.jails.property.parser.SimplePropertyParser;
import org.jails.util.Strings;
import org.jails.validation.BeanConstraints;
import org.jails.validation.RequiredChecks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AbstractFormBuilder<T> extends SimpleForm<T> {
	private static Logger logger = LoggerFactory.getLogger(AbstractFormBuilder.class);

	protected Class classType;
	protected String name;
	protected String identityField;

	protected T[] beanArray;
	protected String[] beanIdentities;
	protected List<Map<String, List<String>>> errorFieldMaps = new ArrayList<Map<String, List<String>>>();
	protected int repeatCount = 0;

	protected PropertyParser propertyParser = new SimplePropertyParser();

	protected SimpleForm<T> _bindTo(T... objects) {
		if (objects == null || objects.length == 0)
			throw new IllegalArgumentException("beanArray may not be null or empty.");
		if (classType != null && objects.length > 0 && objects[0].getClass() != classType)
			throw new IllegalArgumentException("Binding object must be the same type as the class used to validate this form");
		if (repeatCount > 0 && objects.length != repeatCount)
			throw new IllegalArgumentException("Repeat count must be the same as the number of form beans");
		this.beanArray = objects;
		_validateAs(beanArray[0].getClass());
		identify();
		if (beanArray.length > 1) repeat(beanArray.length);
		return this;
	}

	protected void identify() {
		IdentifyBy[] ids = Identity.getIdentifiers(beanArray[0]);
		if (ids != null && ids.length == 1) {
			identifyBy(ids[0].field());
		}
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

	protected void _validateAs(Class classType) {
		if (classType == null)
			throw new IllegalArgumentException("classType must not be null");

		if (this.name == null) named(classType.getSimpleName());
		this.classType = classType;
	}

	public SimpleForm<T> named(String name) {
		this.name = Strings.toCamelCase(name);
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

	public Boolean isBound() {
		return beanArray != null && beanArray.length > 0;
	}

	public String getIdentity(int index) {
		return beanIdentities[index];
	}

	public String getIdentity() {
		return getIdentity(0);
	}

	public String[] getIdentities() {
		return beanIdentities;
	}

	public Integer getTimesToRepeat() {
		return repeatCount;
	}

	public boolean isRepeatable() {
		return repeatCount > 0;
	}

	//todo - should paramName take full parameter, or just attribute name? (formName.field vs field)
	public boolean isFieldRequired(String paramName) {
		if (propertyParser.hasNestedProperty(paramName)) {

			Class nestedClass = ReflectionUtil.getPropertyType(classType, paramName);
			String nestedParam = (paramName.lastIndexOf(".") > 0)
					? paramName.substring(paramName.lastIndexOf(".") + 1)
					: paramName;

			return isFieldRequired(nestedClass, nestedParam);
		} else {
			return isFieldRequired(classType, paramName);
		}
	}

	protected boolean isFieldRequired(Class type, String paramName) {
		logger.info("isFieldRequired? " + paramName + " of " + type);

		Set<Class<?>> constraints = BeanConstraints.getInstance()
				.getConstraintGroups(type, paramName);
		if (constraints != null) {
			for (Class<?> group : constraints) {
				logger.trace("Checking group " + group);
				logger.trace("Required group " + RequiredChecks.class);
				if (RequiredChecks.class.equals(group)) {
					logger.debug(paramName + " isRequired");
					return true;
				}
			}
		}
		return false;
	}

	protected Map<String, List<String>> getErrorFieldMap(int index) {
		logger.info("Getting error map " + index + " of " + repeatCount);
		if (index <= repeatCount && errorFieldMaps != null) {
			Map<String, List<String>> errorFieldMap = errorFieldMaps.get(index);
			if (errorFieldMap == null) {
				logger.info("New error map");
				errorFieldMap = new HashMap<String, List<String>>();
				errorFieldMaps.add(errorFieldMap);
			}
			logger.info("Got error map");
			return errorFieldMap;
		}
		return null;
	}

	public void setErrors(Map<String, List<String>> errorFieldMap) {
		errorFieldMaps.set(0, errorFieldMap);
	}

	public void addErrors(Map<String, List<String>> errorFieldMap) {
		errorFieldMaps.add(errorFieldMap);
	}

	public void addError(String paramName, String errorMessage) {
		addError(paramName, errorMessage, 0);
	}

	public void addError(String paramName, String errorMessage, int index) {
		addError(getErrorFieldMap(index), paramName, errorMessage);
	}

	public boolean hasError() {
		boolean hasError = errorFieldMaps != null
				&& errorFieldMaps.size() > 0
				&& !errorFieldMaps.get(0).isEmpty();
		if (hasError) logger.info("Form has error");
		return hasError;
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
			logger.info("Field error not found");
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
		String baseAction = "/" + Strings.flattenCamelCase(name, "_") + "/";
		String action;
		if (beanIdentities != null) {
			if (beanIdentities.length == 0) {
				action = baseAction + getIdentity(0) + "/" + SimpleFormRouter.ACTION_EDIT;
			} else {
				action = baseAction + Strings.join(",", beanIdentities)
						+ "/" + SimpleFormRouter.ACTION_EDIT;
			}
		} else {
			action = baseAction + SimpleFormRouter.ACTION_NEW;
		}
		return action;
	}

}
