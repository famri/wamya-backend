package com.excentria_it.wamya.common.validator.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.annotation.Annotation;
import java.time.LocalDateTime;

import javax.validation.Payload;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.excentria_it.wamya.common.PeriodCriterion;
import com.excentria_it.wamya.common.annotation.Period;

public class PeriodCriterionValidatorTests {

	private static final PeriodCriterionValidator validator = new PeriodCriterionValidator();

	@BeforeAll
	static void init() {

		validator.initialize(new Period() {

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
			public String[] value() {

				return new String[] { "y1", "m6", "m3" };
			}

		});
	}

	@Test
	void givenValidPeriodCriterion_WhenIsValid_ThenReturnTrue() {
		LocalDateTime ldt = LocalDateTime.now();
		boolean validationResult = validator
				.isValid(new PeriodCriterion("Y1", PeriodCriterion.PeriodValue.Y1.calculateLowerEdge(ldt), ldt), null);

		assertTrue(validationResult);
	}
	
	@Test
	void givenInvalidPeriodCriterion_WhenIsValid_ThenReturnTrue() {
		LocalDateTime ldt = LocalDateTime.now();
		boolean validationResult = validator
				.isValid(new PeriodCriterion("not_valid_period_value", PeriodCriterion.PeriodValue.Y1.calculateLowerEdge(ldt), ldt), null);

		assertFalse(validationResult);
	}
}
