package com.excentria_it.wamya.domain;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Value;

@Value
@JsonDeserialize(builder = LoadConstructorsResult.Builder.class)
public class LoadConstructorsResult {
	private Integer totalElements;
	private List<LoadConstructorsDto> content;

	@JsonPOJOBuilder
	static class Builder {
		Integer totalElements;

		List<LoadConstructorsDto> content;

		Builder withTotalElements(Integer totalElements) {
			this.totalElements = totalElements;
			return this;
		}

		Builder withContent(List<LoadConstructorsDto> content) {
			this.content = content;
			return this;
		}

		public LoadConstructorsResult build() {
			return new LoadConstructorsResult(totalElements, content);
		}
	}

}
