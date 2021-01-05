package com.excentria_it.wamya.common.validator.impl;

import java.util.Set;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.excentria_it.wamya.common.annotation.Among;

public class AmongValidator implements ConstraintValidator<Among, String> {

	private Set<String> validValues;

	@Override
	public void initialize(Among amongAnnotation) {
		validValues = Set.of(amongAnnotation.value());
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {

		return validValues.contains(value);
	}

}
