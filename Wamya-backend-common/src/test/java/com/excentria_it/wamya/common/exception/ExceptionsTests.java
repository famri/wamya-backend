package com.excentria_it.wamya.common.exception;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class ExceptionsTests {

	@Test
	void testUnsupportedInternationalCallingCode() {
		String message = "SOME MESSAGE";
		UnsupportedInternationalCallingCodeException exception = new UnsupportedInternationalCallingCodeException(
				message);
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

	@Test
	void testJourneyRequestExpiredException() {
		String message = "SOME MESSAGE";
		JourneyRequestExpiredException exception = new JourneyRequestExpiredException(message);
		assertEquals(exception.getMessage(), message);

	}

	@Test
	void testJourneyRequestNotFoundException() {
		String message = "SOME MESSAGE";
		JourneyRequestNotFoundException exception = new JourneyRequestNotFoundException(message);
		assertEquals(exception.getMessage(), message);

	}

	@Test
	void testInvalidTransporterVehiculeException() {
		String message = "SOME MESSAGE";
		InvalidTransporterVehiculeException exception = new InvalidTransporterVehiculeException(message);
		assertEquals(exception.getMessage(), message);

	}

	@Test
	void testJourneyProposalNotFoundException() {
		String message = "SOME MESSAGE";
		JourneyProposalNotFoundException exception = new JourneyProposalNotFoundException(message);
		assertEquals(exception.getMessage(), message);
	}

	@Test
	void testOperationDeniedException() {
		String message = "SOME MESSAGE";
		OperationDeniedException exception = new OperationDeniedException(message);
		assertEquals(exception.getMessage(), message);
	}

	@Test
	void testConstructorModelNotFoundException() {
		String message = "SOME MESSAGE";
		ConstructorModelNotFoundException exception = new ConstructorModelNotFoundException(message);
		assertEquals(exception.getMessage(), message);
	}

	@Test
	void testConstructorNotFoundException() {
		String message = "SOME MESSAGE";
		ConstructorNotFoundException exception = new ConstructorNotFoundException(message);
		assertEquals(exception.getMessage(), message);
	}

	@Test
	void testEngineTypeNotFoundException() {
		String message = "SOME MESSAGE";
		EngineTypeNotFoundException exception = new EngineTypeNotFoundException(message);
		assertEquals(exception.getMessage(), message);
	}

	@Test
	void testCountryNotFoundException() {
		String message = "SOME MESSAGE";
		CountryNotFoundException exception = new CountryNotFoundException(message);
		assertEquals(exception.getMessage(), message);
	}

	@Test
	void testInvalidPlaceTypeException() {
		String message = "SOME MESSAGE";
		InvalidPlaceTypeException exception = new InvalidPlaceTypeException(message);
		assertEquals(exception.getMessage(), message);
	}

	@Test
	void testDepartmentNotFoundException() {

		String message = "SOME MESSAGE";
		DepartmentNotFoundException exception = new DepartmentNotFoundException(message);
		assertEquals(exception.getMessage(), message);
	}

	@Test
	void testDiscussionNotFoundException() {
		String message = "SOME MESSAGE";
		DiscussionNotFoundException exception = new DiscussionNotFoundException(message);
		assertEquals(exception.getMessage(), message);
	}

	@Test
	void testGenderNotFoundException() {
		String message = "SOME MESSAGE";
		GenderNotFoundException exception = new GenderNotFoundException(message);
		assertEquals(exception.getMessage(), message);
	}
}
