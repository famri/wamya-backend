package com.excentria_it.wamya.application.port.out;

import java.util.Optional;

import com.excentria_it.wamya.domain.UserPreference;

public interface LoadUserPreferencesPort {

	Optional<UserPreference> loadUserPreferenceByKeyAndSubject(String preferenceKey, String username);

	Optional<UserPreference> loadUserPreferenceByKeyAndUserId(String userTimeZoneKey, Long userId);

}
