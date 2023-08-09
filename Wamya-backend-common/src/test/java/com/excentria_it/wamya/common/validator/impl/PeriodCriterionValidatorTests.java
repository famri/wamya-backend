package com.excentria_it.wamya.common.validator.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.annotation.Annotation;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

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

				return new String[] { "w1", "m1", "lm1", "lm3" };
			}

		});
	}

	@Test
	void givenValidPeriodCriterion_WhenIsValid_ThenReturnTrue() {
		ZonedDateTime today = ZonedDateTime.now(ZoneOffset.UTC);
		ZonedDateTime[] edges = PeriodCriterion.PeriodValue.M1.calculateLowerAndHigherEdges(today);

		boolean validationResult = validator.isValid(new PeriodCriterion("M1", edges[0], edges[1]), null);

		assertTrue(validationResult);
	}

	@Test
	void givenInvalidPeriodCriterion_WhenIsValid_ThenReturnTrue() {
		ZonedDateTime today = ZonedDateTime.now(ZoneOffset.UTC);
		ZonedDateTime[] edges = PeriodCriterion.PeriodValue.M1.calculateLowerAndHigherEdges(today);
		boolean validationResult = validator.isValid(new PeriodCriterion("not_valid_period_value", edges[0], edges[1]),
				null);

		assertFalse(validationResult);
	}
}
