package com.excentria_it.wamya.common.validator.impl;

import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.excentria_it.wamya.common.PeriodCriterion;
import com.excentria_it.wamya.common.annotation.Period;

public class PeriodCriterionValidator implements ConstraintValidator<Period, PeriodCriterion> {
	private Set<String> validValues;

	@Override
	public void initialize(Period periodAnnotation) {
		validValues = Set.of(periodAnnotation.value()).stream().map(v -> v.toUpperCase()).collect(Collectors.toSet());
	}

	@Override
	public boolean isValid(PeriodCriterion periodCriterion, ConstraintValidatorContext context) {

		return (periodCriterion == null || validValues.contains(periodCriterion.getValue().toUpperCase()));

	}

}
