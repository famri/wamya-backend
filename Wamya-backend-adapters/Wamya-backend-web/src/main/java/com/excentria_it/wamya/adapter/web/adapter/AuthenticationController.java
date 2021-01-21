package com.excentria_it.wamya.adapter.web.adapter;

import javax.validation.Valid;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.excentria_it.wamya.application.port.in.AuthenticateUserUseCase;
import com.excentria_it.wamya.application.port.in.AuthenticateUserUseCase.LoginUserCommand;
import com.excentria_it.wamya.common.annotation.WebAdapter;
import com.excentria_it.wamya.common.exception.ApiError;
import com.excentria_it.wamya.common.exception.AuthorizationException;
import com.excentria_it.wamya.domain.JwtOAuth2AccessToken;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@WebAdapter
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping(path = "/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class AuthenticationController {

	private final AuthenticateUserUseCase authenticateUserUseCase;

	@PostMapping(path = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
	public JwtOAuth2AccessToken loginUser(@Valid @RequestBody LoginUserCommand command) {

		JwtOAuth2AccessToken accessToken = authenticateUserUseCase.loginUser(command);

		return accessToken;
	}



}
