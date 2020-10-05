package com.excentria_it.wamya.adapter.web.exception;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import com.excentria_it.wamya.adapter.web.exception.ApiError;

public class ApiErrorTest {
	private static final String ERROR = "A dummy error";
	private static final String MESSAGE = "A dummy message";

	@Test
	void whenConsutructor_thenFieldsInitialized(){
		ApiError apiError = new ApiError(HttpStatus.OK, MESSAGE, ERROR);
		
		assertThat(apiError.getErrors()).hasSize(1);
		assertThat(apiError.getErrors().get(0)).isEqualTo(ERROR);
		
		assertThat(apiError.getMessage()).isEqualTo(MESSAGE);
		
		assertThat(apiError.getStatus()).isEqualTo(HttpStatus.OK);
		
	}
}
