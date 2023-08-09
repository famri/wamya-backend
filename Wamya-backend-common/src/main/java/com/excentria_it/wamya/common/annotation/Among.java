package com.excentria_it.wamya.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.excentria_it.wamya.common.validator.impl.AmongValidator;

@Documented
@Constraint(validatedBy = AmongValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Among {
	String message() default "{com.excentria_it.wamya.common.among.message}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	/**
	 * @return the value
	 */
	String[] value();

}
