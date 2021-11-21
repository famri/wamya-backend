package com.excentria_it.wamya.test.data.common;

import com.excentria_it.wamya.application.port.in.RegisterWebsocketConnectionStatusUseCase.RegisterConnectionStatusCommand;
import com.excentria_it.wamya.application.port.in.RegisterWebsocketConnectionStatusUseCase.RegisterConnectionStatusCommand.RegisterConnectionStatusCommandBuilder;

public class RegisterConnectionStatusTestData {

	public static RegisterConnectionStatusCommandBuilder defaultLoginUserCommand() {
		return RegisterConnectionStatusCommand.builder().username(TestConstants.DEFAULT_EMAIL).connected(true);
	}
}
