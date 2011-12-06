package org.jails.validation.client;

import org.jails.property.ReflectionUtil;
import org.jails.property.parser.PropertyParser;
import org.jails.property.parser.SimplePropertyParser;
import org.jails.validation.BeanConstraints;
import org.jails.validation.constraint.FieldMatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.metadata.ConstraintDescriptor;
import java.util.List;

public class PositionRelativeValidationConstructor
		implements ClientValidationConstructor {
	private static Logger logger = LoggerFactory.getLogger(PositionRelativeValidationConstructor.class);

	protected PropertyParser propertyParser = new SimplePropertyParser();

	public String getValidationHtml(List<ClientConstraintInfo> clientConstraints,
									Class classType, String property) {
		if (propertyParser.hasNestedProperty(property)) {
			Class nestedClass = ReflectionUtil.getPropertyType(classType, property);
			String nestedProperty = (property.lastIndexOf(".") > 0)
					? property.substring(property.lastIndexOf(".") + 1)
					: property;

			return _getValidationHtml(clientConstraints, nestedClass, nestedProperty);
		} else {
			return _getValidationHtml(clientConstraints, classType, property);
		}
	}

	protected String _getValidationHtml(List<ClientConstraintInfo> clientConstraints,
										Class classType, String property) {
		StringBuffer validationBuffer = null;

		for (ClientConstraintInfo info : clientConstraints) {
			ConstraintDescriptor<?> descriptor = BeanConstraints.getInstance()
					.getConstraint(classType, property, info.getConstraint());
			if (descriptor == null) descriptor = BeanConstraints.getInstance()
					.findConstraint(classType, property, info.getConstraint());
			String validation = null;
			if (descriptor == null) {
				validation = info.getClientValidation();
			} else {
				if (info.getConstraint().equals(FieldMatch.class)) {
					logger.info("Getting Validation for FieldMatch.class");
					String matchField = (String) descriptor.getAttributes().get("matchField");
					logger.info("matchField: " + matchField);
					logger.info("property: " + property);
					if (property.equals(descriptor.getAttributes().get("matchField"))) {
						validation = info.parseClientValidation(descriptor);
					}
				} else {
					validation = info.parseClientValidation(descriptor);
				}
			}

			if (validation != null) {
				if (validationBuffer == null) {
					validationBuffer = new StringBuffer();
					validationBuffer.append(validation);
				} else {
					validationBuffer.append(",").append(validation);
				}
			}
		}
		return (validationBuffer != null)
				? " class=\"validate[" + validationBuffer.toString() + "]\""
				: null;
	}
}
