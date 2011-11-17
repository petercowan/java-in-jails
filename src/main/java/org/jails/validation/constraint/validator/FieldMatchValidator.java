package org.jails.validation.constraint.validator;

import org.apache.commons.beanutils.BeanUtils;
import org.jails.validation.constraint.FieldMatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FieldMatchValidator
        implements ConstraintValidator<FieldMatch, Object> {
	private static Logger logger = LoggerFactory.getLogger(FieldMatchValidator.class);

	private String fieldName;
	private String matchFieldName;

	public void initialize(final FieldMatch constraintAnnotation) {
		fieldName = constraintAnnotation.field();
		matchFieldName = constraintAnnotation.matchField();
	}

	public boolean isValid(final Object object, final ConstraintValidatorContext context) {
        if (object == null) return false;
		try {
			logger.info("Matching " + fieldName + " and " + matchFieldName);
			final Object value = BeanUtils.getProperty(object, fieldName);
			logger.info(fieldName + " = " + value);
			final Object matchValue = BeanUtils.getProperty(object, matchFieldName);
			logger.info(matchFieldName + " = " + matchValue + "? " + (value != null && value.equals(matchValue)));

			if  (value == null && matchValue == null) return true;
            else return value != null && value.equals(matchValue);
		} catch (Exception ignore) {
			return false;
		}
	}
}