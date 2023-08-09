package com.excentria_it.wamya.domain;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Value;

@Value
@JsonDeserialize(builder = LoadLocalesResult.Builder.class)
public class LoadLocalesResult {
	private Integer totalElements;
	private List<LoadLocalesDto> content;

	@JsonPOJOBuilder
	static class Builder {
		Integer totalElements;

		List<LoadLocalesDto> content;

		Builder withTotalElements(Integer totalElements) {
			this.totalElements = totalElements;
			return this;
		}

		Builder withContent(List<LoadLocalesDto> content) {
			this.content = content;
			return this;
		}

		public LoadLocalesResult build() {
			return new LoadLocalesResult(totalElements, content);
		}
	}
}
