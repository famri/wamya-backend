package com.excentria_it.wamya.adapter.web.adapter;

import javax.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.excentria_it.wamya.application.port.in.AuthenticateUserUseCase;
import com.excentria_it.wamya.application.port.in.AuthenticateUserUseCase.LoginUserCommand;
import com.excentria_it.wamya.common.annotation.WebAdapter;
import com.excentria_it.wamya.domain.OpenIdAuthResponse;

import lombok.RequiredArgsConstructor;

@WebAdapter
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthenticationController {

	private final AuthenticateUserUseCase authenticateUserUseCase;

	@PostMapping(path = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
	public OpenIdAuthResponse loginUser(@Valid @RequestBody LoginUserCommand command) {

		OpenIdAuthResponse accessToken = authenticateUserUseCase.loginUser(command);

		return accessToken;
	}

}
