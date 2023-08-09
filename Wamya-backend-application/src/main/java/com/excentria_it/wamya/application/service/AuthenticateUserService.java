package com.excentria_it.wamya.application.service;

import com.excentria_it.wamya.application.port.in.AuthenticateUserUseCase;
import com.excentria_it.wamya.application.port.out.LoadUserAccountPort;
import com.excentria_it.wamya.application.port.out.OAuthUserAccountPort;
import com.excentria_it.wamya.common.annotation.UseCase;
import com.excentria_it.wamya.common.exception.UserAccountNotFoundException;
import com.excentria_it.wamya.domain.OpenIdAuthResponse;
import lombok.RequiredArgsConstructor;

import javax.transaction.Transactional;

@UseCase
@Transactional
@RequiredArgsConstructor
public class AuthenticateUserService implements AuthenticateUserUseCase {

    private final OAuthUserAccountPort oAuthUserAccountPort;
    private final LoadUserAccountPort loadUserAccountPort;

    @Override
    public OpenIdAuthResponse loginUser(LoginUserCommand command) {

        final boolean userExists = loadUserAccountPort.existsBySubject(command.getUsername());
        if (!userExists) {
            throw new UserAccountNotFoundException(String.format("User account not found by username: %s", command.getUsername()));
        }

        final OpenIdAuthResponse jwtToken = oAuthUserAccountPort.fetchJwtTokenForUser(command.getUsername(),
                command.getPassword());
        return jwtToken;

    }

}
