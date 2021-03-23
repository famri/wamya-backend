package com.excentria_it.wamya.common.validator.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.annotation.Annotation;

import javax.validation.Payload;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.excentria_it.wamya.common.FilterCriterion;
import com.excentria_it.wamya.common.annotation.Filter;

public class FilterCriterionValidatorTests {
	private static final FilterValidator validator = new FilterValidator();

	@BeforeAll
	static void init() {

		validator.initialize(new Filter() {

			@Override
			public Class<? extends Annotation> annotationType() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String[] fields() {

				return new String[] { "field1", "field2", "field3" };
			}

			@Override
			public String[] values() {
				return new String[] { "value1", "value2", "value3" };
			}

			@Override
			public Class<? extends Payload>[] payload() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String message() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Class<?>[] groups() {
				// TODO Auto-generated method stub
				return null;
			}

		});
	}

	@Test
	void givenValidFilteringCriterion_WhenIsValid_ThenReturnTrue() {

		boolean validationResult = validator.isValid(new FilterCriterion("field1", "value1"), null);

		assertTrue(validationResult);
	}

	@Test
	void givenInvalidFilteringCriterionField_WhenIsValid_ThenReturnFalse() {

		boolean validationResult = validator.isValid(new FilterCriterion("field4", "value3"), null);

		assertFalse(validationResult);
	}

	@Test
	void givenInvalidFilteringCriterionValue_WhenIsValid_ThenReturnFalse() {

		boolean validationResult = validator.isValid(new FilterCriterion("field1", "value4"), null);

		assertFalse(validationResult);
	}

	@Test
	void givenWrongOrderOfFilteringCriterionValue_WhenIsValid_ThenReturnFalse() {

		boolean validationResult = validator.isValid(new FilterCriterion("field1", "value2"), null);

		assertFalse(validationResult);
	}
}
