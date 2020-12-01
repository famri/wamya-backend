package com.excentria_it.wamya.application.service;

import static com.excentria_it.wamya.test.data.common.UserLoginTestData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import com.excentria_it.wamya.application.port.in.AuthenticateUserUseCase.LoginUserCommand;
import com.excentria_it.wamya.application.port.out.OAuthUserAccountPort;
import com.excentria_it.wamya.domain.JwtOAuth2AccessToken;

@ExtendWith(MockitoExtension.class)
public class AuthenticateUserServiceTests {
	@Mock
	private OAuthUserAccountPort oAuthUserAccountPort;

	private static final String TOKEN = "SOME_TOKEN";

	@Spy
	@InjectMocks
	private AuthenticateUserService authenticateUserService;

	@Test
	public void givenIsAuthenticated_WhenLoginUser_ThenReturnJwtToken() {
		// Given

		givenOAuthUserAccountPort_FetchJwtTokenForUser_WillReturnToken();
		// When
		LoginUserCommand command = defaultLoginUserCommand().build();
		JwtOAuth2AccessToken token = authenticateUserService.loginUser(command);

		// Then
		then(oAuthUserAccountPort).should(times(1)).fetchJwtTokenForUser(command.getUsername(), command.getPassword());
		assertEquals(TOKEN, token.getAccessToken());
	}

	private void givenOAuthUserAccountPort_FetchJwtTokenForUser_WillReturnToken() {
		JwtOAuth2AccessToken token = new JwtOAuth2AccessToken();
		token.setAccessToken(TOKEN);
		given(oAuthUserAccountPort.fetchJwtTokenForUser(any(String.class), any(String.class))).willReturn(token);

	}

}
