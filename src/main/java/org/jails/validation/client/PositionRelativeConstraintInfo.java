package org.jails.validation.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jails.validation.ValidationUtil;

import javax.validation.metadata.ConstraintDescriptor;
import java.lang.annotation.Annotation;
import java.util.Map;

public class PositionRelativeConstraintInfo
		implements ClientConstraintInfo {
	private static Logger logger = LoggerFactory.getLogger(PositionRelativeConstraintInfo.class);

	private Class<? extends Annotation> constraint;
	private String clientValidation;
	private String[] attributeNames;

	public PositionRelativeConstraintInfo(Class<? extends Annotation> constraint, String clientValidation, String... attributeNames) {
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

	public Class<? extends Annotation> getConstraint() {
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

	public String parseClientValidation(ConstraintDescriptor constraint) {
		if (constraint == null) return "";
		String validation;
		Map<String, Object> attributes = constraint.getAttributes();
		if (hasAttributes() && attributes != null
					&& attributeCount() <= attributes.size()) {
				validation = parseClientValidation(attributes);
		} else {
			 validation = getClientValidation();
		}
		return validation;
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


