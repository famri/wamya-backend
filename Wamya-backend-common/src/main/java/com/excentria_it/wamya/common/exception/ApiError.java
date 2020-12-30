package com.excentria_it.wamya.common.exception;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ApiError {

	private HttpStatus status;
	private ErrorCode errorCode;
	private List<String> errors;

	public ApiError(final HttpStatus status, ErrorCode errorCode, final String error) {
		super();
		this.errorCode = errorCode;
		this.status = status;
		errors = Arrays.asList(error);
	}

	public enum ErrorCode {
		INTERNAL_SERVER_ERROR, UNSUPPORTED_MEDIA_TYPE, METHOD_NOT_ALLOWED, NOT_FOUND, MOBILE_VALIDATION,
		EMAIL_VALIDATION, VALIDATION_ERROR, MISSING_PARAMETER, MISSING_PART, TYPE_MISMATCH, BIND_ERROR, ACCOUNT_EXISTS,
		AUTHORIZATION, OBJECT_NOT_FOUND;
	}

}