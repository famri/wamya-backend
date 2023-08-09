package com.excentria_it.wamya.common.validator.impl;

import java.util.Set;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.excentria_it.wamya.common.SortCriterion;
import com.excentria_it.wamya.common.annotation.Sort;

public class SortCriterionValidator implements ConstraintValidator<Sort, SortCriterion> {

	private Set<String> validFieldNames;

	private Set<String> validSortingDirections;

	@Override
	public void initialize(Sort sortCriterionAnnotation) {
		validFieldNames = Set.of(sortCriterionAnnotation.fields());
		validSortingDirections = Set.of(sortCriterionAnnotation.directions());
	}

	@Override
	public boolean isValid(SortCriterion sortingCriterion, ConstraintValidatorContext context) {

		return (sortingCriterion == null || (validFieldNames.contains(sortingCriterion.getField())
				&& validSortingDirections.contains(sortingCriterion.getDirection())));

	}

}
