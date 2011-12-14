package org.jails.validation.client;

import org.jails.validation.ValidationUtil;
import org.jails.validation.client.jsr303.Jsr303ClientConstraintInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public abstract class AbstractClientConstraintInfo<T,V>
		implements ClientConstraintInfo<T,V> {
	private static Logger logger = LoggerFactory.getLogger(Jsr303ClientConstraintInfo.class);

	protected T constraint;
	protected String clientValidation;
	protected String[] attributeNames;

	public AbstractClientConstraintInfo(T constraint, String clientValidation, String... attributeNames) {
		if (constraint == null) {
			throw new IllegalArgumentException("constraint must not be null");
		}
		if (clientValidation == null) {
			throw new IllegalArgumentException("clientValidation must not be null");
		}
		this.constraint = constraint;
		this.clientValidation = clientValidation;
		this.attributeNames = attributeNames;
	}

	public T getConstraint() {
		return constraint;
	}

	public String getClientValidation() {
		logger.info("clientValidation: " + clientValidation);
		return clientValidation;
	}

	public String[] getAttributeNames() {
		return attributeNames;
	}

	public boolean hasAttributes() {
		return attributeNames != null && attributeNames.length > 0;
	}

	public int attributeCount() {
		return (attributeNames != null) ? attributeNames.length : 0;
	}

	protected String parseClientValidation(Map<String, Object> attributeValues) {
		if (attributeValues == null || attributeNames == null || attributeValues.size() < attributeNames.length) {
			throw new IllegalArgumentException("attributeValues must be the same length as attributeNames");
		}
		for (String key : attributeValues.keySet()) {
			logger.trace(key + ": " + attributeValues.get(key));
		}
		String parsedValidation = ValidationUtil.replaceTokens(clientValidation, attributeValues);
		logger.info("parsedValidation: " + parsedValidation);

		return parsedValidation;
	}

}
