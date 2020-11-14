package com.excentria_it.wamya.application.port.out;

import com.excentria_it.wamya.domain.JwtOAuth2AccessToken;
import com.excentria_it.wamya.domain.OAuthUserAccount;

public interface OAuthUserAccountPort {
	Long createOAuthUserAccount(OAuthUserAccount userAccount);

	JwtOAuth2AccessToken fetchJwtTokenForUser(String username, String password);

}
