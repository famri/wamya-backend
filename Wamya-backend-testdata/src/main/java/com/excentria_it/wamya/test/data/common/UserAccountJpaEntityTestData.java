package com.excentria_it.wamya.test.data.common;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.excentria_it.wamya.adapter.persistence.entity.ClientJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.TransporterJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.UserPreferenceId;
import com.excentria_it.wamya.adapter.persistence.entity.UserPreferenceJpaEntity;
import com.excentria_it.wamya.domain.UserAccount;
import com.excentria_it.wamya.domain.ValidationState;

public class UserAccountJpaEntityTestData {

	private static UserAccount userAccount = UserAccountTestData.defaultUserAccountBuilder().build();;

	public static final TransporterJpaEntity defaultExistentTransporterJpaEntity() {
		Map<String, UserPreferenceJpaEntity> preferences = new HashMap<>();
		userAccount.getPreferences().forEach((k, v) -> {
			preferences.put(k, new UserPreferenceJpaEntity(new UserPreferenceId(1L, k), v, null));
		});
		TransporterJpaEntity t = new TransporterJpaEntity(1L, userAccount.getOauthId(),
				GenderJpaTestData.manGenderJpaEntity(), userAccount.getFirstname(), userAccount.getLastname(),ValidationState.VALIDATED,
				TestConstants.DEFAULT_MINIBIO,
				userAccount.getDateOfBirth(), userAccount.getEmail(), userAccount.getEmailValidationCode(),
				userAccount.getIsValidatedEmail(),
				InternationalCallingCodeJpaEntityTestData.defaultExistentInternationalCallingCodeJpaEntity(),
				userAccount.getMobilePhoneNumber().getMobileNumber(), userAccount.getMobileNumberValidationCode(),
				userAccount.getIsValidatedMobileNumber(), userAccount.getReceiveNewsletter(),
				userAccount.getCreationDateTime().toInstant(),
				DocumentJpaTestData.defaultManProfileImageDocumentJpaEntity(), preferences,
				DocumentJpaTestData.jpegIdentityDocumentJpaEntity(), null,null);
		t.setGlobalRating(4.3);
		return t;
	}

	public static final TransporterJpaEntity defaultNewTransporterJpaEntity() {

		TransporterJpaEntity t = new TransporterJpaEntity(null, userAccount.getOauthId(),
				GenderJpaTestData.manGenderJpaEntity(), userAccount.getFirstname(), userAccount.getLastname(),ValidationState.NOT_VALIDATED,
				TestConstants.DEFAULT_MINIBIO,
				userAccount.getDateOfBirth(), userAccount.getEmail(), userAccount.getEmailValidationCode(),
				userAccount.getIsValidatedEmail(),
				InternationalCallingCodeJpaEntityTestData.defaultExistentInternationalCallingCodeJpaEntity(),
				userAccount.getMobilePhoneNumber().getMobileNumber(), userAccount.getMobileNumberValidationCode(),
				userAccount.getIsValidatedMobileNumber(), userAccount.getReceiveNewsletter(),
				userAccount.getCreationDateTime().toInstant(),
				DocumentJpaTestData.defaultManProfileImageDocumentJpaEntity(), Collections.emptyMap(), null, null,null);
		t.setGlobalRating(5.0);
		return t;
	}

	public static final ClientJpaEntity defaultExistentClientJpaEntity() {
		Map<String, UserPreferenceJpaEntity> preferences = new HashMap<>();
		userAccount.getPreferences().forEach((k, v) -> {
			preferences.put(k, new UserPreferenceJpaEntity(new UserPreferenceId(1L, k), v, null));
		});
		return new ClientJpaEntity(1L, userAccount.getOauthId(), GenderJpaTestData.manGenderJpaEntity(),
				userAccount.getFirstname(), userAccount.getLastname(), ValidationState.VALIDATED,
				TestConstants.DEFAULT_MINIBIO, userAccount.getDateOfBirth(), userAccount.getEmail(),
				userAccount.getEmailValidationCode(), userAccount.getIsValidatedEmail(),
				InternationalCallingCodeJpaEntityTestData.defaultExistentInternationalCallingCodeJpaEntity(),
				userAccount.getMobilePhoneNumber().getMobileNumber(), userAccount.getMobileNumberValidationCode(),
				userAccount.getIsValidatedMobileNumber(), userAccount.getReceiveNewsletter(),
				userAccount.getCreationDateTime().toInstant(),
				DocumentJpaTestData.defaultManProfileImageDocumentJpaEntity(), preferences, null, null,null);

	}

	public static final ClientJpaEntity defaultExistentWomanClientJpaEntity() {
		Map<String, UserPreferenceJpaEntity> preferences = new HashMap<>();
		userAccount.getPreferences().forEach((k, v) -> {
			preferences.put(k, new UserPreferenceJpaEntity(new UserPreferenceId(1L, k), v, null));
		});
		return new ClientJpaEntity(3L, userAccount.getOauthId(), GenderJpaTestData.womanGenderJpaEntity(),
				userAccount.getFirstname(), userAccount.getLastname(), ValidationState.VALIDATED,
				TestConstants.DEFAULT_MINIBIO, userAccount.getDateOfBirth(), userAccount.getEmail(),
				userAccount.getEmailValidationCode(), userAccount.getIsValidatedEmail(),
				InternationalCallingCodeJpaEntityTestData.defaultExistentInternationalCallingCodeJpaEntity(),
				userAccount.getMobilePhoneNumber().getMobileNumber(), userAccount.getMobileNumberValidationCode(),
				userAccount.getIsValidatedMobileNumber(), userAccount.getReceiveNewsletter(),
				userAccount.getCreationDateTime().toInstant(),
				DocumentJpaTestData.defaultManProfileImageDocumentJpaEntity(), preferences, null, null,null);

	}

