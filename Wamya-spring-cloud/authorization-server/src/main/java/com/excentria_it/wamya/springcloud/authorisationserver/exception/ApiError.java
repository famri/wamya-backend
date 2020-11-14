package com.excentria_it.wamya.springcloud.authorisationserver.exception;

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

	private List<String> errors;

	public ApiError(final HttpStatus status, final String error) {
		super();
		this.status = status;
		errors = Arrays.asList(error);
	}

	
	
}