package com.excentria_it.wamya.application.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.excentria_it.wamya.application.port.out.LoadUserAccountPort;
import com.excentria_it.wamya.domain.UserAccount;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class DateTimeHelper {
	private final LoadUserAccountPort loadUserAccountPort;

	private static final String USER_TIME_ZONE_KEY = "timezone";

	public ZoneId findUserZoneId(String username) {
		Optional<UserAccount> accountOptional;
		if (username.contains("@")) {
			accountOptional = loadUserAccountPort.loadUserAccountByEmail(username);
		} else if (username.contains("_")) {

			String[] userMobile = username.split("_");
			accountOptional = loadUserAccountPort.loadUserAccountByIccAndMobileNumber(userMobile[0], userMobile[1]);

		} else {
			return null;
		}
		UserAccount account = accountOptional.get();
		if (account.getPreferences().isEmpty() || account.getPreferences().get(USER_TIME_ZONE_KEY) == null

				|| account.getPreferences().get(USER_TIME_ZONE_KEY).isEmpty()) {
			log.error(String.format("Timezone not found for user %s", username));
			return null;
		}

		return ZoneId.of(account.getPreferences().get(USER_TIME_ZONE_KEY));
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
