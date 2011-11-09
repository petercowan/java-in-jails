package org.jails.validation.constraint.validator;

import org.jails.util.StringUtil;
import org.jails.validation.constraint.StrongPassword;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class StrongPasswordValidator implements ConstraintValidator<StrongPassword, String> {

    public void initialize(StrongPassword constraintAnnotation) {
    }

    public boolean isValid(final String password, final ConstraintValidatorContext constraintContext) {
        if (password == null)
            return false;
        else
            return StringUtil.isStrongPassword(password);
    }

}
