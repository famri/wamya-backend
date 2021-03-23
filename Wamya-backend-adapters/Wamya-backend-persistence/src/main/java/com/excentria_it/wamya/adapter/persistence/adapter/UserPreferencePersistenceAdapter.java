package com.excentria_it.wamya.adapter.persistence.adapter;

import java.util.Optional;

import com.excentria_it.wamya.adapter.persistence.entity.UserAccountJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.UserPreferenceId;
import com.excentria_it.wamya.adapter.persistence.entity.UserPreferenceJpaEntity;
import com.excentria_it.wamya.adapter.persistence.repository.UserAccountRepository;
import com.excentria_it.wamya.adapter.persistence.repository.UserPreferenceRepository;
import com.excentria_it.wamya.application.port.out.LoadUserPreferencesPort;
import com.excentria_it.wamya.application.port.out.SaveUserPreferencePort;
import com.excentria_it.wamya.common.annotation.PersistenceAdapter;
import com.excentria_it.wamya.domain.UserPreference;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@PersistenceAdapter
@Slf4j
public class UserPreferencePersistenceAdapter implements SaveUserPreferencePort, LoadUserPreferencesPort {

	private final UserAccountRepository userAccountRepository;
	private final UserPreferenceRepository userPreferenceRepository;

	@Override
	public void saveUserPreference(String key, String value, String username) {
		Optional<UserAccountJpaEntity> accountOptional;
		if (username.contains("@")) {
			accountOptional = userAccountRepository.findByEmailWithUserPreferences(username);
		} else if (username.contains("_")) {

			String[] userMobile = username.split("_");
			accountOptional = userAccountRepository.findByMobilePhoneNumberWithUserPreferences(userMobile[0],
					userMobile[1]);

		} else {
			log.error(String.format("Invalid username: %s", username));
			return;
		}
		if (accountOptional.isEmpty()) {
			log.error(String.format("User account not found by username: %s", username));
			return;
		}

		UserAccountJpaEntity userAccount = accountOptional.get();

		UserPreferenceJpaEntity userPreference = new UserPreferenceJpaEntity(
				new UserPreferenceId(userAccount.getId(), key), value, userAccount);

		userAccount.getPreferences().put(key, userPreference);

		userAccountRepository.save(userAccount);
	}

	@Override
	public Optional<UserPreference> loadUserPreferenceByKeyAndUsername(String preferenceKey, String username) {
		if (username.contains("@")) {
			return userPreferenceRepository.findByKeyAndUserAccountEmail(preferenceKey, username);
		} else if (username.contains("_")) {

			String[] userMobile = username.split("_");
			return userPreferenceRepository.findByKeyAndUserAccountMobileNumber(preferenceKey, userMobile[0],
					userMobile[1]);

		} else {
			return Optional.empty();
		}

	}

}
