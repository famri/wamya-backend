package com.excentria_it.wamya.application.utils;

import static com.excentria_it.wamya.test.data.common.UserPreferenceTestData.*;
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

import com.excentria_it.wamya.application.port.out.LoadUserAccountPort;
import com.excentria_it.wamya.domain.UserPreference;
import com.excentria_it.wamya.test.data.common.TestConstants;

@ExtendWith(MockitoExtension.class)
public class DateTimeHelperTests {
	@Mock
	private LoadUserAccountPort loadUserAccountPort;
	@InjectMocks
	private DateTimeHelper dateTimeHelper;

	@Test
	void testFindUserZoneIdByEmail() {
		// given
		UserPreference timeZonePreference = defaultUserTimeZonePreference();
		given(loadUserAccountPort.loadUserPreferenceByEmailAndKey(any(String.class), any(String.class)))
				.willReturn(Optional.of(timeZonePreference));

		// when
		ZoneId userZoneId = dateTimeHelper.findUserZoneId(TestConstants.DEFAULT_EMAIL);
		// then
		assertEquals(ZoneId.of(timeZonePreference.getValue()), userZoneId);
	}

	@Test
	void testFindUserZoneIdByMobileNumber() {
		// given
		UserPreference timeZonePreference = defaultUserTimeZonePreference();
		given(loadUserAccountPort.loadUserPreferenceByIccAndMobileNumberAndKey(any(String.class), any(String.class),
				any(String.class))).willReturn(Optional.of(timeZonePreference));

		// when
		ZoneId userZoneId = dateTimeHelper.findUserZoneId(TestConstants.DEFAULT_MOBILE_NUMBER_USERNAME);
		// then
		assertEquals(ZoneId.of(timeZonePreference.getValue()), userZoneId);
	}

	@Test
	void testFindUserZoneIdByBadUsername() {
		// given

		// when
		ZoneId userZoneId = dateTimeHelper.findUserZoneId("A BAD USERNAME");
		// then
		assertNull(userZoneId);
	}

	@Test
	void testFindUserZoneIdWithEmptyPreference() {
		// given

		given(loadUserAccountPort.loadUserPreferenceByEmailAndKey(any(String.class), any(String.class)))
				.willReturn(Optional.empty());

		// when
		ZoneId userZoneId = dateTimeHelper.findUserZoneId(TestConstants.DEFAULT_EMAIL);
		// then
		assertNull(userZoneId);
	}

	@Test
	void testFindUserZoneIdWithNullPreferenceValue() {
		// given
		UserPreference timeZonePreference = new UserPreference(1L, "timezone", null);

		given(loadUserAccountPort.loadUserPreferenceByEmailAndKey(any(String.class), any(String.class)))
				.willReturn(Optional.of(timeZonePreference));

		// when
		ZoneId userZoneId = dateTimeHelper.findUserZoneId(TestConstants.DEFAULT_EMAIL);
		// then
		assertNull(userZoneId);
	}

	@Test
	void testFindUserZoneIdWithEmptyPreferenceValue() {
		// given
		UserPreference timeZonePreference = new UserPreference(1L, "timezone", "");

		given(loadUserAccountPort.loadUserPreferenceByEmailAndKey(any(String.class), any(String.class)))
				.willReturn(Optional.of(timeZonePreference));

		// when
		ZoneId userZoneId = dateTimeHelper.findUserZoneId(TestConstants.DEFAULT_EMAIL);
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
