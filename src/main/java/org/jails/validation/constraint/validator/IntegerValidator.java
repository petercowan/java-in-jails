package org.jails.validation.constraint.validator;

import org.jails.validation.constraint.IsInteger;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigInteger;

public class IntegerValidator implements ConstraintValidator<IsInteger, String> {

    public void initialize(IsInteger constraintAnnotation) {
    }

    public boolean isValid(final String integer, final ConstraintValidatorContext constraintContext) {
        if (integer == null)
            return true;
        else
			try {
				new BigInteger(integer);
				return true;
			} catch (NumberFormatException e) {
				return false;
			}
	}

}
