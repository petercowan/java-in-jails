package org.jails.validation.constraint;


import org.jails.validation.constraint.validator.IntegerValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target( { ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IntegerValidator.class)
@Documented
public @interface IsInteger {

	String message() default "{org.jails.validation.constraints.IsInteger.message}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
