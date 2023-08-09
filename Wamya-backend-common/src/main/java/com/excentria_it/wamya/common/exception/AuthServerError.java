package com.excentria_it.wamya.common.exception;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthServerError {

	private String error;

	@JsonProperty("error_description")
	private String errorDescription;
}
