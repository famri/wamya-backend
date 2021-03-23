package com.excentria_it.wamya.application.port.out;

import java.util.Optional;

import com.excentria_it.wamya.domain.UserPreference;

public interface LoadUserPreferencesPort {

	Optional<UserPreference> loadUserPreferenceByKeyAndUsername(String preferenceKey, String username);

}
