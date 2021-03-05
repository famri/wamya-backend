package com.excentria_it.wamya.test.data.common;

import java.util.Set;

import com.excentria_it.wamya.domain.UserPreference;

public class UserPreferenceTestData {
	public static Set<UserPreference> defaultUserPreferences() {
		return Set.of(new UserPreference(1L, "timezone", "Africa/Tunis"));
	}

	public static UserPreference defaultUserTimeZonePreference() {
		return new UserPreference(1L, "timezone", "Africa/Tunis");
	}
}
