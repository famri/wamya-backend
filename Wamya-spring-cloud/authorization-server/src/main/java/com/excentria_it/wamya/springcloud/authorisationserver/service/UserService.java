package com.excentria_it.wamya.springcloud.authorisationserver.service;

import javax.validation.constraints.NotEmpty;

import com.excentria_it.wamya.springcloud.authorisationserver.dto.OAuthUserAccount;

public interface UserService {
	OAuthUserAccount createUser(OAuthUserAccount user);
	
	OAuthUserAccount loadUserInfoByUsername(String username);

	void resetPassword(Long oauthId, @NotEmpty String newPassword);

	void updateMobile(Long oauthId, String icc, String mobileNumber);

	void updateEmail(Long oauthId, String email);
}
