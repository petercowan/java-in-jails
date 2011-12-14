package org.jails.validation.client;

import org.jails.property.CommonsPropertyUtils;
import org.jails.property.PropertyUtils;
import org.jails.property.parser.PropertyParser;
import org.jails.property.parser.SimplePropertyParser;
import org.jails.util.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class ClientConstraintInfoRegistry<T,U extends ClientConstraintInfo<T,V>, V> {
	private static Logger logger = LoggerFactory.getLogger(ClientConstraintInfoRegistry.class);

	private static ClientConstraintInfoRegistry instance;
	protected Map<T, U> registry;
    protected Set<T> requiredConstraints;
	protected ClientValidationConstructor validationConstructor;
	protected PropertyParser propertyParser;
    protected PropertyUtils propertyUtils;

	protected ClientConstraintInfoRegistry() {
        propertyParser = new SimplePropertyParser();
        propertyUtils = new CommonsPropertyUtils();
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
            Tuple<Class<?>, String> tuple = propertyUtils.getNestedPropertyType(classType, property);
            Class<?> nestedClass = tuple.get0();
			String nestedProperty = tuple.get1();

			return getValidations(nestedClass, nestedProperty);
		} else {
			return getValidations(classType, property);
		}
	}

	protected abstract List<String> getValidations(Class classType, String property);

	public List<U> getClientConstraints(Class classType, String property) {
		if (propertyParser.hasNestedProperty(property)) {
            Tuple<Class<?>, String> tuple = propertyUtils.getNestedPropertyType(classType, property);
            Class<?> nestedClass = tuple.get0();
			String nestedProperty = tuple.get1();

			return getConstraints(nestedClass, nestedProperty);
		} else {
			return getConstraints(classType, property);
		}
	}

	protected abstract List<U> getConstraints(Class classType, String property);

	public ClientValidationConstructor getValidationConstructor() {
		return validationConstructor;
	}

    public boolean isRequired(Class classType, String property) {
        if (propertyParser.hasNestedProperty(property)) {
            Tuple<Class<?>, String> tuple = propertyUtils.getNestedPropertyType(classType, property);
            Class<?> nestedClass = tuple.get0();
			String nestedProperty = tuple.get1();

            return isPropertyRequired(nestedClass, nestedProperty);
        } else {
            return isPropertyRequired(classType, property);
        }
    }

    protected boolean isPropertyRequired(Class classType, String property) {
        List<U> constraintInfoList = getClientConstraints(classType, property);

        for (U constraintInfo: constraintInfoList) {
            if (requiredConstraints.contains(constraintInfo.getConstraint())) {
                return true;
            }
        }
        return false;
    }
}
