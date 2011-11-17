package org.jails.validation.constraint.validator;

import org.jails.validation.constraint.IsInteger;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;

public class DecimalValidator implements ConstraintValidator<IsInteger, String> {

    public void initialize(IsInteger constraintAnnotation) {
    }

    public boolean isValid(final String decimal, final ConstraintValidatorContext constraintContext) {
        if (decimal == null)
            return true;
        else
			try {
				new BigDecimal(decimal);
				return true;
			} catch (NumberFormatException e) {
				return false;
			}
	}

}
