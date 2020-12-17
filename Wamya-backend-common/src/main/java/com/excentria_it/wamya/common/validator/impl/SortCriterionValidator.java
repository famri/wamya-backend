package com.excentria_it.wamya.common.validator.impl;

import java.util.Set;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.excentria_it.wamya.common.SortingCriterion;
import com.excentria_it.wamya.common.annotation.SortCriterion;

public class SortCriterionValidator implements ConstraintValidator<SortCriterion, SortingCriterion> {

	private Set<String> validFieldNames;

	private Set<String> validSortingDirections;

	@Override
	public void initialize(SortCriterion sortCriterionAnnotation) {
		validFieldNames = Set.of(sortCriterionAnnotation.fields());
		validSortingDirections = Set.of(sortCriterionAnnotation.directions());
	}

	@Override
	public boolean isValid(SortingCriterion sortingCriterion, ConstraintValidatorContext context) {

		return (sortingCriterion == null || (validFieldNames.contains(sortingCriterion.getField())
				&& validSortingDirections.contains(sortingCriterion.getDirection())));

	}

}
