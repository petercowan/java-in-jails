package org.jails.validation;

import org.jails.validation.cloner.Cloner;
import org.jails.validation.cloner.XStreamCloner;
import org.jails.property.Mapper;
import org.jails.property.SimpleMapper;
import org.jails.validation.constraint.RequiredChecks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.groups.Default;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * BeanValidator validates a JaveBean based on the JSR 303  Annotations
 * set in that class. The underlying implemention handled by Hibernate.
 */
public class SimpleValidator {
	private static Logger logger = LoggerFactory.getLogger(SimpleValidator.class);

	private Cloner cloner;
	private Mapper beanMapper;
	private Validator validator;

	public SimpleValidator() {
		this(new XStreamCloner(),new SimpleMapper());
	}

	public SimpleValidator(Mapper beanMapper) {
		this(new XStreamCloner(),beanMapper);
	}

	public SimpleValidator(Cloner cloner, Mapper beanMapper) {
		validator = ValidatorInstance.getInstance().getValidator();
		this.cloner = cloner;
		this.beanMapper = beanMapper;
	}

	/**
	 * <pre>
	 * Validates an objects fields that belong to the given Constraint groups.
	 *
	 * Groups are validated sequentially, such that, if the first group fails,
	 * and exception is thrown and the reast are not validated.
	 *
	 * </pre>
	 *
	 * @param object Object to validate
	 * @param groups Constraint groups to validate
	 */
	public <T> Map<String, List<String>> validate(T object, Class<?>... groups) {
		logger.info("Validating " + object.getClass() + ": " + object.toString());

		for (Class group : groups) {
			Set<ConstraintViolation<T>> constraintViolations =
					validator.validate(object, group);

			if (constraintViolations.size() > 0) {
				return ValidationUtil.getErrorFieldsMap(constraintViolations);
			}
		}
		return null;
	}

	/**
	 * <pre>
	 * Validates an objects fields that belong to our Constraint groups
	 * RequiredChecks.class and Default.class.
	 *
	 * Groups are validated sequentially,
	 * such that, if the first group fails, and exception is thrown and the reast are not validated.
	 * </pre>
	 *
	 * @param object Object to validate
	 *
	 */
	public <T> Map<String, List<String>> validate(T object) {
		return validate(object, RequiredChecks.class, Default.class);
	}

	/**
	 * <pre>
	 * Validates an objects fields that belong to the given Constraint groups,
	 * using values that are contained in the corresponding parameter map.
	 *
	 * Groups are validated sequentially, such that, if the first group fails,
	 * and exception is thrown and the reast are not validated.
	 *
	 * The object passed to this method is never modified.
	 * </pre>
	 *
	 *
	 * @param object Object to validate
	 * @param params properties in the format of the Mapper associated with this class
	 * @param groups Constraint groups to validate
	 * @throws ValidationException containing error messages, if Object is invalid
	 */
	public <T> Map<String, List<String>> validate(T object, Map<String, String[]> params, Class<?>... groups) {
		T copy = cloner.deepCopy(object);
		beanMapper.toExistingObject(copy, params);

		return validate(copy, groups);
	}

	/**
	 * <pre>
	 * Validates an objects fields that belong to our Constraint groups
	 * RequiredChecks.class and Default.class, using values that are contained
	 * in the corresponding parameter map.
	 *
	 * Groups are validated sequentially, such that, if the first group fails,
	 * and exception is thrown and the reast are not validated.
	 *
	 * The object passed to this method is never modified.
	 * </pre>
	 *
	 *
	 * @param object Object to validate
	 * @param params properties in the format of the Mapper associated with this class
	 */
	public <T> Map<String, List<String>> validate(T object, Map<String, String[]> params) {
		return validate(object, params, RequiredChecks.class, Default.class);
	}

	/**
	 * <pre>
	 * Validates an Classes fields that belong to the given Constraint groups,
	 * using values that are contained in the corresponding parameter map.
	 *
	 * Groups are validated sequentially, such that, if the first group fails,
	 * and exception is thrown and the reast are not validated.
	 *
	 * The object passed to this method is never modified.
	 * </pre>
	 *
	 *
	 * @param classType type of object to validate
	 * @param params properties in the format of the Mapper associated with this class
	 * @param groups Constraint groups to validate
	 * @return new T instance of classType
	 */
	public <T> Map<String, List<String>> validate(Class<T> classType, Map<String, String[]> params, Class<?>... groups) {
		T bean = beanMapper.toObject(classType, params);

		return validate(bean, groups);
	}

	/**
	 * <pre>
	 * Validates an Classes fields that belong to our Constraint groups
	 * RequiredChecks.class and Default.class, using values that are contained
	 * in the corresponding parameter map.
	 *
	 * Groups are validated sequentially, such that, if the first group fails,
	 * and exception is thrown and the reast are not validated.
	 *
	 * The object passed to this method is never modified.
	 * </pre>
	 *
	 *
	 * @param classType type of object to validate
	 * @param params properties in the format of the Mapper associated with this class
	 * @return new T instance of classType
	 */
	public <T> Map<String, List<String>> validate(Class<T> classType, Map<String, String[]> params) {
		return validate(classType, params, RequiredChecks.class, Default.class);
	}

	/*
	 * retrieve assocaited Mapper
	 */
	public Mapper getMapper() {
		return beanMapper;
	}
}
