package com.excentria_it.wamya.common.domain;

import static org.assertj.core.api.Assertions.*;

import java.lang.annotation.Annotation;

import javax.validation.Payload;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.excentria_it.wamya.common.LoginType;
import com.excentria_it.wamya.common.annotation.ValidUserLogin;
import com.excentria_it.wamya.common.validator.impl.UserLoginValidator;
import com.excentria_it.wamya.test.data.common.TestConstants;

public class UserLoginValidatorTests {

	private UserLoginValidator userLoginValidator = new UserLoginValidator();

	@BeforeEach
	void init() {

		userLoginValidator.initialize(new ValidUserLogin() {

			@Override
			public Class<? extends Annotation> annotationType() {
				// TODO Auto-generated method stub
				return null;
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
			public String loginValueField() {

				return "loginValue";
			}

			@Override
			public String loginTypeField() {

				return "loginType";
			}

			@Override
			public Class<?>[] groups() {
				// TODO Auto-generated method stub
				return null;
			}
		});
	}

	@Test
	void testIsValidEmailLogin() {

		assertThat(userLoginValidator.isValid(new UserLogin(LoginType.EMAIL, TestConstants.DEFAULT_EMAIL), null))
				.isTrue();

	}

	@Test
	void testIsNotValidEmailLogin() {
		assertThat(userLoginValidator.isValid(new UserLogin(LoginType.EMAIL, "toto#tata@gmail.com"), null)).isFalse();
	}

	@Test
	void testIsValidTunisiaPhoneNumberLogin() {

		assertThat(userLoginValidator.isValid(new UserLogin(LoginType.PHONE_NUMBER, "+216_23093418"), null)).isTrue();

	}

	@Test
	void testIsNotValidTunisiaPhoneNumberLogin() {

		assertThat(userLoginValidator.isValid(new UserLogin(LoginType.PHONE_NUMBER, "+216_023093418"), null)).isFalse();

	}

	@Test
	void testIsValidFrancePhoneNumberLogin() {

		assertThat(userLoginValidator.isValid(new UserLogin(LoginType.PHONE_NUMBER, "+33_762072850"), null)).isTrue();

	}

	@Test
	void testIsNotValidFrancePhoneNumberLogin() {

		assertThat(userLoginValidator.isValid(new UserLogin(LoginType.PHONE_NUMBER, "+33_0762072850"), null)).isFalse();

	}

	@Test
	void testIllegalArgumentException() {

		assertThat(
				userLoginValidator.isValid(new BadLoginTypeUserLogin("This is not a LoginType enum", "whatever"), null))
						.isFalse();

	}

	@Test
	void testExceptionWhenLookingForObjectToValidateFields() {

		assertThat(userLoginValidator
				.isValid(new BadLoginTypeFieldNameUserLogin(LoginType.EMAIL, TestConstants.DEFAULT_EMAIL), null))
						.isFalse();

	}

	@Test
	void testIsNotValidWhenNotEmailNeitherPhoneNumberLoginType() {

		assertThat(userLoginValidator.isValid(new UserLogin(LoginType.USER_UID, TestConstants.DEFAULT_EMAIL), null))
				.isFalse();

	}

}
