package com.excentria_it.wamya.common.exception;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.excentria_it.wamya.common.exception.ApiError.ErrorCode;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class RestApiExceptionHandler extends ResponseEntityExceptionHandler {

	// 400

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex,
			final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
		log.warn("Exception at " + ex.getClass().getName() + ": ", ex);

		//
		final List<String> errors = new ArrayList<String>();
		for (final FieldError error : ex.getBindingResult().getFieldErrors()) {
			errors.add(error.getField() + ": " + error.getDefaultMessage());
		}
		for (final ObjectError error : ex.getBindingResult().getGlobalErrors()) {
			errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
		}

		final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ErrorCode.VALIDATION_ERROR, errors);
		return handleExceptionInternal(ex, apiError, headers, apiError.getStatus(), request);
	}

	@Override
	protected ResponseEntity<Object> handleBindException(final BindException ex, final HttpHeaders headers,
			final HttpStatus status, final WebRequest request) {
		log.warn("Exception at " + ex.getClass().getName() + ": ", ex);

		final List<String> errors = new ArrayList<String>();
		for (final FieldError error : ex.getBindingResult().getFieldErrors()) {
			errors.add(error.getField() + ": " + error.getDefaultMessage());
		}
		for (final ObjectError error : ex.getBindingResult().getGlobalErrors()) {
			errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
		}
		final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ErrorCode.BIND_ERROR, errors);
		return handleExceptionInternal(ex, apiError, headers, apiError.getStatus(), request);
	}

	@ExceptionHandler({ MethodArgumentTypeMismatchException.class })
	public ResponseEntity<Object> handleMethodArgumentTypeMismatch(final MethodArgumentTypeMismatchException ex,
			final WebRequest request) {
		log.warn("Exception at " + ex.getClass().getName() + ": ", ex);

		final String error = ex.getName() + " should be of type " + ex.getRequiredType().getName();

		final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ErrorCode.TYPE_MISMATCH, error);
		return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
	}

	@Override
	protected ResponseEntity<Object> handleTypeMismatch(final TypeMismatchException ex, final HttpHeaders headers,
			final HttpStatus status, final WebRequest request) {
		log.warn("Exception at " + ex.getClass().getName() + ": ", ex);

		final String error = ex.getValue() + " value for " + ex.getPropertyName() + " should be of type "
				+ ex.getRequiredType();

		final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ErrorCode.TYPE_MISMATCH, error);
		return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
	}

	@Override
	protected ResponseEntity<Object> handleMissingServletRequestPart(final MissingServletRequestPartException ex,
			final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
		log.warn("Exception at " + ex.getClass().getName() + ": ", ex);

		final String error = ex.getRequestPartName() + " part is missing";
		final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ErrorCode.MISSING_PART, error);
		return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
	}

	@Override
	protected ResponseEntity<Object> handleMissingServletRequestParameter(
			final MissingServletRequestParameterException ex, final HttpHeaders headers, final HttpStatus status,
			final WebRequest request) {
		log.warn("Exception at " + ex.getClass().getName() + ": ", ex);

		final String error = ex.getParameterName() + " parameter is missing";
		final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ErrorCode.MISSING_PARAMETER, error);
		return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
	}

	//

	@ExceptionHandler({ ConstraintViolationException.class })
	public ResponseEntity<Object> handleConstraintViolation(final ConstraintViolationException ex,
			final WebRequest request) {
		log.warn("Exception at " + ex.getClass().getName() + ": ", ex);

		final List<String> errors = new ArrayList<String>();
		for (final ConstraintViolation<?> violation : ex.getConstraintViolations()) {
			errors.add(violation.getPropertyPath() + ": " + violation.getMessage());
		}

		final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ErrorCode.VALIDATION_ERROR, errors);
		return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
	}

	// 404

	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(final NoHandlerFoundException ex,
			final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
		log.warn("Exception at " + ex.getClass().getName() + ": ", ex);

		final String error = "No handler found for " + ex.getHttpMethod() + " " + ex.getRequestURL();

		final ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ErrorCode.NOT_FOUND, error);
		return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
	}

	// 405

	@Override
	protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
			final HttpRequestMethodNotSupportedException ex, final HttpHeaders headers, final HttpStatus status,
			final WebRequest request) {
		log.warn("Exception at " + ex.getClass().getName() + ": ", ex);

		final StringBuilder builder = new StringBuilder();
		builder.append(ex.getMethod());
		builder.append(" method is not supported for this request. Supported methods are ");
		ex.getSupportedHttpMethods().forEach(t -> builder.append(t + " "));

		final ApiError apiError = new ApiError(HttpStatus.METHOD_NOT_ALLOWED, ErrorCode.METHOD_NOT_ALLOWED,
				builder.toString().trim());
		return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
	}

	// 415

	@Override
	protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(final HttpMediaTypeNotSupportedException ex,
			final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
		log.warn("Exception at " + ex.getClass().getName() + ": ", ex);

		final StringBuilder builder = new StringBuilder();
		builder.append(ex.getContentType());
		builder.append(" media type is not supported. Supported media types are ");
		ex.getSupportedMediaTypes().forEach(t -> builder.append(t + " "));

		final ApiError apiError = new ApiError(HttpStatus.UNSUPPORTED_MEDIA_TYPE, ErrorCode.UNSUPPORTED_MEDIA_TYPE,
				builder.toString().trim());
		return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
	}

	// 500

	@ExceptionHandler({ Exception.class })
	public ResponseEntity<Object> handleAll(final Exception ex, final WebRequest request) {
		log.error("Exception at " + ex.getClass().getName() + ": ", ex);

		final ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.INTERNAL_SERVER_ERROR,
				"error occurred.");
		return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getStatus());
	}

	@ExceptionHandler({ UserAccountNotFoundException.class })
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<ApiError> handleUserAccountNotFoundException(UserAccountNotFoundException exception) {
		log.warn("Exception at " + exception.getClass() + ": ", exception);

		final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ErrorCode.OBJECT_NOT_FOUND,
				exception.getMessage());
		return new ResponseEntity<ApiError>(apiError, new HttpHeaders(), apiError.getStatus());

	}

	@ExceptionHandler({ UserMobileNumberValidationException.class })
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<ApiError> handleUserMobileNumberValidationException(
			UserMobileNumberValidationException exception) {
		log.warn("Exception at " + exception.getClass() + ": ", exception);

		final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ErrorCode.MOBILE_VALIDATION,
				exception.getMessage());
		return new ResponseEntity<ApiError>(apiError, new HttpHeaders(), apiError.getStatus());
	}

	@ExceptionHandler({ UserEmailValidationException.class })
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<ApiError> handleUserEmailValidationException(UserEmailValidationException exception) {
		log.warn("Exception at " + exception.getClass() + ": ", exception);

		final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ErrorCode.EMAIL_VALIDATION,
				exception.getMessage());
		return new ResponseEntity<ApiError>(apiError, new HttpHeaders(), apiError.getStatus());
	}

	@ExceptionHandler({ UserAccountAlreadyExistsException.class })
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<ApiError> handleUserAccountAlreadyExistsException(
			UserAccountAlreadyExistsException exception) {

		log.warn("Exception at " + exception.getClass() + ": ", exception);
		final String error = "User account already exists.";
		final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ErrorCode.ACCOUNT_EXISTS, error);
		return new ResponseEntity<ApiError>(apiError, new HttpHeaders(), apiError.getStatus());

	}

	@ExceptionHandler({ UnsupportedInternationalCallingCode.class })
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<ApiError> handleUnsupportedInternationalCallingCode(
			UnsupportedInternationalCallingCode exception) {

		log.warn("Exception at " + exception.getClass() + ": ", exception);
		final String error = "International calling code is not supported.";
		final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ErrorCode.VALIDATION_ERROR, error);
		return new ResponseEntity<ApiError>(apiError, new HttpHeaders(), apiError.getStatus());

	}

	@ExceptionHandler({ AuthorizationException.class })
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public ResponseEntity<ApiError> handleAuthorizationException(AuthorizationException exception) {

		log.warn("Exception at " + exception.getClass() + ": ", exception);
		final String error = exception.getMessage();
		final ApiError apiError = new ApiError(HttpStatus.UNAUTHORIZED, ErrorCode.AUTHORIZATION, error);
		return new ResponseEntity<ApiError>(apiError, new HttpHeaders(), apiError.getStatus());

	}

	@ExceptionHandler({ JourneyRequestNotFoundException.class })
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<ApiError> handleJourneyRequestNotFoundException(JourneyRequestNotFoundException exception) {

		log.warn("Exception at " + exception.getClass() + ": ", exception);
		final String error = exception.getMessage();
		final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ErrorCode.OBJECT_NOT_FOUND, error);
		return new ResponseEntity<ApiError>(apiError, new HttpHeaders(), apiError.getStatus());

	}

	@ExceptionHandler({ JourneyRequestExpiredException.class })
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<ApiError> handleJourneyRequestExpiredException(JourneyRequestExpiredException exception) {

		log.warn("Exception at " + exception.getClass() + ": ", exception);
		final String error = exception.getMessage();
		final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ErrorCode.VALIDATION_ERROR, error);
		return new ResponseEntity<ApiError>(apiError, new HttpHeaders(), apiError.getStatus());

	}
	@ExceptionHandler({ InvalidTransporterVehiculeException.class })
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<ApiError> handleInvalidTransporterVehiculeException(
			InvalidTransporterVehiculeException exception) {

		log.warn("Exception at " + exception.getClass() + ": ", exception);
		final String error = exception.getMessage();
		final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ErrorCode.VALIDATION_ERROR, error);
		return new ResponseEntity<ApiError>(apiError, new HttpHeaders(), apiError.getStatus());

	}
}