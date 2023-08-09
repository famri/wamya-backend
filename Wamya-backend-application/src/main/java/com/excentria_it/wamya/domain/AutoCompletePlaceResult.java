package com.excentria_it.wamya.domain;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Value;

@Value
@JsonDeserialize(builder = AutoCompletePlaceResult.Builder.class)
public class AutoCompletePlaceResult {
	private Integer totalElements;
	private List<AutoCompletePlaceDto> content;

	@JsonPOJOBuilder
	static class Builder {
		Integer totalElements;

		List<AutoCompletePlaceDto> content;

		Builder withTotalElements(Integer totalElements) {
			this.totalElements = totalElements;
			return this;
		}

		Builder withContent(List<AutoCompletePlaceDto> content) {
			this.content = content;
			return this;
		}

		public AutoCompletePlaceResult build() {
			return new AutoCompletePlaceResult(totalElements, content);
		}
	}
}
