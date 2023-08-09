package com.excentria_it.wamya.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Value;

@Value
@JsonDeserialize(builder = CodeValidationResult.Builder.class)
public class CodeValidationResult {
	private boolean valid;

	@JsonPOJOBuilder
	static class Builder {
		boolean valid;

		Builder withValid(boolean isValid) {
			this.valid = isValid;
			return this;
		}

		public CodeValidationResult build() {
			return new CodeValidationResult(valid);
		}
	}
}
