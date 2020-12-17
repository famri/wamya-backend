package com.excentria_it.wamya.common.validator.impl;

import java.util.List;
import java.util.Set;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.excentria_it.wamya.common.SortingCriterion;
import com.excentria_it.wamya.common.annotation.SortCriteria;

public class SortCriteriaValidator implements ConstraintValidator<SortCriteria, List<SortingCriterion>> {

	private Set<String> validFieldNames;

	private Set<String> validSortingDirections;

	@Override
	public void initialize(SortCriteria sortCriteriaAnnotation) {
		validFieldNames = Set.of(sortCriteriaAnnotation.fields());
		validSortingDirections = Set.of(sortCriteriaAnnotation.directions());
	}

	@Override
	public boolean isValid(List<SortingCriterion> sortingCriteria, ConstraintValidatorContext context) {

		if (sortingCriteria.isEmpty())
			return true;

		for (SortingCriterion sc : sortingCriteria) {
			if (!validFieldNames.contains(sc.getField()) || !validSortingDirections.contains(sc.getDirection()))
				return false;
		}

		return true;
	}

}
