package com.excentria_it.wamya.common.validator.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.annotation.Annotation;

import javax.validation.Payload;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.excentria_it.wamya.common.annotation.Among;

public class AmonValidatorTests {

	private static final AmongValidator validator = new AmongValidator();

	@BeforeAll
	static void init() {

		validator.initialize(new Among() {

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

				return new String[] { "accepted", "rejected", "canceled" };
			}

		});
	}

	@Test
	void givenValidAmong_WhenIsValid_ThenReturnTrue() {

		boolean validationResult = validator.isValid("accepted", null);

		assertTrue(validationResult);
	}

	@Test
	void givenInvalidAmong_WhenIsValid_ThenReturnTrue() {

		boolean validationResult = validator.isValid("invalid_string", null);

		assertFalse(validationResult);
	}
}
