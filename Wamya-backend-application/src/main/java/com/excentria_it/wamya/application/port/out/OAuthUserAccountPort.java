package com.excentria_it.wamya.application.port.out;

import java.util.UUID;

import org.springframework.security.oauth2.core.OAuth2AccessToken;

import com.excentria_it.wamya.domain.OAuthUserAccount;

public interface OAuthUserAccountPort {
	UUID createOAuthUserAccount(OAuthUserAccount userAccount);

	OAuth2AccessToken authorizeOAuthUser(String username, String password);

}
