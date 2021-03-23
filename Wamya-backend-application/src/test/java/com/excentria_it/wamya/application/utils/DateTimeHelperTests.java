package com.excentria_it.wamya.application.utils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.excentria_it.wamya.application.port.out.LoadUserPreferencesPort;
import com.excentria_it.wamya.domain.UserPreference;
import com.excentria_it.wamya.test.data.common.TestConstants;

@ExtendWith(MockitoExtension.class)
public class DateTimeHelperTests {
	@Mock
	private LoadUserPreferencesPort loadUserPreferencesPort;
	@InjectMocks
	private DateTimeHelper dateTimeHelper;

	@Test
	void testFindUserZoneIdByUsername() {
		// given
		UserPreference userPreference = new UserPreference(1L, "timezone", "Africa/Tunis");
		given(loadUserPreferencesPort.loadUserPreferenceByKeyAndUsername(any(String.class), any(String.class)))
				.willReturn(Optional.of(userPreference));

		// when
		ZoneId userZoneId = dateTimeHelper.findUserZoneId(TestConstants.DEFAULT_MOBILE_NUMBER_USERNAME);
		// then
		assertEquals(ZoneId.of("Africa/Tunis"), userZoneId);
	}

	@Test
	void testFindUserZoneIdByBadUsername() {
		// given
		given(loadUserPreferencesPort.loadUserPreferenceByKeyAndUsername(any(String.class), any(String.class)))
				.willReturn(Optional.empty());
		// when
		ZoneId userZoneId = dateTimeHelper.findUserZoneId("A BAD USERNAME");
		// then
		assertNull(userZoneId);
	}

	@Test
	void testUserLocalToSystemDateTime() {
		// given
		Instant now = Instant.now();
		ZoneId userZoneId = ZoneId.of("Africa/Tunis");
		LocalDateTime userDateTime = now.atZone(userZoneId).toLocalDateTime();

		// when
		Instant systemInstant = dateTimeHelper.userLocalToSystemDateTime(userDateTime, userZoneId);
		// then
		assertEquals(now, systemInstant);
	}

	@Test
	void testUserLocalToSystemDateTimeWithNullUserDateTime() {
		// given

		ZoneId userZoneId = ZoneId.of("Africa/Tunis");

		// when
		Instant systemInstant = dateTimeHelper.userLocalToSystemDateTime(null, userZoneId);
		// then
		assertNull(systemInstant);
	}

	@Test
	void testUserLocalToSystemDateTimeWithNullUserZoneId() {
		// given
		Instant now = Instant.now();
		ZoneId userZoneId = ZoneId.of("Africa/Tunis");
		LocalDateTime userDateTime = now.atZone(userZoneId).toLocalDateTime();

		// when
		Instant systemInstant = dateTimeHelper.userLocalToSystemDateTime(userDateTime, null);
		// then
		assertNull(systemInstant);
	}

	@Test
	void testSystemToUserLocalDateTime() {
		// given
		Instant now = Instant.now();
		ZoneId userZoneId = ZoneId.of("Africa/Tunis");

		LocalDateTime nowLocalDateTime = now.atZone(userZoneId).toLocalDateTime();
		// when
		LocalDateTime userLocalDateTime = dateTimeHelper.systemToUserLocalDateTime(now, userZoneId);
		// then
		assertEquals(nowLocalDateTime, userLocalDateTime);
	}

	@Test
	void testSystemToUserLocalDateTimeWithNullSystemDateTime() {
		// given
		ZoneId userZoneId = ZoneId.of("Africa/Tunis");

		// when
		LocalDateTime userLocalDateTime = dateTimeHelper.systemToUserLocalDateTime(null, userZoneId);
		// then
		assertNull(userLocalDateTime);
	}

	@Test
	void testSystemToUserLocalDateTimeWithNullUserZoneId() {
		// given
		Instant now = Instant.now();

		// when
		LocalDateTime userLocalDateTime = dateTimeHelper.systemToUserLocalDateTime(now, null);
		// then
		assertNull(userLocalDateTime);
	}

}
