package com.excentria_it.wamya.common.exception.handlers;

import com.excentria_it.wamya.common.exception.*;
import com.excentria_it.wamya.common.exception.ApiError.ErrorCode;
import lombok.extern.slf4j.Slf4j;
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

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
@Slf4j
public class RestApiExceptionHandler extends ResponseEntityExceptionHandler {

    // 400

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex,
                                                                  final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        log.warn("Exception at " + ex.getClass().getName() + ": ", ex);

        //
        final List<String> errors = new ArrayList<>();
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

        final List<String> errors = new ArrayList<>();
        for (final FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (final ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }
        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ErrorCode.BIND_ERROR, errors);
        return handleExceptionInternal(ex, apiError, headers, apiError.getStatus(), request);
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(final MethodArgumentTypeMismatchException ex,
                                                                   final WebRequest request) {
        log.warn("Exception at " + ex.getClass().getName() + ": ", ex);

        final String error = ex.getName() + " should be of type " + ex.getRequiredType().getName();

        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ErrorCode.TYPE_MISMATCH, error);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(final TypeMismatchException ex, final HttpHeaders headers,
                                                        final HttpStatus status, final WebRequest request) {
        log.warn("Exception at " + ex.getClass().getName() + ": ", ex);

        final String error = ex.getValue() + " value for " + ex.getPropertyName() + " should be of type "
                + ex.getRequiredType();

        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ErrorCode.TYPE_MISMATCH, error);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(final MissingServletRequestPartException ex,
                                                                     final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        log.warn("Exception at " + ex.getClass().getName() + ": ", ex);

        final String error = ex.getRequestPartName() + " part is missing";
        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ErrorCode.MISSING_PART, error);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            final MissingServletRequestParameterException ex, final HttpHeaders headers, final HttpStatus status,
            final WebRequest request) {
        log.warn("Exception at " + ex.getClass().getName() + ": ", ex);

        final String error = ex.getParameterName() + " parameter is missing";
        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ErrorCode.MISSING_PARAMETER, error);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    //

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Object> handleConstraintViolation(final ConstraintViolationException ex,
                                                            final WebRequest request) {
        log.warn("Exception at " + ex.getClass().getName() + ": ", ex);

        final List<String> errors = new ArrayList<>();
        for (final ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.add(violation.getPropertyPath() + ": " + violation.getMessage());
        }

        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ErrorCode.VALIDATION_ERROR, errors);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    // 404

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(final NoHandlerFoundException ex,
                                                                   final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        log.warn("Exception at " + ex.getClass().getName() + ": ", ex);

        final String error = "No handler found for " + ex.getHttpMethod() + " " + ex.getRequestURL();

        final ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ErrorCode.NOT_FOUND, error);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
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
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
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
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    // 500

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleAll(final Exception ex, final WebRequest request) {
        log.warn("Exception at " + ex.getClass().getName() + ": ", ex);

        final ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.INTERNAL_SERVER_ERROR,
                "error occurred.");
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler({UserAccountNotFoundException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ApiError> handleUserAccountNotFoundException(UserAccountNotFoundException exception) {
        log.warn("Exception at {} : {}", exception.getClass().getName(), exception.getMessage());

        final ApiError apiError = new ApiError(HttpStatus.UNAUTHORIZED, ErrorCode.AUTHORIZATION,
                "Bad credentials.");
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());

    }

    @ExceptionHandler({UserMobileNumberValidationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiError> handleUserMobileNumberValidationException(
            UserMobileNumberValidationException exception) {
        log.warn("Exception at {} : {}", exception.getClass().getName(), exception.getMessage());
        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ErrorCode.MOBILE_VALIDATION,
                exception.getMessage());
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler({UserEmailValidationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiError> handleUserEmailValidationException(UserEmailValidationException exception) {
        log.warn("Exception at {} : {}", exception.getClass().getName(), exception.getMessage());

        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ErrorCode.EMAIL_VALIDATION,
                exception.getMessage());
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler({UserAccountAlreadyExistsException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiError> handleUserAccountAlreadyExistsException(
            UserAccountAlreadyExistsException exception) {

        log.warn("Exception at {} : {}", exception.getClass().getName(), exception.getMessage());
        final String error = "User account already exists.";
        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ErrorCode.ACCOUNT_EXISTS, error);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());

    }

    @ExceptionHandler({UnsupportedInternationalCallingCodeException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiError> handleUnsupportedInternationalCallingCode(
            UnsupportedInternationalCallingCodeException exception) {

        log.warn("Exception at {} : {}", exception.getClass().getName(), exception.getMessage());
        final String error = "International calling code is not supported.";
        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ErrorCode.VALIDATION_ERROR, error);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());

    }

    @ExceptionHandler({AuthorizationException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ApiError> handleAuthorizationException(AuthorizationException exception) {

        log.warn("Exception at {} : {}", exception.getClass().getName(), exception.getMessage());
        final String error = "Bad credentials.";
        final ApiError apiError = new ApiError(HttpStatus.UNAUTHORIZED, ErrorCode.AUTHORIZATION, error);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());

    }

    @ExceptionHandler({JourneyRequestNotFoundException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiError> handleJourneyRequestNotFoundException(JourneyRequestNotFoundException exception) {

        log.warn("Exception at {} : {}", exception.getClass().getName(), exception.getMessage());
        final String error = exception.getMessage();
        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ErrorCode.OBJECT_NOT_FOUND, error);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());

    }

    @ExceptionHandler({JourneyRequestExpiredException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiError> handleJourneyRequestExpiredException(JourneyRequestExpiredException exception) {

        log.warn("Exception at {} : {}", exception.getClass().getName(), exception.getMessage());
        final String error = exception.getMessage();
        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ErrorCode.VALIDATION_ERROR, error);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());

    }

    @ExceptionHandler({InvalidTransporterVehicleException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiError> handleInvalidTransporterVehicleException(
            InvalidTransporterVehicleException exception) {

        log.warn("Exception at {} : {}", exception.getClass().getName(), exception.getMessage());
        final String error = exception.getMessage();
        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ErrorCode.VALIDATION_ERROR, error);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());

    }

    @ExceptionHandler({CountryNotFoundException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiError> handleCountryNotFoundException(CountryNotFoundException exception) {

        log.warn("Exception at {} : {}", exception.getClass().getName(), exception.getMessage());
        final String error = exception.getMessage();
        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ErrorCode.VALIDATION_ERROR, error);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());

    }

    @ExceptionHandler({InvalidPlaceTypeException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiError> handleInvalidPlaceTypeException(InvalidPlaceTypeException exception) {

        log.warn("Exception at {} : {}", exception.getClass().getName(), exception.getMessage());
        final String error = exception.getMessage();
        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ErrorCode.VALIDATION_ERROR, error);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler({DepartmentNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ApiError> handleDepartmentNotFoundException(DepartmentNotFoundException exception) {

        log.warn("Exception at {} : {}", exception.getClass().getName(), exception.getMessage());
        final String error = exception.getMessage();
        final ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ErrorCode.OBJECT_NOT_FOUND, error);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler({OperationDeniedException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiError> handleOperationDeniedException(OperationDeniedException exception) {

        log.warn("Exception at {} : {}", exception.getClass().getName(), exception.getMessage());
        final String error = exception.getMessage();
        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ErrorCode.VALIDATION_ERROR, error);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler({JourneyProposalNotFoundException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiError> handleJourneyProposalNotFoundException(JourneyProposalNotFoundException exception) {

        log.warn("Exception at {} : {}", exception.getClass().getName(), exception.getMessage());
        final String error = exception.getMessage();
        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ErrorCode.OBJECT_NOT_FOUND, error);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler({DiscussionNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ApiError> handleDiscussionNotFoundException(DiscussionNotFoundException exception) {

        log.warn("Exception at {} : {}", exception.getClass().getName(), exception.getMessage());
        final String error = exception.getMessage();
        final ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ErrorCode.OBJECT_NOT_FOUND, error);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler({GenderNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ApiError> handleGenderNotFoundException(GenderNotFoundException exception) {

        log.warn("Exception at {} : {}", exception.getClass().getName(), exception.getMessage());
        final String error = exception.getMessage();
        final ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ErrorCode.OBJECT_NOT_FOUND, error);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler({DocumentAccessException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ApiError> handleDocumentAccessException(DocumentAccessException exception) {

        log.warn("Exception at {} : {}", exception.getClass().getName(), exception.getMessage());
        final String error = exception.getMessage();
        final ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.INTERNAL_SERVER_ERROR,
                error);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler({ForbiddenAccessException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<ApiError> handleForbiddenAccessException(ForbiddenAccessException exception) {

        log.warn("Exception at {} : {}", exception.getClass().getName(), exception.getMessage());
        final String error = exception.getMessage();
        final ApiError apiError = new ApiError(HttpStatus.FORBIDDEN, ErrorCode.AUTHORIZATION, error);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler({UnsupportedMimeTypeException.class})
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    public ResponseEntity<ApiError> handleUnsupportedMimeTypeException(UnsupportedMimeTypeException exception) {

        log.error("Exception at " + exception.getClass() + ": ", exception);

        final String error = exception.getMessage();
        final ApiError apiError = new ApiError(HttpStatus.UNSUPPORTED_MEDIA_TYPE, ErrorCode.UNSUPPORTED_MEDIA_TYPE,
                error);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler({VehicleNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ApiError> handleVehicleNotFoundException(VehicleNotFoundException exception) {

        log.warn("Exception at {} : {}", exception.getClass().getName(), exception.getMessage());
        final String error = exception.getMessage();
        final ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ErrorCode.OBJECT_NOT_FOUND, error);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler({LinkExpiredException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ApiError> handleLinkExpiredException(LinkExpiredException exception) {

        log.warn("Exception at {} : {}", exception.getClass().getName(), exception.getMessage());
        final String error = exception.getMessage();
        final ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ErrorCode.NOT_FOUND, error);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler({JourneyRequestUpdateException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiError> handleJourneyRequestUpdateException(JourneyRequestUpdateException exception) {

        log.warn("Exception at {} : {}", exception.getClass().getName(), exception.getMessage());
        final String error = exception.getMessage();
        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ErrorCode.VALIDATION_ERROR, error);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler({TransporterRatingRequestNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ApiError> handleTransporterRatingDetailsNotFoundException(TransporterRatingRequestNotFoundException exception) {

        log.warn("Exception at {} : {}", exception.getClass().getName(), exception.getMessage());
        final String error = exception.getMessage();
        final ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ErrorCode.NOT_FOUND, error);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler({UserCreationException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ApiError> handleUserCreationException(UserCreationException exception) {

        log.warn("Exception at {} : {}", exception.getClass().getName(), exception.getMessage());
        final String error = "Cannot create user. Please contact support.";
        final ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.INTERNAL_SERVER_ERROR, error);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }
}