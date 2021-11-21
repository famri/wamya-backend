package com.excentria_it.wamya.application.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.excentria_it.wamya.application.port.in.RegisterWebsocketConnectionStatusUseCase;
import com.excentria_it.wamya.application.port.out.ConnectionStatusUpdatePort;

public class RegisterWebsocketConnectionStatusService implements RegisterWebsocketConnectionStatusUseCase {

	@Autowired
	private ConnectionStatusUpdatePort connectionStatusUpdatePort;

	@Override
	public void registerConnectionStatus(RegisterConnectionStatusCommand command) {
		
		connectionStatusUpdatePort.updateConnectionStatus(command.getUsername(), command.getConnected());

	}

}
