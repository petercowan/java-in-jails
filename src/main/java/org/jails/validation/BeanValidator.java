package org.jails.validation;

import com.thoughtworks.xstream.XStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jails.property.MapToBean;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.groups.Default;
import java.util.Map;
import java.util.Set;

/**
 * BeanValidator validates a JaveBean based on the javax.validation Annotations
 * set in that class
 */
public class BeanValidator {
	private static Logger logger = LoggerFactory.getLogger(BeanValidator.class);

	private static XStream xStream = new XStream();

	private MapToBean beanMapper;
	private Validator validator;

	private BeanValidator() {
		validator = ValidatorInstance.getInstance().getValidator();
	}

	public BeanValidator(MapToBean beanMapper) {
		this();
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
		T copy = (T) xStream.fromXML(xStream.toXML(bean));
		beanMapper.setBeanProperties(params, copy);

		validate(copy, groups);
	}

	public <T> void validate(T bean, Map<String, String[]> params) throws ValidationException {
		validate(bean, params, RequiredChecks.class, Default.class);
	}
}
