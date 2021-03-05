package com.excentria_it.wamya.test.data.common;

import com.excentria_it.wamya.adapter.persistence.entity.ClientJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.TransporterJpaEntity;
import com.excentria_it.wamya.domain.UserAccount;

public class UserAccountJpaEntityTestData {

	private static UserAccount userAccount = UserAccountTestData.defaultUserAccountBuilder().build();;

	public static final TransporterJpaEntity defaultExistentTransporterJpaEntity() {

		TransporterJpaEntity t = new TransporterJpaEntity(1L, userAccount.getOauthId(), userAccount.getGender(),
				userAccount.getFirstname(), userAccount.getLastname(), userAccount.getDateOfBirth(),
				userAccount.getEmail(), userAccount.getEmailValidationCode(), userAccount.getIsValidatedEmail(),
				InternationalCallingCodeJpaEntityTestData.defaultExistentInternationalCallingCodeJpaEntity(),
				userAccount.getMobilePhoneNumber().getMobileNumber(), userAccount.getMobileNumberValidationCode(),
				userAccount.getIsValidatedMobileNumber(), userAccount.getReceiveNewsletter(),
				userAccount.getCreationDateTime().toInstant(), userAccount.getPhotoUrl());
		t.setGlobalRating(4.3);
		return t;
	}

	public static final TransporterJpaEntity defaultNewTransporterJpaEntity() {

		TransporterJpaEntity t = new TransporterJpaEntity(null, userAccount.getOauthId(), userAccount.getGender(),
				userAccount.getFirstname(), userAccount.getLastname(), userAccount.getDateOfBirth(),
				userAccount.getEmail(), userAccount.getEmailValidationCode(), userAccount.getIsValidatedEmail(),
				InternationalCallingCodeJpaEntityTestData.defaultExistentInternationalCallingCodeJpaEntity(),
				userAccount.getMobilePhoneNumber().getMobileNumber(), userAccount.getMobileNumberValidationCode(),
				userAccount.getIsValidatedMobileNumber(), userAccount.getReceiveNewsletter(),
				userAccount.getCreationDateTime().toInstant(), userAccount.getPhotoUrl());
		t.setGlobalRating(5.0);
		return t;
	}

	public static final ClientJpaEntity defaultExistentClientJpaEntity() {

		return new ClientJpaEntity(1L, userAccount.getOauthId(), userAccount.getGender(), userAccount.getFirstname(),
				userAccount.getLastname(), userAccount.getDateOfBirth(), userAccount.getEmail(),
				userAccount.getEmailValidationCode(), userAccount.getIsValidatedEmail(),
				InternationalCallingCodeJpaEntityTestData.defaultExistentInternationalCallingCodeJpaEntity(),
				userAccount.getMobilePhoneNumber().getMobileNumber(), userAccount.getMobileNumberValidationCode(),
				userAccount.getIsValidatedMobileNumber(), userAccount.getReceiveNewsletter(),
				userAccount.getCreationDateTime().toInstant(), userAccount.getPhotoUrl());

	}

	public static final ClientJpaEntity defaultNewClientJpaEntity() {
		return new ClientJpaEntity(null, userAccount.getOauthId(), userAccount.getGender(), userAccount.getFirstname(),
				userAccount.getLastname(), userAccount.getDateOfBirth(), userAccount.getEmail(),
				userAccount.getEmailValidationCode(), userAccount.getIsValidatedEmail(),
				InternationalCallingCodeJpaEntityTestData.defaultExistentInternationalCallingCodeJpaEntity(),
				userAccount.getMobilePhoneNumber().getMobileNumber(), userAccount.getMobileNumberValidationCode(),
				userAccount.getIsValidatedMobileNumber(), userAccount.getReceiveNewsletter(),
				userAccount.getCreationDateTime().toInstant(), userAccount.getPhotoUrl());
	}
}
