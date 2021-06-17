package com.excentria_it.wamya.application.service;

import java.util.Optional;

import javax.transaction.Transactional;

import com.excentria_it.wamya.application.port.in.UpdateDeviceRegistrationTokenUseCase;
import com.excentria_it.wamya.application.port.out.LoadUserAccountPort;
import com.excentria_it.wamya.application.port.out.UpdateUserAccountPort;
import com.excentria_it.wamya.common.annotation.UseCase;
import com.excentria_it.wamya.domain.UserAccount;

import lombok.RequiredArgsConstructor;

@UseCase
@Transactional
@RequiredArgsConstructor
public class DeviceRegistrationTokenService implements UpdateDeviceRegistrationTokenUseCase {

	private final LoadUserAccountPort loadUserAccountPort;
	private final UpdateUserAccountPort updateUserAccountPort;

	@Override
	public void updateToken(String token, String username) {

		Optional<UserAccount> userAccountOptional = loadUserAccountPort.loadUserAccountByUsername(username);
		Long userId = userAccountOptional.get().getId();

		updateUserAccountPort.updateDeviceRegistrationToken(userId, token);
	}

}
