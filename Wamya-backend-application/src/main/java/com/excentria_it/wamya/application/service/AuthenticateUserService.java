package com.excentria_it.wamya.application.service;

import javax.transaction.Transactional;

import com.excentria_it.wamya.application.port.in.AuthenticateUserUseCase;
import com.excentria_it.wamya.application.port.out.OAuthUserAccountPort;
import com.excentria_it.wamya.common.annotation.UseCase;
import com.excentria_it.wamya.domain.OpenIdAuthResponse;

import lombok.RequiredArgsConstructor;

@UseCase
@Transactional
@RequiredArgsConstructor
public class AuthenticateUserService implements AuthenticateUserUseCase {

	private final OAuthUserAccountPort oAuthUserAccountPort;

	@Override
	public OpenIdAuthResponse loginUser(LoginUserCommand command) {

		OpenIdAuthResponse jwtToken = oAuthUserAccountPort.fetchJwtTokenForUser(command.getUsername(),
				command.getPassword());
		return jwtToken;

	}

}
