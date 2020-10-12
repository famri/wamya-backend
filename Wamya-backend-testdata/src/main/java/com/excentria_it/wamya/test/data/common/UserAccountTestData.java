package com.excentria_it.wamya.test.data.common;

import static com.excentria_it.wamya.test.data.common.TestConstants.*;

import java.time.LocalDateTime;

import com.excentria_it.wamya.application.port.in.CreateUserAccountUseCase.CreateUserAccountCommand;
import com.excentria_it.wamya.application.port.in.CreateUserAccountUseCase.CreateUserAccountCommand.CreateUserAccountCommandBuilder;
import com.excentria_it.wamya.domain.Gender;
import com.excentria_it.wamya.domain.UserAccount;
import com.excentria_it.wamya.domain.UserAccount.MobilePhoneNumber;
import com.excentria_it.wamya.domain.UserAccount.UserAccountBuilder;

public class UserAccountTestData {

	private static UserAccountBuilder defaultUserAccountBuilder() {

		return UserAccount.builder().id(1L).isTransporter(false).gender(Gender.MAN).firstName(DEFAULT_FIRSTNAME)
				.lastName(DEFAULT_LASTNAME).dateOfBirth(DEFAULT_DATE_OF_BIRTH).email("user-email@gmail.com")
				.emailValidationCode(DEFAULT_VALIDATION_CODE).isValidatedEmail(true)
				.mobilePhoneNumber(defaultMobilePhoneNumber()).mobileNumberValidationCode(DEFAULT_VALIDATION_CODE)
				.isValidatedMobileNumber(true).userPassword(DEFAULT_ENCODED_PASSWORD).receiveNewsletter(true)
				.creationTimestamp(LocalDateTime.now());

	}

	public static UserAccount notYetValidatedEmailUserAccount() {
		return defaultUserAccountBuilder().isValidatedEmail(false).build();
	}

	public static UserAccount alreadyValidatedEmailUserAccount() {
		return defaultUserAccountBuilder().isValidatedEmail(true).build();
	}

	public static UserAccount notYetValidatedMobileNumberUserAccount() {
		return defaultUserAccountBuilder().isValidatedMobileNumber(false).build();
	}

	public static UserAccount alreadyValidatedMobileNumberUserAccount() {
		return defaultUserAccountBuilder().isValidatedMobileNumber(true).build();
	}

	public static MobilePhoneNumber defaultMobilePhoneNumber() {
		return new MobilePhoneNumber(DEFAULT_INTERNATIONAL_CALLING_CODE, DEFAULT_MOBILE_NUMBER);

	}

	public static String defaultUserEncodedPasswordBuilder() {
		return DEFAULT_ENCODED_PASSWORD;

	}

	public static String defaultUserClearPasswordBuilder() {

		return DEFAULT_RAW_PASSWORD;
	}

	public static CreateUserAccountCommandBuilder defaultCreateUserAccountCommandBuilder() {
		return CreateUserAccountCommand.builder().isTransporter(false).gender(Gender.MAN).firstName(DEFAULT_FIRSTNAME)
				.lastName(DEFAULT_LASTNAME).dateOfBirth(DEFAULT_DATE_OF_BIRTH).email(DEFAULT_EMAIL)
				.icc(DEFAULT_INTERNATIONAL_CALLING_CODE).mobileNumber(DEFAULT_MOBILE_NUMBER)
				.userPassword(DEFAULT_RAW_PASSWORD).receiveNewsletter(true);
	}

}
