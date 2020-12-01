package com.excentria_it.wamya.common.exception;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class ExceptionsTests {

	@Test
	void testUnsupportedInternationalCallingCode() {
		String message = "SOME MESSAGE";
		UnsupportedInternationalCallingCode exception = new UnsupportedInternationalCallingCode(message);
		assertEquals(exception.getMessage(), message);

	}

	@Test
	void testUserAccountAlreadyExistsException() {
		String message = "SOME MESSAGE";
		UserAccountAlreadyExistsException exception = new UserAccountAlreadyExistsException(message);
		assertEquals(exception.getMessage(), message);

	}

	@Test
	void testUserAccountNotFoundException() {
		String message = "SOME MESSAGE";
		UserAccountNotFoundException exception = new UserAccountNotFoundException(message);
		assertEquals(exception.getMessage(), message);

	}

	@Test
	void testUserEmailValidationException() {
		String message = "SOME MESSAGE";
		UserEmailValidationException exception = new UserEmailValidationException(message);
		assertEquals(exception.getMessage(), message);

	}

	@Test
	void testUserMobileNumberValidationException() {
		String message = "SOME MESSAGE";
		UserMobileNumberValidationException exception = new UserMobileNumberValidationException(message);
		assertEquals(exception.getMessage(), message);

	}

	@Test
	void testLoginOrPasswordNotFoundException() {
		String message = "SOME MESSAGE";
		AuthorizationException exception = new AuthorizationException(message);
		assertEquals(exception.getMessage(), message);

	}
}
