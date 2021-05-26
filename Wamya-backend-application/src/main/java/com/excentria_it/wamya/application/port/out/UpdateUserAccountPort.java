package com.excentria_it.wamya.application.port.out;

public interface UpdateUserAccountPort {

	void updateSMSValidationCode(Long userId, String validationCode);

	void updateEmailValidationCode(Long userId, String validationCode);

	void updateUserProfileImage(Long userId, String location, String hash);

}
