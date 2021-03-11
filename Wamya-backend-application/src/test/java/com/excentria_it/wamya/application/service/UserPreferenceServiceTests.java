package com.excentria_it.wamya.application.service;

import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.excentria_it.wamya.application.port.out.SaveUserPreferencePort;
import com.excentria_it.wamya.test.data.common.TestConstants;

@ExtendWith(MockitoExtension.class)
public class UserPreferenceServiceTests {
	@Mock
	private SaveUserPreferencePort saveUserPreferencePort;

	@InjectMocks
	private UserPreferenceService userPreferenceService;

	private static final String KEY = "timezone";
	private static final String VALUE = "Africa/Tunis";

	@Test
	void testSaveUserPreference() {
		// given
		// when
		userPreferenceService.saveUserPreference(KEY, VALUE, TestConstants.DEFAULT_EMAIL);

		// then
		then(saveUserPreferencePort).should(times(1)).saveUserPreference(KEY, VALUE, TestConstants.DEFAULT_EMAIL);
	}
}
