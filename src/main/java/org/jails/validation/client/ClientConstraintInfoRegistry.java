package org.jails.validation.client;

import org.jails.property.ReflectionUtil;
import org.jails.property.parser.PropertyParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public abstract class ClientConstraintInfoRegistry<T,U extends ClientConstraintInfo> {
	private static Logger logger = LoggerFactory.getLogger(ClientConstraintInfoRegistry.class);

	private static ClientConstraintInfoRegistry instance;
	protected Map<T, U> registry;
	protected ClientValidationConstructor validationConstructor;
	protected PropertyParser propertyParser;

	protected ClientConstraintInfoRegistry() {
	}

	public void addClientConstraint(T constraint,
									String clientFunction, String... attributeNames) {
		U constriaintInfo = registry.get(constraint);
		if (constriaintInfo == null) {
			constriaintInfo = newClientConstraint(constraint, clientFunction, attributeNames);
			registry.put(constraint, constriaintInfo);
		}
	}

	protected abstract U newClientConstraint(T constraint, String clientFunction, String... attributeNames);

	public void addClientConstraint(T constraint,
									String clientFunction) {
		addClientConstraint(constraint, clientFunction, null);
	}

	public U getClientConstraint(T constraint) {
		return registry.get(constraint);
	}

	public List<String> getClientValidations(Class classType, String property) {
		if (propertyParser.hasNestedProperty(property)) {
			Class nestedClass = ReflectionUtil.getPropertyType(classType, property);
			String nestedProperty = (property.lastIndexOf(".") > 0)
					? property.substring(property.lastIndexOf(".") + 1)
					: property;

			return _getClientValidations(nestedClass, nestedProperty);
		} else {
			return _getClientValidations(classType, property);
		}
	}

	protected abstract List<String> _getClientValidations(Class classType, String property);

	public List<U> getClientConstraints(Class classType, String property) {
		if (propertyParser.hasNestedProperty(property)) {
			Class nestedClass = ReflectionUtil.getPropertyType(classType, property);
			String nestedProperty = (property.lastIndexOf(".") > 0)
					? property.substring(property.lastIndexOf(".") + 1)
					: property;

			return _getClientConstraints(nestedClass, nestedProperty);
		} else {
			return _getClientConstraints(classType, property);
		}
	}

	protected abstract List<U> _getClientConstraints(Class classType, String property);

	public ClientValidationConstructor getValidationConstructor() {
		return validationConstructor;
	}

}
