package org.jails.form;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jails.util.StringUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleBeanForm<T> extends SimpleForm<T> {
	private static Logger logger = LoggerFactory.getLogger(SimpleBeanForm.class);

	protected T bean;
	protected String beanIdentity;
	protected Map<String, List<String>> errorFieldMap;

	public SimpleBeanForm() {
		super();
	}

	public SimpleBeanForm(Class classType) {
		super(classType);
	}

	public SimpleBeanForm(T bean) {
		super(bean);
		bindTo(bean);
	}

	public SimpleBeanForm<T> bindTo(T formBean) {
		if (classType != null && formBean.getClass() != classType)
			throw new IllegalArgumentException("Binding object must be the same type as the class used to validate this form");
		this.bean = formBean;
		validateWith(bean.getClass());
		if (identityField != null) identifyWith(identityField);
		return this;
	}

	@Override
	public SimpleBeanForm<T> identifyWith(String identityField) {
		setIdentity(identityField);
		return (SimpleBeanForm) super.identifyWith(identityField);
	}

	@Override
	protected void setIdentity(String idField) {
		try {
			this.beanIdentity = BeanUtils.getProperty(bean, idField);
		} catch (Exception e) {
			throw new IllegalArgumentException("id field cannot be accessed in " + bean.getClass());
		}
		logger.info("Bind object with identity: " + beanIdentity);
	}

	@Override
	public SimpleBeanForm<T> setFormInRequest(HttpServletRequest request) {
		return (SimpleBeanForm) super.setFormInRequest(request);
	}

	@Override
	public SimpleBeanForm<T> usingName(String name) {
		return (SimpleBeanForm) super.usingName(name);
	}

	public Object getBean() {
		return bean;
	}

	public String getBeanIdentity() {
		return beanIdentity;
	}

	public void setErrorFieldMap(Map<String, List<String>> errorFieldMap) {
		this.errorFieldMap = errorFieldMap;
	}

	public void addError(String fieldName, String errorMessage) {
		if (errorFieldMap == null) errorFieldMap = new HashMap<String, List<String>>();
		addError(errorFieldMap, fieldName, errorMessage);
	}

	@Override
	public boolean hasError() {
		return errorFieldMap != null && errorFieldMap.size() > 0;
	}

	@Override
	public boolean isFieldRequired(String paramName) {
		return isFieldRequired(bean, paramName);
	}

	public boolean fieldHasError(String paramName) {
		boolean hasError = errorFieldMap != null && errorFieldMap.get(paramName) != null;
		logger.info(paramName + " hasError? " + hasError);
		return hasError;
	}

	public String getFieldError(String paramName, String label) {
		if (fieldHasError(paramName)) {
			StringBuffer errorMessage = new StringBuffer();
			for (String error : errorFieldMap.get(paramName)) {
				logger.info(label + ": " + error);
				errorMessage.append(label).append(" ").append(error).append("<br />");
			}
			return errorMessage.toString();
		} else {
			return null;
		}
	}

	public String getAction() {
		String baseAction = "/" + StringUtil.flattenCamelCase(name, "_") + "/";
		String action = (beanIdentity != null)
				? baseAction + beanIdentity + "/" + SimpleFormRequest.ACTION_EDIT
				: baseAction + SimpleFormRequest.ACTION_NEW;
		return action;
	}

	@Override
	public Boolean isBoundToObject() {
		return bean != null;
	}
}
