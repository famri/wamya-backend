package com.excentria_it.wamya.application.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.excentria_it.wamya.application.port.out.LoadUserPreferencesPort;
import com.excentria_it.wamya.domain.UserPreference;

import lombok.NoArgsConstructor;

@Component
@NoArgsConstructor
public class DateTimeHelper {

	@Autowired
	private LoadUserPreferencesPort loadUserPreferencesPort;

	private static final String USER_TIME_ZONE_KEY = "timezone";

	public ZoneId findUserZoneId(String username) {
		Optional<UserPreference> userPreferenceOptional = loadUserPreferencesPort
				.loadUserPreferenceByKeyAndUsername(USER_TIME_ZONE_KEY, username);
		if (userPreferenceOptional.isEmpty())
			return null;

		return ZoneId.of(userPreferenceOptional.get().getValue());
	}
	
	public ZoneId findUserZoneId(Long userId) {
		Optional<UserPreference> userPreferenceOptional = loadUserPreferencesPort
				.loadUserPreferenceByKeyAndUserId(USER_TIME_ZONE_KEY, userId);
		if (userPreferenceOptional.isEmpty())
			return null;

		return ZoneId.of(userPreferenceOptional.get().getValue());
	}

	public Instant userLocalToSystemDateTime(LocalDateTime userDateTime, ZoneId userZoneId) {
		if (userDateTime == null || userZoneId == null)
			return null;

		ZonedDateTime userDateTimeZoned = userDateTime.atZone(userZoneId);
		return userDateTimeZoned.toInstant();
	}

	public LocalDateTime systemToUserLocalDateTime(Instant systemDateTime, ZoneId userZoneId) {
		if (systemDateTime == null || userZoneId == null)
			return null;

		ZonedDateTime userDateTimeZoned = systemDateTime.atZone(userZoneId);
		return userDateTimeZoned.toLocalDateTime();
	}
}
