package org.jails.validation;


import org.jails.validation.constraint.FieldMatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Validator;
import javax.validation.metadata.BeanDescriptor;
import javax.validation.metadata.ConstraintDescriptor;
import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.HashSet;
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

	/**
	 * Find class constraints that contain this property
	 *
	 * @param classType
	 * @param propertyName
	 * @return
	 */
	public ConstraintDescriptor<?> findConstraint(Class classType, String propertyName,
														 Class<? extends Annotation> constraintType) {
		try {
			Set<ConstraintDescriptor<?>> constraints = findConstraints(classType, propertyName);
			for (ConstraintDescriptor<?> constraint : constraints) {
				if (constraint.getAnnotation().annotationType().equals(constraintType)) {
					logger.info("Found Class constriant " + constraintType + " for " + propertyName);
					return constraint;
				}
			}
		} catch (Exception e) {
			logger.warn(e.getMessage());
		}
		return null;
	}

	/**
	 * Find class constraints that contain this property
	 *
	 * @param classType
	 * @param propertyName
	 * @return
	 */
	public Set<ConstraintDescriptor<?>> findConstraints(Class classType, String propertyName) {
		try {
			Set<ConstraintDescriptor<?>> classConstraints = validator
					.getConstraintsForClass(classType).getConstraintDescriptors();
			return handleClassConstraints(classConstraints, propertyName);
		} catch (Exception e) {
			logger.warn(e.getMessage());
		}
		return Collections.EMPTY_SET;
	}

	private Set<ConstraintDescriptor<?>> handleClassConstraints(Set<ConstraintDescriptor<?>> classConstraints,
																String propertyName) {
		Set<ConstraintDescriptor<?>> constraints = new HashSet();
		for (ConstraintDescriptor<?> constraint : classConstraints) {
			logger.info("Handling " + constraint.getAnnotation().annotationType());
			if (constraint.getAnnotation().annotationType().equals(FieldMatch.class)) {
				String matchField = (String) constraint.getAttributes().get("matchField");
				logger.info("matchField: " + matchField);
				if (matchField != null && matchField.equals(propertyName)) {
					logger.info("Added FieldMatch constraint to " + propertyName);
					constraints.add(constraint);
				}
			}
		}
		return constraints;
	}

	public Set<ConstraintDescriptor<?>> getConstraints(Class classType, String propertyName) {
		try {
			BeanDescriptor beanDescriptor = validator.getConstraintsForClass(classType);
			Set<ConstraintDescriptor<?>> propertyConstraints = beanDescriptor.getConstraintsForProperty(propertyName)
					.getConstraintDescriptors();
			return propertyConstraints;
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
				if (constraint.getAnnotation().annotationType().equals(constraintType)) {
					logger.info("Found Constraint: " + constraint.getAnnotation().annotationType());
					return constraint;
				}
			}
		}
		logger.info("Constraint " + constraintType.getSimpleName() + " for " + classType.getSimpleName() + ", " + propertyName + " not found");
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

}
