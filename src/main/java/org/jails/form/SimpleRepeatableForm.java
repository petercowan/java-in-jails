package org.jails.form;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jails.util.StringUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleRepeatableForm<T> extends SimpleForm<T> {
	private static Logger logger = LoggerFactory.getLogger(SimpleRepeatableForm.class);

	protected T[] beanArray;
	protected String[] beanIdentities;
	private Integer repeatCount;
	private List<Map<String, List<String>>> errorFieldMaps = new ArrayList<Map<String, List<String>>>();

	public SimpleRepeatableForm() {
		super();
	}

	public SimpleRepeatableForm(Class classType) {
		super(classType);
	}

	public SimpleRepeatableForm(T[] beanArray) {
		if (beanArray == null || beanArray.length == 0)
			throw new IllegalArgumentException("beanArray may not be null or empty.");
		bindTo(beanArray);
	}

	public SimpleRepeatableForm<T> bindTo(T[] formBeans) {
		if (classType != null && formBeans.length > 0 && formBeans[0].getClass() != classType)
			throw new IllegalArgumentException("Binding object must be the same type as the class used to validate this form");
		if (repeatCount != null && formBeans.length != repeatCount)
			throw new IllegalArgumentException("Repeat count must be the same as the number of form beans");
		this.beanArray = formBeans;
		validateWith(formBeans[0].getClass());
		if (identityField != null) identifyWith(identityField);
		return repeatTimes(beanArray.length);
	}

	@Override
	public SimpleRepeatableForm<T> identifyWith(String identityField) {
		return (SimpleRepeatableForm) super.identifyWith(identityField);
	}

	protected void setIdentity(String idField) {
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

	@Override
	public SimpleRepeatableForm<T> setFormInRequest(HttpServletRequest request) {
		return (SimpleRepeatableForm) super.setFormInRequest(request);
	}

	@Override
	public SimpleRepeatableForm<T> usingName(String name) {
		return (SimpleRepeatableForm) super.usingName(name);
	}

	public SimpleRepeatableForm<T> repeatTimes(Integer repeatCount) {
		logger.info("repeat form " + repeatCount + " times");
		this.repeatCount = repeatCount;
		return this;
	}

	public T[] getBeanArray() {
		return beanArray;
	}

	public T getBean(int index) {
		return beanArray[index];
	}

	public String[] getBeanIdentities() {
		return beanIdentities;
	}

	public Integer getTimesToRepeat() {
		return repeatCount;
	}

	public void addErrorFieldsMap(Map<String, List<String>> errorFieldMap) {
		errorFieldMaps.add(errorFieldMap);
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

	public void addError(String fieldName, String errorMessage, int index) {
		addError(getErrorFieldMap(index), fieldName, errorMessage);
	}

	@Override
	public boolean hasError() {
		return errorFieldMaps != null && errorFieldMaps.size() > 0;
	}

	@Override
	public boolean isFieldRequired(String paramName) {
		return isFieldRequired(getBean(0), paramName);
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

	public String getAction() {
		String baseAction = "/" + StringUtil.flattenCamelCase(name, "_") + "/";
		String action = (beanIdentities != null && beanIdentities.length > 0)
				? baseAction + StringUtil.join(
				",", beanIdentities)
				+ "/" + SimpleFormRequest.ACTION_EDIT
				: baseAction + SimpleFormRequest.ACTION_NEW;
		return action;
	}

	@Override
	public Boolean isBoundToObject() {
		return beanArray != null && beanArray.length > 0;
	}
}
