package com.excentria_it.wamya.test.data.common;

import com.excentria_it.wamya.adapter.persistence.entity.UserAccountJpaEntity;
import com.excentria_it.wamya.domain.UserAccount;

public class UserAccountJpaEntityTestData {

	private static UserAccount userAccount = UserAccountTestData.defaultUserAccountBuilder().build();;

	public static final UserAccountJpaEntity defaultExistingTransporterUserAccountJpaEntity() {

		return UserAccountJpaEntity.builder().id(1L).isTransporter(userAccount.getIsTransporter())
				.gender(userAccount.getGender()).firstName(userAccount.getFirstName())
				.lastName(userAccount.getLastName()).dateOfBirth(userAccount.getDateOfBirth())
				.email(userAccount.getEmail()).emailValidationCode(userAccount.getEmailValidationCode())
				.isValidatedEmail(userAccount.getIsValidatedEmail())
				.icc(InternationalCallingCodeJpaEntityTestData.defaultInternationalCallingCodeJpaEntity())
				.mobileNumber(userAccount.getMobilePhoneNumber().getMobileNumber())
				.mobileNumberValidationCode(userAccount.getMobileNumberValidationCode())
				.isValidatedMobileNumber(userAccount.getIsValidatedMobileNumber())
				.password(userAccount.getUserPassword()).receiveNewsletter(userAccount.getReceiveNewsletter())
				.creationTimestamp(userAccount.getCreationTimestamp()).id(1L).isTransporter(true).build();
	}

	public static final UserAccountJpaEntity defaultNewTransporterUserAccountJpaEntity() {

		return UserAccountJpaEntity.builder().id(1L).isTransporter(userAccount.getIsTransporter())
				.gender(userAccount.getGender()).firstName(userAccount.getFirstName())
				.lastName(userAccount.getLastName()).dateOfBirth(userAccount.getDateOfBirth())
				.email(userAccount.getEmail()).emailValidationCode(userAccount.getEmailValidationCode())
				.isValidatedEmail(userAccount.getIsValidatedEmail())
				.icc(InternationalCallingCodeJpaEntityTestData.defaultInternationalCallingCodeJpaEntity())
				.mobileNumber(userAccount.getMobilePhoneNumber().getMobileNumber())
				.mobileNumberValidationCode(userAccount.getMobileNumberValidationCode())
				.isValidatedMobileNumber(userAccount.getIsValidatedMobileNumber())
				.password(userAccount.getUserPassword()).receiveNewsletter(userAccount.getReceiveNewsletter())
				.creationTimestamp(userAccount.getCreationTimestamp()).id(null).isTransporter(true).build();
	}

	public static final UserAccountJpaEntity defaultExistingNotTransporterUserAccountJpaEntity() {
		return UserAccountJpaEntity.builder().id(1L).isTransporter(userAccount.getIsTransporter())
				.gender(userAccount.getGender()).firstName(userAccount.getFirstName())
				.lastName(userAccount.getLastName()).dateOfBirth(userAccount.getDateOfBirth())
				.email(userAccount.getEmail()).emailValidationCode(userAccount.getEmailValidationCode())
				.isValidatedEmail(userAccount.getIsValidatedEmail())
				.icc(InternationalCallingCodeJpaEntityTestData.defaultInternationalCallingCodeJpaEntity())
				.mobileNumber(userAccount.getMobilePhoneNumber().getMobileNumber())
				.mobileNumberValidationCode(userAccount.getMobileNumberValidationCode())
				.isValidatedMobileNumber(userAccount.getIsValidatedMobileNumber())
				.password(userAccount.getUserPassword()).receiveNewsletter(userAccount.getReceiveNewsletter())
				.creationTimestamp(userAccount.getCreationTimestamp()).id(1L).isTransporter(false).build();
	}

	public static final UserAccountJpaEntity defaultNewNotTransporterUserAccountJpaEntity() {
		return UserAccountJpaEntity.builder().id(1L).isTransporter(userAccount.getIsTransporter())
				.gender(userAccount.getGender()).firstName(userAccount.getFirstName())
				.lastName(userAccount.getLastName()).dateOfBirth(userAccount.getDateOfBirth())
				.email(userAccount.getEmail()).emailValidationCode(userAccount.getEmailValidationCode())
				.isValidatedEmail(userAccount.getIsValidatedEmail())
				.icc(InternationalCallingCodeJpaEntityTestData.defaultInternationalCallingCodeJpaEntity())
				.mobileNumber(userAccount.getMobilePhoneNumber().getMobileNumber())
				.mobileNumberValidationCode(userAccount.getMobileNumberValidationCode())
				.isValidatedMobileNumber(userAccount.getIsValidatedMobileNumber())
				.password(userAccount.getUserPassword()).receiveNewsletter(userAccount.getReceiveNewsletter())
				.creationTimestamp(userAccount.getCreationTimestamp()).id(null).isTransporter(false).build();
	}
}
