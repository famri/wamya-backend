package com.excentria_it.wamya.application.service;

import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.excentria_it.wamya.application.port.in.RegisterWebsocketConnectionStatusUseCase.RegisterConnectionStatusCommand;
import com.excentria_it.wamya.application.port.out.ConnectionStatusUpdatePort;
import com.excentria_it.wamya.test.data.common.RegisterConnectionStatusTestData;

@ExtendWith(MockitoExtension.class)
public class RegisterWebsocketConnectionStatusServiceTests {
	@Mock
	private ConnectionStatusUpdatePort connectionStatusUpdatePort;
	@InjectMocks
	private RegisterWebsocketConnectionStatusService registerWebsocketConnectionStatusService;

	@Test
	void givenRegisterConnectionStatusCommand_whenRegisterConnectionStatus_thenUpdateConnectionStatus() {
		// given
		RegisterConnectionStatusCommand command = RegisterConnectionStatusTestData.defaultLoginUserCommand().build();
		// When
		registerWebsocketConnectionStatusService.registerConnectionStatus(command);
		// then
		then(connectionStatusUpdatePort).should(times(1)).updateConnectionStatus(command.getUsername(),
				command.getConnected());
	}
}
