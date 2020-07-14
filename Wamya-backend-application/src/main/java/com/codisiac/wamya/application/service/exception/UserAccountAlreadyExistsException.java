package com.codisiac.wamya.application.service.exception;

import com.codisiac.wamya.domain.UserAccount.MobilePhoneNumber;

public class UserAccountAlreadyExistsException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public UserAccountAlreadyExistsException(MobilePhoneNumber mobileNumber) {
		super(String.format("An Account having mobile phone number %s %s already exists.",
				mobileNumber.getInternationalCallingCode(), mobileNumber.getMobileNumber()));
	}
}
