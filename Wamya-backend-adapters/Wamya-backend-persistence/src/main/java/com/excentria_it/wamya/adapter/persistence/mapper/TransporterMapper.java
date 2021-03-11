package com.excentria_it.wamya.adapter.persistence.mapper;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.excentria_it.wamya.adapter.persistence.entity.InternationalCallingCodeJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.TransporterJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.UserPreferenceId;
import com.excentria_it.wamya.adapter.persistence.entity.UserPreferenceJpaEntity;
import com.excentria_it.wamya.domain.UserAccount;

@Component
public class TransporterMapper {

	public TransporterJpaEntity mapToJpaEntity(UserAccount userAccount, InternationalCallingCodeJpaEntity icc) {
		if (userAccount == null || !userAccount.getIsTransporter())
			return null;

		Map<String, UserPreferenceJpaEntity> preferences = new HashMap<>();

		userAccount.getPreferences().forEach((k, v) -> preferences.put(k,
				new UserPreferenceJpaEntity(new UserPreferenceId(userAccount.getId(), k), v, null)));

		return new TransporterJpaEntity(userAccount.getId(), userAccount.getOauthId(), userAccount.getGender(),
				userAccount.getFirstname(), userAccount.getLastname(), userAccount.getDateOfBirth(),
				userAccount.getEmail(), userAccount.getEmailValidationCode(), userAccount.getIsValidatedEmail(), icc,
				userAccount.getMobilePhoneNumber().getMobileNumber(), userAccount.getMobileNumberValidationCode(),
				userAccount.getIsValidatedMobileNumber(), userAccount.getReceiveNewsletter(),
				userAccount.getCreationDateTime().toInstant(), userAccount.getPhotoUrl(), preferences);

	}
}
