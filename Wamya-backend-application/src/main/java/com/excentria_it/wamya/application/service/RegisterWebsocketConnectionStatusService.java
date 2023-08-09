package com.excentria_it.wamya.application.service;

import javax.transaction.Transactional;

import com.excentria_it.wamya.application.port.in.RegisterWebsocketConnectionStatusUseCase;
import com.excentria_it.wamya.application.port.out.ConnectionStatusUpdatePort;
import com.excentria_it.wamya.common.annotation.UseCase;

import lombok.RequiredArgsConstructor;

@UseCase
@Transactional
@RequiredArgsConstructor
public class RegisterWebsocketConnectionStatusService implements RegisterWebsocketConnectionStatusUseCase {

	private final ConnectionStatusUpdatePort connectionStatusUpdatePort;

	@Override
	public void registerConnectionStatus(RegisterConnectionStatusCommand command) {

		connectionStatusUpdatePort.updateConnectionStatus(command.getUsername(), command.getConnected());

	}

}
