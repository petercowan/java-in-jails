package org.jails.property;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target( { ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface IdentifyBy {

	String field() default "id";

	/**
	 * Defines several <code>@IdentifyBy</code> annotations on the same element
	 */
	@Target( { ElementType.TYPE })
	@Retention(RetentionPolicy.RUNTIME)
	@Documented
	@interface List {
		IdentifyBy[] value();
	}
}
