package com.excentria_it.wamya.common.validator.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.List;

import javax.validation.Payload;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.excentria_it.wamya.common.SortCriterion;
import com.excentria_it.wamya.common.annotation.SortCriteria;

public class SortCriteriaValidatorTests {

	private static final SortCriteriaValidator emptyListValidator = new SortCriteriaValidator();
	private static final SortCriteriaValidator validator = new SortCriteriaValidator();

	@BeforeAll
	static void init() {
		emptyListValidator.initialize(new SortCriteria() {

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

		validator.initialize(new SortCriteria() {

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
	void givenEmptySortingCriteria_WhenIsValid_ThenReturnTrue() {

		boolean validationResult = emptyListValidator.isValid(Collections.<SortCriterion>emptyList(), null);

		assertTrue(validationResult);
	}

	@Test
	void givenValidSortingCriteria_WhenIsValid_ThenReturnTrue() {

		boolean validationResult = validator.isValid(List.of(new SortCriterion("field1", "asc"),
				new SortCriterion("field2", "desc"), new SortCriterion("field3", "asc")), null);

		assertTrue(validationResult);
	}

	@Test
	void givenInvalidSortingCriterionField_WhenIsValid_ThenReturnTrue() {

		boolean validationResult = validator
				.isValid(List.of(new SortCriterion("field1", "asc"), new SortCriterion("field4", "desc")), null);

		assertFalse(validationResult);
	}

	@Test
	void givenInvalidSortingCriterionDirection_WhenIsValid_ThenReturnTrue() {

		boolean validationResult = validator
				.isValid(List.of(new SortCriterion("field1", "asc"), new SortCriterion("field2", "up")), null);

		assertFalse(validationResult);
	}
}
