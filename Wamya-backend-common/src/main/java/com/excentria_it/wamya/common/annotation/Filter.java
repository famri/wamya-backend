package com.excentria_it.wamya.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.excentria_it.wamya.common.validator.impl.FilterValidator;

@Documented
@Constraint(validatedBy = FilterValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Filter {
	String message() default "{com.excentria_it.wamya.common.filter_criterion.message}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	/**
	 * @return The fields
	 */
	String[] fields();

	/**
	 * @return The directions
	 */
	String[] values() default {};
}
