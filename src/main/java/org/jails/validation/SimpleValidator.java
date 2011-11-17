package org.jails.validation;

import org.jails.cloner.Cloner;
import org.jails.cloner.XStreamCloner;
import org.jails.property.Mapper;
import org.jails.property.SimpleMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.groups.Default;
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
	 * @param object Object to validate
	 * @param groups Constraint groups to validate
	 * @param <T> Type of Object
	 * @throws ValidationException containing error messages, if Object is invalid
	 */
	public <T> void validate(T object, Class<?>... groups) throws ValidationException {
		logger.info("Validating " + object.getClass() + ": " + object.toString());

		for (Class group : groups) {
			Set<ConstraintViolation<T>> constraintViolations =
					validator.validate(object, group);

			if (constraintViolations.size() > 0) {
				throw new ValidationException("Validation error",
						ValidationUtil.getErrorFieldsMap(constraintViolations));
			}
		}
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
	 * @param <T> Type of Object
	 * @throws ValidationException containing error messages, if Object is invalid
	 */
	public <T> void validate(T object) throws ValidationException {
		validate(object, RequiredChecks.class, Default.class);
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
	 * @param object Object to validate
	 * @param params properties in the format of the Mapper associated with this class
	 * @param groups Constraint groups to validate
	 * @param <T> Type of Object
	 * @throws ValidationException containing error messages, if Object is invalid
	 */
	public <T> void validate(T object, Map<String, String[]> params, Class<?>... groups)
			throws ValidationException {
		T copy = cloner.deepCopy(object);
		beanMapper.toExistingObject(copy, params);

		validate(copy, groups);
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
	 * @param object Object to validate
	 * @param params properties in the format of the Mapper associated with this class
	 * @param <T> Type of Object
	 * @throws ValidationException containing error messages, if Object is invalid
	 */
	public <T> void validate(T object, Map<String, String[]> params) throws ValidationException {
		validate(object, params, RequiredChecks.class, Default.class);
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
	 * @param classType type of object to validate
	 * @param params properties in the format of the Mapper associated with this class
	 * @param groups Constraint groups to validate
	 * @param <T> Type of Object
	 * @return new T instance of classType
	 * @throws ValidationException containing error messages, if Object is invalid
	 */
	public <T> T validate(Class<T> classType, Map<String, String[]> params, Class<?>... groups)
			throws ValidationException {
		T bean = beanMapper.toObject(classType, params);

		validate(bean, groups);

		return bean;
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
	 * @param classType type of object to validate
	 * @param params properties in the format of the Mapper associated with this class
	 * @param <T> Type of Object
	 * @return new T instance of classType
	 * @throws ValidationException containing error messages, if Object is invalid
	 */
	public <T> T validate(Class<T> classType, Map<String, String[]> params) throws ValidationException {
		return validate(classType, params, RequiredChecks.class, Default.class);
	}

	/*
	 * retrieve assocaited Mapper
	 */
	public Mapper getMapper() {
		return beanMapper;
	}
}
