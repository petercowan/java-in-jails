package org.jails.validation.constraint;

import org.jails.validation.constraint.validator.NotNullDependsValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target( { ElementType.TYPE, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NotNullDependsValidator.class)
@Documented
public @interface NotNullDepends {
	String message() default "{org.jails.validation.constraints.NotNullDepends.message}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	/**
	 * @return The first that may have a not null requirement
	 */
	String field();

	/**
	 * @return The field that determines not null
	 */
	String dependsField();

    /**
     * @return The field value that determines not null
     */
    String dependsValue();

	/**
	 * Defines several <code>@NotNullDepends</code> annotations on the same element
	 *
	 * @see FieldMatch
	 */
	@Target( { ElementType.TYPE, ElementType.ANNOTATION_TYPE })
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	@interface List {
		NotNullDepends[] value();
	}
}