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

	@Test
	void testDocumentAccessException() {
		String message = "SOME MESSAGE";
		DocumentAccessException exception = new DocumentAccessException(message);
		assertEquals(exception.getMessage(), message);
	}

	@Test
	void testUnsupportedMimeTypeException() {
		String message = "SOME MESSAGE";
		UnsupportedMimeTypeException exception = new UnsupportedMimeTypeException(message);
		assertEquals(exception.getMessage(), message);
	}

	@Test
	void testDocumentNotFoundException() {
		String message = "SOME MESSAGE";
		DocumentNotFoundException exception = new DocumentNotFoundException(message);
		assertEquals(exception.getMessage(), message);
	}

	@Test
	void testForbiddenAccessException() {
		String message = "SOME MESSAGE";
		ForbiddenAccessException exception = new ForbiddenAccessException(message);
		assertEquals(exception.getMessage(), message);
	}

	@Test
	void testVehiculeNotFoundException() {
		String message = "SOME MESSAGE";
		VehiculeNotFoundException exception = new VehiculeNotFoundException(message);
		assertEquals(exception.getMessage(), message);
	}

	@Test
	void testLinkExpiredException() {

		String message = "SOME MESSAGE";
		LinkExpiredException exception = new LinkExpiredException(message);
		assertEquals(exception.getMessage(), message);
	}

	@Test
	void testJourneyRequestUpdateException() {

		String message = "SOME MESSAGE";
		JourneyRequestUpdateException exception = new JourneyRequestUpdateException(message);
		assertEquals(exception.getMessage(), message);
	}

	@Test
	void testTransporterRatingDetailsNotFoundException() {

		String message = "SOME MESSAGE";
		TransporterRatingDetailsNotFoundException exception = new TransporterRatingDetailsNotFoundException(message);
		assertEquals(exception.getMessage(), message);
	}
}
