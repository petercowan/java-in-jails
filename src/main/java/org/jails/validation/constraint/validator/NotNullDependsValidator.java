package org.jails.validation.constraint.validator;

import org.apache.commons.beanutils.BeanUtils;
import org.jails.validation.constraint.NotNullDepends;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NotNullDependsValidator
        implements ConstraintValidator<NotNullDepends, Object> {
	private String fieldName;
	private String dependsFieldName;
    private String dependsFieldValue;

	public void initialize(final NotNullDepends constraintAnnotation) {
		fieldName = constraintAnnotation.field();
		dependsFieldName = constraintAnnotation.dependsField();
        dependsFieldValue = constraintAnnotation.dependsValue();
	}

	public boolean isValid(final Object object, final ConstraintValidatorContext context) {
        if (object == null) return false;
		try {
			final Object dependsValue = BeanUtils.getProperty(object, dependsFieldName);

            if (dependsValue != null && dependsValue.equals(dependsFieldValue)) {
                final Object fieldValue = BeanUtils.getProperty(object, fieldName);
                return fieldValue != null;
            }
            return true;
		} catch (Exception ignore) {
			return true;
		}
	}
}
