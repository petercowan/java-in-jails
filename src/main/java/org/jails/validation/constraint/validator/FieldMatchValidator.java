package org.jails.validation.constraint.validator;

import org.apache.commons.beanutils.BeanUtils;
import org.jails.validation.constraint.FieldMatch;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FieldMatchValidator
        implements ConstraintValidator<FieldMatch, Object> {
	private String fieldName;
	private String matchFieldName;

	public void initialize(final FieldMatch constraintAnnotation) {
		fieldName = constraintAnnotation.field();
		matchFieldName = constraintAnnotation.matchField();
	}

	public boolean isValid(final Object object, final ConstraintValidatorContext context) {
        if (object == null) return false;
		try {
			final Object value = BeanUtils.getProperty(object, fieldName);
			final Object matchValue = BeanUtils.getProperty(object, matchFieldName);

			return value == null && matchValue == null
                    || value != null && value.equals(matchValue);
		} catch (Exception ignore) {
			return false;
		}
	}
}