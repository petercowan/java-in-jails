package org.jails.validation.client;

import org.jails.property.PropertyUtils;
import org.jails.property.parser.PropertyParser;

import java.util.List;

public abstract class AbstractClientValidationConstructor<T extends ClientConstraintInfo>
		implements ClientValidationConstructor<T>{
	protected PropertyParser propertyParser;
	protected PropertyUtils propertyUtils;

	protected AbstractClientValidationConstructor(PropertyParser propertyParser, PropertyUtils propertyUtils) {
		this.propertyParser = propertyParser;
		this.propertyUtils = propertyUtils;
	}

	public String getValidationHtml(List<T> clientConstraints, Class classType, String property) {
		if (propertyParser.hasNestedProperty(property)) {
			Class nestedClass = propertyUtils.getPropertyType(classType, property);
			String nestedProperty = (property.lastIndexOf(".") > 0)
					? property.substring(property.lastIndexOf(".") + 1)
					: property;

			return _getValidationHtml(clientConstraints, nestedClass, nestedProperty);
		} else {
			return _getValidationHtml(clientConstraints, classType, property);
		}
	}

	protected abstract String _getValidationHtml(List<T> clientConstraints,
										Class classType, String property);
}
