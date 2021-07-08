package com.excentria_it.wamya.application.port.out;

import com.excentria_it.wamya.domain.DocumentType;

public interface UpdateUserAccountPort {

	void updateSMSValidationCode(Long userId, String validationCode);

	void updateEmailValidationCode(Long userId, String validationCode);

	void updateUserProfileImage(Long userId, String location, String hash);

	void updateIdentityDocument(Long userId, String location, String hash, DocumentType documentType);

	void updateDeviceRegistrationToken(Long userId, String token);

	void updateIsValidatedMobileNumber(Long userId, boolean isValidated);

}
