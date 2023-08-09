package com.excentria_it.wamya.application.port.out;

import com.excentria_it.wamya.domain.OAuthUserAccount;
import com.excentria_it.wamya.domain.OpenIdAuthResponse;

public interface OAuthUserAccountPort {
    String createOAuthUserAccount(OAuthUserAccount userAccount);

    OpenIdAuthResponse fetchJwtTokenForUser(String username, String password);

    void resetPassword(String userOauthId, String password);

    void updateMobileNumber(String oauthId, String internationalCallingCode, String mobileNumber);

    void updateEmail(String userOauthId, String email);
}
