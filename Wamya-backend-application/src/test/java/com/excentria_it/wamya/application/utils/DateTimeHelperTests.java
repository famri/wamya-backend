package com.excentria_it.wamya.application.utils;

import static com.excentria_it.wamya.test.data.common.UserAccountTestData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.excentria_it.wamya.application.port.out.LoadUserAccountPort;
import com.excentria_it.wamya.domain.UserAccount;
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
		UserAccount userAccount = defaultUserAccountBuilder().preferences(Map.of("timezone", "Africa/Tunis")).build();
		given(loadUserAccountPort.loadUserAccountByEmail(any(String.class))).willReturn(Optional.of(userAccount));

		// when
		ZoneId userZoneId = dateTimeHelper.findUserZoneId(TestConstants.DEFAULT_EMAIL);
		// then
		assertEquals(ZoneId.of("Africa/Tunis"), userZoneId);
	}

	@Test
	void testFindUserZoneIdByMobileNumber() {
		// given
		UserAccount userAccount = defaultUserAccountBuilder().preferences(Map.of("timezone", "Africa/Tunis")).build();
		given(loadUserAccountPort.loadUserAccountByIccAndMobileNumber(any(String.class), any(String.class)))
				.willReturn(Optional.of(userAccount));

		// when
		ZoneId userZoneId = dateTimeHelper.findUserZoneId(TestConstants.DEFAULT_MOBILE_NUMBER_USERNAME);
		// then
		assertEquals(ZoneId.of("Africa/Tunis"), userZoneId);
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
		UserAccount userAccount = defaultUserAccountBuilder().preferences(new HashMap<>()).build();
		given(loadUserAccountPort.loadUserAccountByEmail(any(String.class))).willReturn(Optional.of(userAccount));

		// when
		ZoneId userZoneId = dateTimeHelper.findUserZoneId(TestConstants.DEFAULT_EMAIL);
		// then
		assertNull(userZoneId);
	}

	@Test
	void testFindUserZoneIdWithNullPreferenceValue() {
		// given
		UserAccount userAccount = defaultUserAccountBuilder().preferences(Map.of("K", "V")).build();
		given(loadUserAccountPort.loadUserAccountByEmail(any(String.class))).willReturn(Optional.of(userAccount));

		// when
		ZoneId userZoneId = dateTimeHelper.findUserZoneId(TestConstants.DEFAULT_EMAIL);
		// then
		assertNull(userZoneId);
	}

	@Test
	void testFindUserZoneIdWithEmptyPreferenceValue() {
		// given
		UserAccount userAccount = defaultUserAccountBuilder().preferences(Map.of("timezone", "")).build();
		given(loadUserAccountPort.loadUserAccountByEmail(any(String.class))).willReturn(Optional.of(userAccount));

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
