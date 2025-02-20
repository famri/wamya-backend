package com.excentria_it.wamya.common.exception;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import com.excentria_it.wamya.common.exception.ApiError.ErrorCode;

public class ApiErrorTest {
	private static final String ERROR = "A dummy error";

	@Test
	void whenConsutructor_thenFieldsInitialized() {
		ApiError apiError = new ApiError(HttpStatus.OK, ErrorCode.AUTHORIZATION, ERROR);

		assertThat(apiError.getErrors()).hasSize(1);
		assertThat(apiError.getErrors().get(0)).isEqualTo(ERROR);
		assertThat(apiError.getErrorCode()).isEqualTo(ErrorCode.AUTHORIZATION);
		assertThat(apiError.getStatus()).isEqualTo(HttpStatus.OK);

	}
}
