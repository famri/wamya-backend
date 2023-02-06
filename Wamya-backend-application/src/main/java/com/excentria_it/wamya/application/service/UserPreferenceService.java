package com.excentria_it.wamya.application.service;

import javax.transaction.Transactional;

import com.excentria_it.wamya.application.port.in.SaveUserPreferenceUseCase;
import com.excentria_it.wamya.application.port.out.SaveUserPreferencePort;
import com.excentria_it.wamya.common.annotation.UseCase;

import lombok.RequiredArgsConstructor;

@UseCase
@Transactional
@RequiredArgsConstructor

public class UserPreferenceService implements SaveUserPreferenceUseCase {
	private final SaveUserPreferencePort saveUserPreferencePort;

	@Override
	public void saveUserPreference(String key, String value, String subject) {
		saveUserPreferencePort.saveUserPreference(key, value, subject);
	}

}
