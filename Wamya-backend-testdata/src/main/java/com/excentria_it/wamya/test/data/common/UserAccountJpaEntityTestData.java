package com.excentria_it.wamya.test.data.common;

import com.excentria_it.wamya.adapter.persistence.entity.ClientJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.TransporterJpaEntity;
import com.excentria_it.wamya.domain.UserAccount;

public class UserAccountJpaEntityTestData {

	private static UserAccount userAccount = UserAccountTestData.defaultUserAccountBuilder().build();;

	public static final TransporterJpaEntity defaultExistentTransporterJpaEntity() {

		return new TransporterJpaEntity(1L, userAccount.getOauthId(), userAccount.getGender(),
				userAccount.getFirstname(), userAccount.getLastname(), userAccount.getDateOfBirth(),
				userAccount.getEmail(), userAccount.getEmailValidationCode(), userAccount.getIsValidatedEmail(),
				InternationalCallingCodeJpaEntityTestData.defaultExistentInternationalCallingCodeJpaEntity(),
				userAccount.getMobilePhoneNumber().getMobileNumber(), userAccount.getMobileNumberValidationCode(),
				userAccount.getIsValidatedMobileNumber(), userAccount.getReceiveNewsletter(),
				userAccount.getCreationDateTime(), userAccount.getPhotoUrl(), null, null, null, null, null);
	}

	public static final TransporterJpaEntity defaultNewTransporterJpaEntity() {

		return new TransporterJpaEntity(null, userAccount.getOauthId(), userAccount.getGender(),
				userAccount.getFirstname(), userAccount.getLastname(), userAccount.getDateOfBirth(),
				userAccount.getEmail(), userAccount.getEmailValidationCode(), userAccount.getIsValidatedEmail(),
				InternationalCallingCodeJpaEntityTestData.defaultExistentInternationalCallingCodeJpaEntity(),
				userAccount.getMobilePhoneNumber().getMobileNumber(), userAccount.getMobileNumberValidationCode(),
				userAccount.getIsValidatedMobileNumber(), userAccount.getReceiveNewsletter(),
				userAccount.getCreationDateTime(), userAccount.getPhotoUrl(), null, null, null, null, null);
	}

	public static final ClientJpaEntity defaultExistentClientJpaEntity() {

		return new ClientJpaEntity(1L, userAccount.getOauthId(), userAccount.getGender(), userAccount.getFirstname(),
				userAccount.getLastname(), userAccount.getDateOfBirth(), userAccount.getEmail(),
				userAccount.getEmailValidationCode(), userAccount.getIsValidatedEmail(),
				InternationalCallingCodeJpaEntityTestData.defaultExistentInternationalCallingCodeJpaEntity(),
				userAccount.getMobilePhoneNumber().getMobileNumber(), userAccount.getMobileNumberValidationCode(),
				userAccount.getIsValidatedMobileNumber(), userAccount.getReceiveNewsletter(),
				userAccount.getCreationDateTime(), userAccount.getPhotoUrl(), null);

	}

	public static final ClientJpaEntity defaultNewClientJpaEntity() {
		return new ClientJpaEntity(null, userAccount.getOauthId(), userAccount.getGender(), userAccount.getFirstname(),
				userAccount.getLastname(), userAccount.getDateOfBirth(), userAccount.getEmail(),
				userAccount.getEmailValidationCode(), userAccount.getIsValidatedEmail(),
				InternationalCallingCodeJpaEntityTestData.defaultExistentInternationalCallingCodeJpaEntity(),
				userAccount.getMobilePhoneNumber().getMobileNumber(), userAccount.getMobileNumberValidationCode(),
				userAccount.getIsValidatedMobileNumber(), userAccount.getReceiveNewsletter(),
				userAccount.getCreationDateTime(), userAccount.getPhotoUrl(), null);
	}
}
