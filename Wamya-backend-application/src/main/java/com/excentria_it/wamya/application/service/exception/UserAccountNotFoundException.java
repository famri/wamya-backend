package com.excentria_it.wamya.application.service.exception;

import com.excentria_it.wamya.domain.UserAccount.MobilePhoneNumber;

public class UserAccountNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -65629201028723663L;

	public UserAccountNotFoundException(MobilePhoneNumber mobileNumber) {
		super(String.format("No account having mobile phone number %s %s was found.",
				mobileNumber.getInternationalCallingCode(), mobileNumber.getMobileNumber()));
	}

	public UserAccountNotFoundException(Long userAccountId) {
		super(String.format("No account having ID %d was found.", userAccountId));
	}

}
