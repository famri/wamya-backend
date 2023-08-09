package com.excentria_it.wamya.test.data.common;

import com.excentria_it.wamya.application.port.in.AuthenticateUserUseCase.LoginUserCommand;
import com.excentria_it.wamya.application.port.in.AuthenticateUserUseCase.LoginUserCommand.LoginUserCommandBuilder;

public class UserLoginTestData {

	public static LoginUserCommandBuilder defaultLoginUserCommand() {
		return LoginUserCommand.builder().username(TestConstants.DEFAULT_EMAIL)
				.password(TestConstants.DEFAULT_RAW_PASSWORD);
	}
}
