package com.excentria_it.wamya.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.excentria_it.wamya.common.validator.impl.SortCriteriaValidator;

@Documented
@Constraint(validatedBy = SortCriteriaValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface SortCriteria {
	String message() default "{com.excentria_it.wamya.common.sort_criteria.message}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	/**
	 * @return The first field
	 */
	String[] fields();

	/**
	 * @return The second field
	 */
	String[] directions() default { "asc", "desc" };

}
