package com.excentria_it.wamya.common.validator.impl;

import java.util.List;
import java.util.Set;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.excentria_it.wamya.common.SortCriterion;
import com.excentria_it.wamya.common.annotation.SortCriteria;

public class SortCriteriaValidator implements ConstraintValidator<SortCriteria, List<SortCriterion>> {

	private Set<String> validFieldNames;

	private Set<String> validSortingDirections;

	@Override
	public void initialize(SortCriteria sortCriteriaAnnotation) {
		validFieldNames = Set.of(sortCriteriaAnnotation.fields());
		validSortingDirections = Set.of(sortCriteriaAnnotation.directions());
	}

	@Override
	public boolean isValid(List<SortCriterion> sortingCriteria, ConstraintValidatorContext context) {

		if (sortingCriteria.isEmpty())
			return true;

		for (SortCriterion sc : sortingCriteria) {
			if (!validFieldNames.contains(sc.getField()) || !validSortingDirections.contains(sc.getDirection()))
				return false;
		}

		return true;
	}

}
