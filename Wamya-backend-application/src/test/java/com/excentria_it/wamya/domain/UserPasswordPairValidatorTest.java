package com.excentria_it.wamya.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.*;

import javax.validation.*;

import org.junit.jupiter.api.*;

import com.excentria_it.wamya.common.*;
import com.excentria_it.wamya.domain.UserAccount.*;

public class UserPasswordPairValidatorTest {
	private static Validator validator;

	@BeforeAll
	public static void setUp() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	@Test
	public void givenMatchingPasswordPair_thenValidatorShouldNotReportErrors() {

		UserPasswordPair userPasswordPair = new UserPasswordPair(UserAccountTestData.DEFAULT_RAW_PASSWORD,
				UserAccountTestData.DEFAULT_RAW_PASSWORD);

		Set<ConstraintViolation<UserPasswordPair>> violations = validator.validate(userPasswordPair);
		assertThat(violations.isEmpty()).isTrue();
	}

	@Test
	public void givenNotMatchingPasswordPair_thenValidatorShouldReportErrors() {

		UserPasswordPair userPasswordPair = new UserPasswordPair(UserAccountTestData.DEFAULT_RAW_PASSWORD,
				"DUMMY_PASSWORD");

		Set<ConstraintViolation<UserPasswordPair>> violations = validator.validate(userPasswordPair);
		assertThat(violations.isEmpty()).isFalse();
	}
}
