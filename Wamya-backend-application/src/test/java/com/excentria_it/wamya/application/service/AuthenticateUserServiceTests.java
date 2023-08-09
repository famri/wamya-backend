package com.excentria_it.wamya.application.service;

import com.excentria_it.wamya.application.port.in.AuthenticateUserUseCase.LoginUserCommand;
import com.excentria_it.wamya.application.port.out.LoadUserAccountPort;
import com.excentria_it.wamya.application.port.out.OAuthUserAccountPort;
import com.excentria_it.wamya.common.exception.UserAccountNotFoundException;
import com.excentria_it.wamya.domain.OpenIdAuthResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.excentria_it.wamya.test.data.common.UserLoginTestData.defaultLoginUserCommand;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticateUserServiceTests {
    @Mock
    private OAuthUserAccountPort oAuthUserAccountPort;

    @Mock
    private LoadUserAccountPort loadUserAccountPort;

    private static final String TOKEN = "SOME_TOKEN";

    @Spy
    @InjectMocks
    private AuthenticateUserService authenticateUserService;

    @Test
    public void givenUserHasAccount_WhenLoginUser_ThenReturnJwtToken() {
        // Given
        given(loadUserAccountPort.existsBySubject(any(String.class))).willReturn(true);
        givenOAuthUserAccountPort_FetchJwtTokenForUser_WillReturnToken();
        // When
        LoginUserCommand command = defaultLoginUserCommand().build();
        OpenIdAuthResponse token = authenticateUserService.loginUser(command);

        // Then
        then(loadUserAccountPort).should(times(1)).existsBySubject(command.getUsername());
        then(oAuthUserAccountPort).should(times(1)).fetchJwtTokenForUser(command.getUsername(), command.getPassword());
        assertEquals(TOKEN, token.getAccessToken());
    }

    @Test
    public void givenUserDoesNotHaveAccount_WhenLoginUser_ThenThrowUserNotFoundException() {
        // Given
        given(loadUserAccountPort.existsBySubject(any(String.class))).willReturn(false);

        // When
        LoginUserCommand command = defaultLoginUserCommand().build();

        // Then
        assertThrows(UserAccountNotFoundException.class, () -> authenticateUserService.loginUser(command));
        then(loadUserAccountPort).should(times(1)).existsBySubject(command.getUsername());
        then(oAuthUserAccountPort).should(never()).fetchJwtTokenForUser(any(String.class), any(String.class));

    }

    private void givenOAuthUserAccountPort_FetchJwtTokenForUser_WillReturnToken() {
        OpenIdAuthResponse token = new OpenIdAuthResponse();
        token.setAccessToken(TOKEN);
        given(oAuthUserAccountPort.fetchJwtTokenForUser(any(String.class), any(String.class))).willReturn(token);

    }

}
