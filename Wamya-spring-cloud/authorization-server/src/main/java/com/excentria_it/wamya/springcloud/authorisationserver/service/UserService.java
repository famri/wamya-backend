package com.excentria_it.wamya.springcloud.authorisationserver.service;

import com.excentria_it.wamya.springcloud.authorisationserver.dto.OAuthUserAccount;

public interface UserService {
	OAuthUserAccount createUser(OAuthUserAccount user);
	
	OAuthUserAccount loadUserInfoByUsername(String username);
}
