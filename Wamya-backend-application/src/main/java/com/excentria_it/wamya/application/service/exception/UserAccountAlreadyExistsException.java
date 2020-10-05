package com.excentria_it.wamya.application.service.exception;

import com.excentria_it.wamya.domain.UserAccount.MobilePhoneNumber;

import lombok.Getter;

public class UserAccountAlreadyExistsException extends RuntimeException {

	private static final long serialVersionUID = 5549444648458595290L;
	@Getter
	private MobilePhoneNumber mobilePhoneNumber;

	public UserAccountAlreadyExistsException(MobilePhoneNumber mobileNumber) {

		super(String.format("An Account having mobile phone number %s %s already exists.",
				mobileNumber.getInternationalCallingCode(), mobileNumber.getMobileNumber()));
		this.mobilePhoneNumber = mobileNumber;
	}
}
