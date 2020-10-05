package com.excentria_it.wamya.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.*;

import javax.validation.*;

import org.junit.jupiter.api.*;

import com.excentria_it.wamya.common.*;
import com.excentria_it.wamya.domain.UserAccount.*;

public class MobilePhoneNumberValidatorTest {

	private static Validator validator;

	private static final String WRONG_INTERNATIONAL_CALLING_CODE = "+1234";

	private static final String WRONG_MOBILE_NUMBER = "990000";

	@BeforeAll
	public static void setUp() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	@Test
	public void givenCorrectMobilePhoneNumber_thenValidatorShouldNotReportErrors() {

		MobilePhoneNumber mobilePhoneNumber = new MobilePhoneNumber(
				UserAccountTestData.DEFAULT_INTERNATIONAL_CALLING_CODE, UserAccountTestData.DEFAULT_MOBILE_NUMBER);

		Set<ConstraintViolation<MobilePhoneNumber>> violations = validator.validate(mobilePhoneNumber);
		assertThat(violations.isEmpty()).isTrue();
	}

	@Test
	public void givenWrongInternationalCallingCode_thenValidatorShouldReportErrors() {

		MobilePhoneNumber mobilePhoneNumber = new MobilePhoneNumber(WRONG_INTERNATIONAL_CALLING_CODE,
				UserAccountTestData.DEFAULT_MOBILE_NUMBER);

		Set<ConstraintViolation<MobilePhoneNumber>> violations = validator.validate(mobilePhoneNumber);
		assertThat(violations.isEmpty()).isFalse();
	}

	@Test
	public void givenWrongMobileNumber_thenValidatorShouldReportErrors() {

		MobilePhoneNumber mobilePhoneNumber = new MobilePhoneNumber(
				UserAccountTestData.DEFAULT_INTERNATIONAL_CALLING_CODE, WRONG_MOBILE_NUMBER);

		Set<ConstraintViolation<MobilePhoneNumber>> violations = validator.validate(mobilePhoneNumber);
		assertThat(violations.isEmpty()).isFalse();
	}
}
