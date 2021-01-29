package com.excentria_it.wamya.common.exception;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.excentria_it.wamya.common.exception.ApiError.ErrorCode;

@ExtendWith(MockitoExtension.class)
public class RestApiExceptionHandlerTest {

	@InjectMocks
	private RestApiExceptionHandler restApiExceptionHandler;

	private static final String SOME_MESSAGE = "Some message";

	private static final String FIELD_1 = "field1";
	private static final String FIELD_2 = "field2";

	private static final String VAL_DOUBLE = "3.14";

	private static final String REQUEST_PART = "Some request part";

	private static final String FIELD_ERROR_1 = "Some field error 1";
	private static final String FIELD_ERROR_2 = "Some field error 2";

	private static final String GLOBAL_ERROR_1 = "Some global error 1";
	private static final String GLOBAL_ERROR_2 = "Some global error 2";

	private static final String OBJECT_TO_VALIDATE = "ObjectToValidate";

	private static final String SOME_PATH = "Some property path";

	private static final String SOME_REQUEST_URL = "/exceptions/not-found";

	private static final String ERROR_OCCURED = "error occurred.";

	@Test
	void whenHandleMethodArgumentNotValid_thenStatusIsBadRequest() {
		// given
		MethodArgumentNotValidException methodArgumentNotValidException = givenMethodArgumentNotValidExceptionWith2FieldErrorsAnd2GlobalErrors();
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		HttpHeaders headers = new HttpHeaders();
		WebRequest request = Mockito.mock(WebRequest.class);

		// When
		ResponseEntity<Object> responseEntity = restApiExceptionHandler
				.handleMethodArgumentNotValid(methodArgumentNotValidException, headers, status, request);

		// Then
		List<String> expectedErrors = new ArrayList<>();
		expectedErrors.add(FIELD_1 + ": " + FIELD_ERROR_1);
		expectedErrors.add(FIELD_2 + ": " + FIELD_ERROR_2);

		expectedErrors.add(OBJECT_TO_VALIDATE + ": " + GLOBAL_ERROR_1);
		expectedErrors.add(OBJECT_TO_VALIDATE + ": " + GLOBAL_ERROR_2);

		then(responseEntity.getBody() instanceof ApiError);

		then(((ApiError) responseEntity.getBody()).getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
		then(((ApiError) responseEntity.getBody()).getErrors()).containsExactlyElementsOf(expectedErrors);
		then(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

	}

	@Test
	void whenHandleBindException_thenStatusIsBadRequest() {
		// given
		BindException bindException = givenBindExceptionWith2FieldErrorsAnd2GlobalErrors();
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		HttpHeaders headers = new HttpHeaders();
		WebRequest request = Mockito.mock(WebRequest.class);

		// When
		ResponseEntity<Object> responseEntity = restApiExceptionHandler.handleBindException(bindException, headers,
				status, request);

		// Then
		List<String> expectedErrors = new ArrayList<>();
		expectedErrors.add(FIELD_1 + ": " + FIELD_ERROR_1);
		expectedErrors.add(FIELD_2 + ": " + FIELD_ERROR_2);

		expectedErrors.add(OBJECT_TO_VALIDATE + ": " + GLOBAL_ERROR_1);
		expectedErrors.add(OBJECT_TO_VALIDATE + ": " + GLOBAL_ERROR_2);

		then(responseEntity.getBody() instanceof ApiError);

		then(((ApiError) responseEntity.getBody()).getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
		then(((ApiError) responseEntity.getBody()).getErrors()).containsExactlyElementsOf(expectedErrors);
		then(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

	}

	@Test
	void whenHandleMethodArgumentTypeMismatchException_thenStatusIsBadRequest() {
		// given
		MethodArgumentTypeMismatchException methodArgumentTypeMismatch = givenMethodArgumentTypeMismatchExceptionWith1Error();

		WebRequest request = Mockito.mock(WebRequest.class);

		// When
		ResponseEntity<Object> responseEntity = restApiExceptionHandler
				.handleMethodArgumentTypeMismatch(methodArgumentTypeMismatch, request);

		// Then
		then(responseEntity.getBody() instanceof ApiError);
		then(((ApiError) responseEntity.getBody()).getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
		then(((ApiError) responseEntity.getBody()).getErrors())
				.containsExactly(FIELD_1 + " should be of type " + Long.class.getName());
		then(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

	}

	@Test
	void whenHandleTypeMismatchException_thenStatusIsBadRequest() {
		// given
		TypeMismatchException typeMismatchException = givenTypeMismatchExceptionWith1Error();
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		HttpHeaders headers = new HttpHeaders();

		WebRequest request = Mockito.mock(WebRequest.class);

		// When
		ResponseEntity<Object> responseEntity = restApiExceptionHandler.handleTypeMismatch(typeMismatchException,
				headers, status, request);

		// Then
		then(responseEntity.getBody() instanceof ApiError);
		then(((ApiError) responseEntity.getBody()).getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
		then(((ApiError) responseEntity.getBody()).getErrors()).containsExactly(
				VAL_DOUBLE + " value for " + FIELD_1 + " should be of type " + typeMismatchException.getRequiredType());
		then(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

	}

	@Test
	void whenHandleMissingServletRequestPart_thenStatusIsBadRequest() {
		// given
		MissingServletRequestPartException missingServletRequestPartException = givenMissingServletRequestPartExceptionWith1Error();
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		HttpHeaders headers = new HttpHeaders();

		WebRequest request = Mockito.mock(WebRequest.class);

		// When
		ResponseEntity<Object> responseEntity = restApiExceptionHandler
				.handleMissingServletRequestPart(missingServletRequestPartException, headers, status, request);

		// Then
		then(responseEntity.getBody() instanceof ApiError);
		then(((ApiError) responseEntity.getBody()).getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
		then(((ApiError) responseEntity.getBody()).getErrors()).containsExactly(REQUEST_PART + " part is missing");
		then(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

	}

	@Test
	void whenHandleMissingServletRequestParameter_thenStatusIsBadRequest() {
		// given
		MissingServletRequestParameterException missingServletRequestParameterException = givenMissingServletRequestParameterExceptionWith1Error();
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		HttpHeaders headers = new HttpHeaders();

		WebRequest request = Mockito.mock(WebRequest.class);

		// When
		ResponseEntity<Object> responseEntity = restApiExceptionHandler.handleMissingServletRequestParameter(
				missingServletRequestParameterException, headers, status, request);

		// Then
		then(responseEntity.getBody() instanceof ApiError);
		then(((ApiError) responseEntity.getBody()).getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
		then(((ApiError) responseEntity.getBody()).getErrors())
				.containsExactly(missingServletRequestParameterException.getParameterName() + " parameter is missing");
		then(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

	}

	@Test
	void whenHandleConstraintViolation_thenStatusIsBadRequest() {
		// given
		ConstraintViolationException constraintViolationException = givenConstraintViolationExceptionWith2ConstraintViolationsErrors();

		WebRequest request = Mockito.mock(WebRequest.class);

		// When
		ResponseEntity<Object> responseEntity = restApiExceptionHandler
				.handleConstraintViolation(constraintViolationException, request);

		// Then
		List<String> expectedErrors = new ArrayList<>();
		expectedErrors.add(SOME_PATH + ": " + SOME_MESSAGE);

		then(responseEntity.getBody() instanceof ApiError);
		then(((ApiError) responseEntity.getBody()).getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
		then(((ApiError) responseEntity.getBody()).getErrors()).containsExactlyElementsOf(expectedErrors);
		then(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

	}

	@Test
	void whenHandleNoHandlerFoundException_thenStatusIsNotFound() {
		// given
		NoHandlerFoundException noHandlerFoundException = givenNoHandlerFoundExceptionWith1Error();
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		HttpHeaders headers = new HttpHeaders();

		WebRequest request = Mockito.mock(WebRequest.class);

		// When
		ResponseEntity<Object> responseEntity = restApiExceptionHandler
				.handleNoHandlerFoundException(noHandlerFoundException, headers, status, request);

		// Then
		then(responseEntity.getBody() instanceof ApiError);
		then(((ApiError) responseEntity.getBody()).getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
		then(((ApiError) responseEntity.getBody()).getErrors()).containsExactly("No handler found for "
				+ noHandlerFoundException.getHttpMethod() + " " + noHandlerFoundException.getRequestURL());
		then(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

	}

	@Test
	void whenHandleHttpRequestMethodNotSupported_thenStatusIsMethodNotAllowed() {
		// given
		HttpRequestMethodNotSupportedException httpRequestMethodNotSupportedException = givenHttpRequestMethodNotSupportedExceptionWith1Error();
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		HttpHeaders headers = new HttpHeaders();

		WebRequest request = Mockito.mock(WebRequest.class);

		// When
		ResponseEntity<Object> responseEntity = restApiExceptionHandler
				.handleHttpRequestMethodNotSupported(httpRequestMethodNotSupportedException, headers, status, request);

		final StringBuilder builder = new StringBuilder();
		builder.append(httpRequestMethodNotSupportedException.getMethod());
		builder.append(" method is not supported for this request. Supported methods are ");
		httpRequestMethodNotSupportedException.getSupportedHttpMethods().forEach(t -> builder.append(t + " "));

		// Then
		then(responseEntity.getBody() instanceof ApiError);
		then(((ApiError) responseEntity.getBody()).getStatus()).isEqualTo(HttpStatus.METHOD_NOT_ALLOWED);
		then(((ApiError) responseEntity.getBody()).getErrors()).containsExactly(builder.toString().trim());
		then(responseEntity.getStatusCode()).isEqualTo(HttpStatus.METHOD_NOT_ALLOWED);

	}

	@Test
	void whenHandleHttpMediaTypeNotSupported_thenStatusIsUnsupportedMediaType() {
		// given
		HttpMediaTypeNotSupportedException httpMediaTypeNotSupportedException = givenHttpMediaTypeNotSupportedExceptionWith1Error();
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		HttpHeaders headers = new HttpHeaders();

		WebRequest request = Mockito.mock(WebRequest.class);

		// When
		ResponseEntity<Object> responseEntity = restApiExceptionHandler
				.handleHttpMediaTypeNotSupported(httpMediaTypeNotSupportedException, headers, status, request);

		final StringBuilder builder = new StringBuilder();
		builder.append(httpMediaTypeNotSupportedException.getContentType());
		builder.append(" media type is not supported. Supported media types are ");
		httpMediaTypeNotSupportedException.getSupportedMediaTypes().forEach(t -> builder.append(t + " "));

		// Then
		then(responseEntity.getBody() instanceof ApiError);
		then(((ApiError) responseEntity.getBody()).getStatus()).isEqualTo(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
		then(((ApiError) responseEntity.getBody()).getErrors()).containsExactly(builder.toString().trim());
		then(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNSUPPORTED_MEDIA_TYPE);

	}

	@Test
	void whenHandleUserAccountNotFoundException_ThenStatusIsBadRequest() {
		// given
		UserAccountNotFoundException exception = givenUserAccountNotFoundException();

		// When
		ResponseEntity<ApiError> responseEntity = restApiExceptionHandler.handleUserAccountNotFoundException(exception);

		// Then
		then(responseEntity.getBody() instanceof ApiError);
		then(((ApiError) responseEntity.getBody()).getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
		then(((ApiError) responseEntity.getBody()).getErrorCode()).isEqualTo(ErrorCode.OBJECT_NOT_FOUND);
		then(((ApiError) responseEntity.getBody()).getErrors()).containsExactly(SOME_MESSAGE);
		then(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}

	@Test
	void whenHandleUserMobileNumberValidationException_ThenStatusIsBadRequest() {
		// given
		UserMobileNumberValidationException exception = givenUserMobileNumberValidationException();

		// When
		ResponseEntity<ApiError> responseEntity = restApiExceptionHandler
				.handleUserMobileNumberValidationException(exception);

		// Then
		then(responseEntity.getBody() instanceof ApiError);
		then(((ApiError) responseEntity.getBody()).getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
		then(((ApiError) responseEntity.getBody()).getErrorCode()).isEqualTo(ErrorCode.MOBILE_VALIDATION);
		then(((ApiError) responseEntity.getBody()).getErrors()).containsExactly(SOME_MESSAGE);
		then(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}

	@Test
	void whenHandleUserEmailValidationException_ThenStatusIsBadRequest() {
		// given
		UserEmailValidationException exception = givenUserEmailValidationException();

		// When
		ResponseEntity<ApiError> responseEntity = restApiExceptionHandler.handleUserEmailValidationException(exception);

		// Then
		then(responseEntity.getBody() instanceof ApiError);
		then(((ApiError) responseEntity.getBody()).getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
		then(((ApiError) responseEntity.getBody()).getErrorCode()).isEqualTo(ErrorCode.EMAIL_VALIDATION);
		then(((ApiError) responseEntity.getBody()).getErrors()).containsExactly(SOME_MESSAGE);
		then(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}

	@Test
	void whenHandleUserAccountAlreadyExistsException_ThenStatusIsBadRequest() {
		// given
		UserAccountAlreadyExistsException exception = givenUserAccountAlreadyExistsException();

		// When
		ResponseEntity<ApiError> responseEntity = restApiExceptionHandler
				.handleUserAccountAlreadyExistsException(exception);

		// Then
		then(responseEntity.getBody() instanceof ApiError);
		then(((ApiError) responseEntity.getBody()).getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
		then(((ApiError) responseEntity.getBody()).getErrorCode()).isEqualTo(ErrorCode.ACCOUNT_EXISTS);
		then(((ApiError) responseEntity.getBody()).getErrors()).containsExactly("User account already exists.");
		then(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}

	@Test
	void whenHandleUnsupportedInternationalCallingCode_ThenStatusIsBadRequest() {
		// given
		UnsupportedInternationalCallingCode exception = givenUnsupportedInternationalCallingCode();

		// When
		ResponseEntity<ApiError> responseEntity = restApiExceptionHandler
				.handleUnsupportedInternationalCallingCode(exception);

		// Then
		then(responseEntity.getBody() instanceof ApiError);
		then(((ApiError) responseEntity.getBody()).getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
		then(((ApiError) responseEntity.getBody()).getErrorCode()).isEqualTo(ErrorCode.VALIDATION_ERROR);
		then(((ApiError) responseEntity.getBody()).getErrors())
				.containsExactly("International calling code is not supported.");
		then(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}

	@Test
	void whenHandleInvalidTransporterVehiculeException_ThenStatusIsBadRequest() {
		// given
		InvalidTransporterVehiculeException exception = givenInvalidTransporterVehiculeException();

		// When
		ResponseEntity<ApiError> responseEntity = restApiExceptionHandler
				.handleInvalidTransporterVehiculeException(exception);

		// Then
		then(responseEntity.getBody() instanceof ApiError);
		then(((ApiError) responseEntity.getBody()).getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
		then(((ApiError) responseEntity.getBody()).getErrorCode()).isEqualTo(ErrorCode.VALIDATION_ERROR);
		then(((ApiError) responseEntity.getBody()).getErrors()).containsExactly(SOME_MESSAGE);
		then(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}

	@Test
	void whenHandleCountryNotFoundException_ThenStatusIsBadRequest() {
		// given
		CountryNotFoundException exception = givenCountryNotFoundException();

		// When
		ResponseEntity<ApiError> responseEntity = restApiExceptionHandler.handleCountryNotFoundException(exception);

		// Then
		then(responseEntity.getBody() instanceof ApiError);
		then(((ApiError) responseEntity.getBody()).getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
		then(((ApiError) responseEntity.getBody()).getErrorCode()).isEqualTo(ErrorCode.VALIDATION_ERROR);
		then(((ApiError) responseEntity.getBody()).getErrors()).containsExactly(SOME_MESSAGE);
		then(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}

	@Test
	void whenHandleAuthorizationException_ThenStatusIsUnauthorized() {
		// given
		AuthorizationException exception = givenAuthorizationException();

		// When
		ResponseEntity<ApiError> responseEntity = restApiExceptionHandler.handleAuthorizationException(exception);

		// Then
		then(responseEntity.getBody() instanceof ApiError);
		then(((ApiError) responseEntity.getBody()).getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED);
		then(((ApiError) responseEntity.getBody()).getErrorCode()).isEqualTo(ErrorCode.AUTHORIZATION);
		then(((ApiError) responseEntity.getBody()).getErrors()).containsExactly(SOME_MESSAGE);
		then(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
	}

	@Test
	void whenHandleJourneyRequestNotFoundException_ThenStatusIsBadRequest() {
		// given
		JourneyRequestNotFoundException exception = givenJourneyRequestNotFoundException();

		// When
		ResponseEntity<ApiError> responseEntity = restApiExceptionHandler
				.handleJourneyRequestNotFoundException(exception);

		// Then
		then(responseEntity.getBody() instanceof ApiError);
		then(((ApiError) responseEntity.getBody()).getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
		then(((ApiError) responseEntity.getBody()).getErrorCode()).isEqualTo(ErrorCode.OBJECT_NOT_FOUND);
		then(((ApiError) responseEntity.getBody()).getErrors()).containsExactly(SOME_MESSAGE);
		then(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}

	@Test
	void whenHandleJourneyRequestExpiredException_ThenStatusIsBadRequest() {
		// given
		JourneyRequestExpiredException exception = givenJourneyRequestExpiredException();

		// When
		ResponseEntity<ApiError> responseEntity = restApiExceptionHandler
				.handleJourneyRequestExpiredException(exception);

		// Then
		then(responseEntity.getBody() instanceof ApiError);
		then(((ApiError) responseEntity.getBody()).getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
		then(((ApiError) responseEntity.getBody()).getErrorCode()).isEqualTo(ErrorCode.VALIDATION_ERROR);
		then(((ApiError) responseEntity.getBody()).getErrors()).containsExactly(SOME_MESSAGE);
		then(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}

	@Test
	void whenHandleAll_thenStatusIsMethodNotAllowed() {
		// given
		Exception exception = givenAnyOtherException();

		WebRequest request = Mockito.mock(WebRequest.class);

		// When
		ResponseEntity<Object> responseEntity = restApiExceptionHandler.handleAll(exception, request);

		// Then
		then(responseEntity.getBody() instanceof ApiError);
		then(((ApiError) responseEntity.getBody()).getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
		then(((ApiError) responseEntity.getBody()).getErrorCode()).isEqualTo(ErrorCode.INTERNAL_SERVER_ERROR);
		then(((ApiError) responseEntity.getBody()).getErrors()).containsExactly(ERROR_OCCURED);
		then(responseEntity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);

	}

	private UserAccountNotFoundException givenUserAccountNotFoundException() {
		UserAccountNotFoundException exception = Mockito.mock(UserAccountNotFoundException.class);
		given(exception.getMessage()).willReturn(SOME_MESSAGE);

		return exception;
	}

	private InvalidTransporterVehiculeException givenInvalidTransporterVehiculeException() {
		InvalidTransporterVehiculeException exception = Mockito.mock(InvalidTransporterVehiculeException.class);
		given(exception.getMessage()).willReturn(SOME_MESSAGE);

		return exception;
	}

	private CountryNotFoundException givenCountryNotFoundException() {
		CountryNotFoundException exception = Mockito.mock(CountryNotFoundException.class);
		given(exception.getMessage()).willReturn(SOME_MESSAGE);

		return exception;
	}

	private UserMobileNumberValidationException givenUserMobileNumberValidationException() {

		UserMobileNumberValidationException exception = Mockito.mock(UserMobileNumberValidationException.class);
		given(exception.getMessage()).willReturn(SOME_MESSAGE);

		return exception;
	}

	private UserEmailValidationException givenUserEmailValidationException() {

		UserEmailValidationException exception = Mockito.mock(UserEmailValidationException.class);
		given(exception.getMessage()).willReturn(SOME_MESSAGE);

		return exception;
	}

	private UserAccountAlreadyExistsException givenUserAccountAlreadyExistsException() {

		UserAccountAlreadyExistsException exception = Mockito.mock(UserAccountAlreadyExistsException.class);
		given(exception.getMessage()).willReturn(SOME_MESSAGE);

		return exception;
	}

	private UnsupportedInternationalCallingCode givenUnsupportedInternationalCallingCode() {
		UnsupportedInternationalCallingCode exception = Mockito.mock(UnsupportedInternationalCallingCode.class);
		given(exception.getMessage()).willReturn(SOME_MESSAGE);

		return exception;
	}

	private AuthorizationException givenAuthorizationException() {
		AuthorizationException exception = Mockito.mock(AuthorizationException.class);
		given(exception.getMessage()).willReturn(SOME_MESSAGE);

		return exception;
	}

	private JourneyRequestNotFoundException givenJourneyRequestNotFoundException() {
		JourneyRequestNotFoundException exception = Mockito.mock(JourneyRequestNotFoundException.class);
		given(exception.getMessage()).willReturn(SOME_MESSAGE);

		return exception;
	}

	private JourneyRequestExpiredException givenJourneyRequestExpiredException() {
		JourneyRequestExpiredException exception = Mockito.mock(JourneyRequestExpiredException.class);
		given(exception.getMessage()).willReturn(SOME_MESSAGE);

		return exception;
	}

	private Exception givenAnyOtherException() {
		Exception exception = Mockito.mock(NullPointerException.class);

		return exception;
	}

	private HttpMediaTypeNotSupportedException givenHttpMediaTypeNotSupportedExceptionWith1Error() {
		HttpMediaTypeNotSupportedException exception = Mockito.mock(HttpMediaTypeNotSupportedException.class);
		given(exception.getContentType()).willReturn(MediaType.APPLICATION_XML);
		given(exception.getSupportedMediaTypes())
				.willReturn(java.util.Arrays.asList(MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED));

		return exception;
	}

	private HttpRequestMethodNotSupportedException givenHttpRequestMethodNotSupportedExceptionWith1Error() {
		HttpRequestMethodNotSupportedException exception = Mockito.mock(HttpRequestMethodNotSupportedException.class);
		given(exception.getMethod()).willReturn(HttpMethod.DELETE.toString());
		given(exception.getSupportedHttpMethods())
				.willReturn(new HashSet<HttpMethod>(java.util.Arrays.asList(HttpMethod.GET, HttpMethod.POST)));

		return exception;
	}

	private NoHandlerFoundException givenNoHandlerFoundExceptionWith1Error() {
		NoHandlerFoundException exception = Mockito.mock(NoHandlerFoundException.class);
		given(exception.getHttpMethod()).willReturn(HttpMethod.GET.toString());
		given(exception.getRequestURL()).willReturn(SOME_REQUEST_URL);
		return exception;
	}

	private ConstraintViolationException givenConstraintViolationExceptionWith2ConstraintViolationsErrors() {

		ConstraintViolationException exception = Mockito.mock(ConstraintViolationException.class);

		Set<ConstraintViolation<?>> constraintViolations = new HashSet<>();

		ConstraintViolation<SomeObject> constraintViolation = Mockito.mock(ConstraintViolation.class);

		// given(constraintViolation.getRootBeanClass()).willReturn(SomeObject.class);

		Path path = Mockito.mock(Path.class);

		given(path.toString()).willReturn(SOME_PATH);

		given(constraintViolation.getPropertyPath()).willReturn(path);

		given(constraintViolation.getMessage()).willReturn(SOME_MESSAGE);

		constraintViolations.add(constraintViolation);

		given(exception.getConstraintViolations()).willReturn(constraintViolations);

		return exception;

	}

	private MissingServletRequestParameterException givenMissingServletRequestParameterExceptionWith1Error() {
		MissingServletRequestParameterException exception = Mockito.mock(MissingServletRequestParameterException.class);

		return exception;
	}

	private MissingServletRequestPartException givenMissingServletRequestPartExceptionWith1Error() {
		MissingServletRequestPartException exception = Mockito.mock(MissingServletRequestPartException.class);
		given(exception.getRequestPartName()).willReturn(REQUEST_PART);

		return exception;
	}

	private TypeMismatchException givenTypeMismatchExceptionWith1Error() {

		TypeMismatchException exception = Mockito.mock(TypeMismatchException.class);
		given(exception.getValue()).willReturn(VAL_DOUBLE);
		doReturn(Long.class).when(exception).getRequiredType();
		given(exception.getPropertyName()).willReturn(FIELD_1);

		return exception;
	}

	private MethodArgumentTypeMismatchException givenMethodArgumentTypeMismatchExceptionWith1Error() {
		MethodArgumentTypeMismatchException exception = Mockito.mock(MethodArgumentTypeMismatchException.class);
		given(exception.getName()).willReturn(FIELD_1);
		doReturn(Long.class).when(exception).getRequiredType();

		return exception;
	}

	private BindException givenBindExceptionWith2FieldErrorsAnd2GlobalErrors() {

		BindingResult bindingResult = Mockito.mock(BindingResult.class);

		MyBindException exception = new MyBindException(bindingResult);

		List<FieldError> fieldErrors = new ArrayList<FieldError>();
		fieldErrors.add(new FieldError(OBJECT_TO_VALIDATE, FIELD_1, FIELD_ERROR_1));
		fieldErrors.add(new FieldError(OBJECT_TO_VALIDATE, FIELD_2, FIELD_ERROR_2));

		given(bindingResult.getFieldErrors()).willReturn(fieldErrors);

		List<ObjectError> objectErrors = new ArrayList<ObjectError>();
		objectErrors.add(new ObjectError(OBJECT_TO_VALIDATE, GLOBAL_ERROR_1));
		objectErrors.add(new ObjectError(OBJECT_TO_VALIDATE, GLOBAL_ERROR_2));

		given(bindingResult.getGlobalErrors()).willReturn(objectErrors);

		return exception;
	}

	private MethodArgumentNotValidException givenMethodArgumentNotValidExceptionWith2FieldErrorsAnd2GlobalErrors() {

		MethodArgumentNotValidException exception = Mockito.mock(MethodArgumentNotValidException.class);

		BindingResult bindingResult = Mockito.mock(BindingResult.class);

		given(exception.getBindingResult()).willReturn(bindingResult);

		List<FieldError> fieldErrors = new ArrayList<FieldError>();
		fieldErrors.add(new FieldError(OBJECT_TO_VALIDATE, FIELD_1, FIELD_ERROR_1));
		fieldErrors.add(new FieldError(OBJECT_TO_VALIDATE, FIELD_2, FIELD_ERROR_2));

		given(bindingResult.getFieldErrors()).willReturn(fieldErrors);

		List<ObjectError> objectErrors = new ArrayList<ObjectError>();
		objectErrors.add(new ObjectError(OBJECT_TO_VALIDATE, GLOBAL_ERROR_1));
		objectErrors.add(new ObjectError(OBJECT_TO_VALIDATE, GLOBAL_ERROR_2));

		given(bindingResult.getGlobalErrors()).willReturn(objectErrors);

		return exception;

	}

}
