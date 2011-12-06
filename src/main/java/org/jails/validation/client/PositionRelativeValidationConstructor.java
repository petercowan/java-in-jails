package org.jails.validation.client;

import org.jails.property.ReflectionUtil;
import org.jails.property.parser.PropertyParser;
import org.jails.property.parser.SimplePropertyParser;
import org.jails.validation.BeanConstraints;

import javax.validation.metadata.ConstraintDescriptor;
import java.util.List;

public class PositionRelativeValidationConstructor
		implements ClientValidationConstructor {
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
			String validation;
			if (descriptor == null) {
				validation = info.getClientValidation();

			} else {
				validation = info.parseClientValidation(descriptor);
			}

			if (validationBuffer == null) {
				validationBuffer = new StringBuffer();
				validationBuffer.append(validation);
			} else {
				validationBuffer.append(",").append(validation);
			}
		}
		return (validationBuffer != null)
				? " class=\"validate[" + validationBuffer.toString() + "]\""
				: null;
	}
}
