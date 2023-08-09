package com.excentria_it.wamya.common.validator.impl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.excentria_it.wamya.common.FilterCriterion;
import com.excentria_it.wamya.common.annotation.Filter;

public class FilterValidator implements ConstraintValidator<Filter, FilterCriterion> {

	private List<String> validFieldNames;

	private List<String> validFilteringValues;

	@Override
	public void initialize(Filter discussionFilter) {
		validFieldNames = List.of(discussionFilter.fields()).stream().map(n -> n.toUpperCase())
				.collect(Collectors.toList());
		validFilteringValues = List.of(discussionFilter.values());
	}

	@Override
	public boolean isValid(FilterCriterion filterCriterion, ConstraintValidatorContext context) {

		return (filterCriterion == null || (validFieldNames != null
				&& validFieldNames.contains(filterCriterion.getField().toUpperCase())
				&& validFilteringValues.size() > validFieldNames.indexOf(filterCriterion.getField().toUpperCase())
				&& Arrays.asList(
						validFilteringValues.get(validFieldNames.indexOf(filterCriterion.getField().toUpperCase())).split(","))
						.contains(filterCriterion.getValue())));

	}

}
