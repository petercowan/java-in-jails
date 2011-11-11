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
 * BeanValidator validates a JaveBean based on the javax.validation Annotations
 * set in that class
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

	public <T> void validate(T bean, Class<?>... groups) throws ValidationException {
		logger.info("Validating " + bean.getClass() + ": " + bean.toString());

		for (Class group : groups) {
			Set<ConstraintViolation<T>> constraintViolations =
					validator.validate(bean, group);

			if (constraintViolations.size() > 0) {
				throw new ValidationException("Validation error",
						ValidationUtil.getErrorFieldsMap(constraintViolations));
			}
		}
	}

	public <T> void validate(T bean) throws ValidationException {
		validate(bean, RequiredChecks.class, Default.class);
	}

	public <T> void validate(T bean, Map<String, String[]> params, Class<?>... groups)
			throws ValidationException {
		T copy = cloner.deepCopy(bean);
		beanMapper.toExistingObject(params, copy);

		validate(copy, groups);
	}

	public <T> void validate(T bean, Map<String, String[]> params) throws ValidationException {
		validate(bean, params, RequiredChecks.class, Default.class);
	}

	public <T> T validate(Class<T> classType, Map<String, String[]> params, Class<?>... groups)
			throws ValidationException {
		T bean = beanMapper.toObject(params, classType);

		validate(bean, groups);

		return bean;
	}

	public <T> T validate(Class<T> classType, Map<String, String[]> params) throws ValidationException {
		return validate(classType, params, RequiredChecks.class, Default.class);
	}

	public Mapper getMapper() {
		return beanMapper;
	}
}
