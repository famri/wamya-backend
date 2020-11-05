package com.excentria_it.wamya.application.port.out;

import java.util.UUID;

import com.excentria_it.wamya.domain.OAuthUserAccount;

public interface CreateOAuthUserAccountPort {
	UUID createOAuthUserAccount(OAuthUserAccount userAccount);
}
