package com.excentria_it.wamya.common.annotation;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.excentria_it.wamya.common.impl.UserLoginValidator;

@Target({ FIELD, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = UserLoginValidator.class)
@Documented
public @interface ValidUserLogin {
	String message() default "{com.excentria_it.wamya.common.user.login.message}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	/**
	 * @return The login type field
	 */
	String loginTypeField();

	/**
	 * @return The login value field
	 */
	String loginValueField();

}
