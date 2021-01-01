package com.excentria_it.wamya.common.validator.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.annotation.Annotation;

import javax.validation.Payload;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.excentria_it.wamya.common.SortCriterion;
import com.excentria_it.wamya.common.annotation.Sort;

public class SortCriterionValidatorTests {

	private static final SortCriterionValidator validator = new SortCriterionValidator();

	@BeforeAll
	static void init() {

		validator.initialize(new Sort() {

			@Override
			public Class<? extends Annotation> annotationType() {
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

			@Override
			public Class<? extends Payload>[] payload() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String[] fields() {

				return new String[] { "field1", "field2", "field3" };
			}

			@Override
			public String[] directions() {

				return new String[] { "asc", "desc" };
			}

		});
	}

	@Test
	void givenValidSortingCriterion_WhenIsValid_ThenReturnTrue() {

		boolean validationResult = validator.isValid(new SortCriterion("field1", "asc"), null);

		assertTrue(validationResult);
	}

	@Test
	void givenInvalidSortingCriterionField_WhenIsValid_ThenReturnTrue() {

		boolean validationResult = validator.isValid(new SortCriterion("field4", "desc"), null);

		assertFalse(validationResult);
	}

	@Test
	void givenInvalidSortingCriterionDirection_WhenIsValid_ThenReturnTrue() {

		boolean validationResult = validator.isValid(new SortCriterion("field2", "up"), null);

		assertFalse(validationResult);
	}
	
	@Test
	void givenInvalidSortingCriterionFieldAndDirection_WhenIsValid_ThenReturnTrue() {

		boolean validationResult = validator.isValid(new SortCriterion("field4", "up"), null);

		assertFalse(validationResult);
	}
}
