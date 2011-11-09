package org.jails.validation;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Validator;
import javax.validation.metadata.BeanDescriptor;
import javax.validation.metadata.ConstraintDescriptor;
import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BeanConstraints {
	private static Logger logger = LoggerFactory.getLogger(BeanConstraints.class);

	private static BeanConstraints instance;

	private Validator validator;

	private BeanConstraints() {
		validator = ValidatorInstance.getInstance().getValidator();
	}

	public static BeanConstraints getInstance() {
		if (instance == null) instance = new BeanConstraints();
		return instance;
	}

	public Set<ConstraintDescriptor<?>> getConstraints(Class classType) {
		try {
			return validator.getConstraintsForClass(classType)
					.getConstraintDescriptors();
		} catch (Exception e) {
			logger.warn(e.getMessage());
		}
		return Collections.EMPTY_SET;
	}

	public Set<ConstraintDescriptor<?>> getConstraints(Class classType, String propertyName) {
		try {
			BeanDescriptor beanDescriptor = validator.getConstraintsForClass(classType);
			return beanDescriptor.getConstraintsForProperty(propertyName)
					.getConstraintDescriptors();
		} catch (Exception e) {
			logger.warn(e.getMessage());
		}
		return Collections.EMPTY_SET;
	}

	public ConstraintDescriptor<?> getConstraint(Class classType, String propertyName,
														 Class<? extends Annotation> constraintType) {
		Set<ConstraintDescriptor<?>> constraints = getConstraints(classType, propertyName);
		if (constraints != null) {
			for (ConstraintDescriptor constraint : constraints) {
				logger.info("Displaying Constraint: " + constraint.getAnnotation().annotationType());
				if (constraint.getAnnotation().annotationType().equals(constraintType)) {
					return constraint;
				}
			}
		}
		return null;
	}

	public Set<Class<?>> getConstraintGroups(Class classType, String propertyName) {
		Set<Class<?>> groups = new HashSet<Class<?>>();
		Set<ConstraintDescriptor<?>> constraints = getConstraints(classType, propertyName);
		if (constraints != null) {
			for (ConstraintDescriptor constraint : constraints) {
				logger.info("Displaying Constraint: " + constraint.getAnnotation().annotationType());
				if (constraint.getGroups() != null) {
					groups.addAll(constraint.getGroups());
				}
				constraint.getAnnotation();
			}
		}
		return groups;
	}

	public Map<String, Object> getConstraintAttributes(Class classType, String propertyName) {
		Set<ConstraintDescriptor<?>> constraints = getConstraints(classType, propertyName);
		if (constraints != null) {
			for (ConstraintDescriptor constraint : constraints) {
				logger.info("Displaying Constraint: " + constraint.getAnnotation().annotationType());
				if (constraint.getAttributes() != null) {
					return constraint.getAttributes();
				}
			}
		}
		return  Collections.EMPTY_MAP;
	}
}
