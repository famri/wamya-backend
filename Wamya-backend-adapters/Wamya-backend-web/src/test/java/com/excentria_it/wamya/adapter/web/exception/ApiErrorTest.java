package com.excentria_it.wamya.adapter.web.exception;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class ApiErrorTest {
	private static final String ERROR = "A dummy error";

	@Test
	void whenConsutructor_thenFieldsInitialized() {
		ApiError apiError = new ApiError(HttpStatus.OK, ERROR);

		assertThat(apiError.getErrors()).hasSize(1);
		assertThat(apiError.getErrors().get(0)).isEqualTo(ERROR);

		assertThat(apiError.getStatus()).isEqualTo(HttpStatus.OK);

	}
}