	public static final ClientJpaEntity defaultNewClientJpaEntity() {
		return new ClientJpaEntity(null, userAccount.getOauthId(), GenderJpaTestData.manGenderJpaEntity(),
				userAccount.getFirstname(), userAccount.getLastname(), ValidationState.NOT_VALIDATED,
				TestConstants.DEFAULT_MINIBIO, userAccount.getDateOfBirth(), userAccount.getEmail(),
				userAccount.getEmailValidationCode(), userAccount.getIsValidatedEmail(),
				InternationalCallingCodeJpaEntityTestData.defaultExistentInternationalCallingCodeJpaEntity(),
				userAccount.getMobilePhoneNumber().getMobileNumber(), userAccount.getMobileNumberValidationCode(),
				userAccount.getIsValidatedMobileNumber(), userAccount.getReceiveNewsletter(),
				userAccount.getCreationDateTime().toInstant(),
				DocumentJpaTestData.defaultManProfileImageDocumentJpaEntity(), Collections.emptyMap(), null, null,null);
	}

	public static final ClientJpaEntity defaultExistentClientJpaEntityWithNoProfileImage() {
		Map<String, UserPreferenceJpaEntity> preferences = new HashMap<>();
		userAccount.getPreferences().forEach((k, v) -> {
			preferences.put(k, new UserPreferenceJpaEntity(new UserPreferenceId(1L, k), v, null));
		});
		return new ClientJpaEntity(1L, userAccount.getOauthId(), GenderJpaTestData.manGenderJpaEntity(),
				userAccount.getFirstname(), userAccount.getLastname(), ValidationState.VALIDATED,
				TestConstants.DEFAULT_MINIBIO, userAccount.getDateOfBirth(), userAccount.getEmail(),
				userAccount.getEmailValidationCode(), userAccount.getIsValidatedEmail(),
				InternationalCallingCodeJpaEntityTestData.defaultExistentInternationalCallingCodeJpaEntity(),
				userAccount.getMobilePhoneNumber().getMobileNumber(), userAccount.getMobileNumberValidationCode(),
				userAccount.getIsValidatedMobileNumber(), userAccount.getReceiveNewsletter(),
				userAccount.getCreationDateTime().toInstant(), null, preferences, null, null,null);

	}

	public static final ClientJpaEntity defaultExistentClientJpaEntityWithNonDefaultProfileImageAndNoIdentityDocument() {
		Map<String, UserPreferenceJpaEntity> preferences = new HashMap<>();
		userAccount.getPreferences().forEach((k, v) -> {
			preferences.put(k, new UserPreferenceJpaEntity(new UserPreferenceId(1L, k), v, null));
		});
		return new ClientJpaEntity(1L, userAccount.getOauthId(), GenderJpaTestData.manGenderJpaEntity(),
				userAccount.getFirstname(), userAccount.getLastname(), ValidationState.NOT_VALIDATED,
				TestConstants.DEFAULT_MINIBIO, userAccount.getDateOfBirth(), userAccount.getEmail(),
				userAccount.getEmailValidationCode(), userAccount.getIsValidatedEmail(),
				InternationalCallingCodeJpaEntityTestData.defaultExistentInternationalCallingCodeJpaEntity(),
				userAccount.getMobilePhoneNumber().getMobileNumber(), userAccount.getMobileNumberValidationCode(),
				userAccount.getIsValidatedMobileNumber(), userAccount.getReceiveNewsletter(),
				userAccount.getCreationDateTime().toInstant(),
				DocumentJpaTestData.nonDefaultManProfileImageDocumentJpaEntity(), preferences, null, null,null);

	}

	public static final ClientJpaEntity defaultExistentClientJpaEntityWithNonDefaultProfileImageAndIdentityDocument() {
		Map<String, UserPreferenceJpaEntity> preferences = new HashMap<>();
		userAccount.getPreferences().forEach((k, v) -> {
			preferences.put(k, new UserPreferenceJpaEntity(new UserPreferenceId(1L, k), v, null));
		});
		return new ClientJpaEntity(1L, userAccount.getOauthId(), GenderJpaTestData.manGenderJpaEntity(),
				userAccount.getFirstname(), userAccount.getLastname(), ValidationState.VALIDATED,
				TestConstants.DEFAULT_MINIBIO, userAccount.getDateOfBirth(), userAccount.getEmail(),
				userAccount.getEmailValidationCode(), userAccount.getIsValidatedEmail(),
				InternationalCallingCodeJpaEntityTestData.defaultExistentInternationalCallingCodeJpaEntity(),
				userAccount.getMobilePhoneNumber().getMobileNumber(), userAccount.getMobileNumberValidationCode(),
				userAccount.getIsValidatedMobileNumber(), userAccount.getReceiveNewsletter(),
				userAccount.getCreationDateTime().toInstant(),
				DocumentJpaTestData.nonDefaultManProfileImageDocumentJpaEntity(), preferences,
				DocumentJpaTestData.jpegIdentityDocumentJpaEntity(), null,null);

	}
}
