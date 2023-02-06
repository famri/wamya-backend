package com.excentria_it.wamya.test.data.common;

import com.excentria_it.wamya.application.port.in.CreateUserAccountUseCase.CreateUserAccountCommand;
import com.excentria_it.wamya.application.port.in.CreateUserAccountUseCase.CreateUserAccountCommand.CreateUserAccountCommandBuilder;
import com.excentria_it.wamya.domain.UserAccount;
import com.excentria_it.wamya.domain.UserAccount.MobilePhoneNumber;
import com.excentria_it.wamya.domain.UserAccount.UserAccountBuilder;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

import static com.excentria_it.wamya.test.data.common.TestConstants.*;

public class UserAccountTestData {

    public static UserAccountBuilder defaultUserAccountBuilder() {
        Map<String, String> preferences = new HashMap<>();
        preferences.put("timezone", "Europe/Paris");
        preferences.put("locale", "fr_FR");
        return UserAccount.builder().id(1L).isTransporter(false).genderId(1L).firstname(DEFAULT_FIRSTNAME).oauthId(OAuthId.USER_UUID)
                .lastname(DEFAULT_LASTNAME).dateOfBirth(DEFAULT_DATE_OF_BIRTH).email(DEFAULT_EMAIL)
                .emailValidationCode(DEFAULT_VALIDATION_CODE).isValidatedEmail(true)
                .mobilePhoneNumber(defaultMobilePhoneNumber()).mobileNumberValidationCode(DEFAULT_VALIDATION_CODE)
                .isValidatedMobileNumber(true).receiveNewsletter(true)
                .creationDateTime(ZonedDateTime.now(ZoneOffset.UTC)).preferences(preferences);

    }

    public static UserAccountBuilder defaultClientUserAccountBuilder() {
        Map<String, String> preferences = new HashMap<>();
        preferences.put("timezone", "Europe/Paris");
        preferences.put("locale", "fr_FR");

        return UserAccount.builder().id(1L).isTransporter(false).genderId(1L).firstname("client1").oauthId(OAuthId.CLIENT1_UUID)
                .lastname("client1").dateOfBirth(DEFAULT_DATE_OF_BIRTH).email("client1@gmail.com")
                .emailValidationCode(DEFAULT_VALIDATION_CODE).isValidatedEmail(true)
                .mobilePhoneNumber(new MobilePhoneNumber(DEFAULT_INTERNATIONAL_CALLING_CODE, "96111111"))
                .mobileNumberValidationCode(DEFAULT_VALIDATION_CODE).isValidatedMobileNumber(true).receiveNewsletter(true)
                .creationDateTime(ZonedDateTime.now(ZoneOffset.UTC)).preferences(preferences);

    }

    public static UserAccountBuilder defaultTransporterUserAccountBuilder() {
        Map<String, String> preferences = new HashMap<>();
        preferences.put("timezone", "Europe/Paris");
        preferences.put("locale", "fr_FR");

        return UserAccount.builder().id(2L).isTransporter(true).genderId(1L).firstname("transporter1").oauthId(OAuthId.TRANSPORTER1_UUID)
                .lastname("transporter1").dateOfBirth(DEFAULT_DATE_OF_BIRTH).email("transporter1@gmail.com")
                .emailValidationCode(DEFAULT_VALIDATION_CODE).isValidatedEmail(true)
                .mobilePhoneNumber(new MobilePhoneNumber(DEFAULT_INTERNATIONAL_CALLING_CODE, "96222222"))
                .mobileNumberValidationCode(DEFAULT_VALIDATION_CODE).isValidatedMobileNumber(true).receiveNewsletter(true)
                .creationDateTime(ZonedDateTime.now(ZoneOffset.UTC)).preferences(preferences);

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

    public static CreateUserAccountCommandBuilder defaultTransporterUserAccountCommandBuilder() {
        return CreateUserAccountCommand.builder().isTransporter(true).genderId(1L).firstname(DEFAULT_FIRSTNAME)
                .lastname(DEFAULT_LASTNAME).dateOfBirth(DEFAULT_DATE_OF_BIRTH).email(DEFAULT_EMAIL)
                .icc(DEFAULT_INTERNATIONAL_CALLING_CODE).mobileNumber(DEFAULT_MOBILE_NUMBER)
                .userPassword(DEFAULT_RAW_PASSWORD).receiveNewsletter(true);
    }

    public static CreateUserAccountCommandBuilder defaultClientUserAccountCommandBuilder() {
        return CreateUserAccountCommand.builder().isTransporter(false).genderId(1L).firstname(DEFAULT_FIRSTNAME)
                .lastname(DEFAULT_LASTNAME).dateOfBirth(DEFAULT_DATE_OF_BIRTH).email(DEFAULT_EMAIL)
                .icc(DEFAULT_INTERNATIONAL_CALLING_CODE).mobileNumber(DEFAULT_MOBILE_NUMBER)
                .userPassword(DEFAULT_RAW_PASSWORD).receiveNewsletter(true);
    }
}
