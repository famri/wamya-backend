package com.excentria_it.wamya.domain;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Value;

@Value
@JsonDeserialize(builder = LoadEngineTypesResult.Builder.class)
public class LoadEngineTypesResult {
	private Integer totalElements;
	private List<LoadEngineTypesDto> content;

	@JsonPOJOBuilder
	static class Builder {
		Integer totalElements;

		List<LoadEngineTypesDto> content;

		Builder withTotalElements(Integer totalElements) {
			this.totalElements = totalElements;
			return this;
		}

		Builder withContent(List<LoadEngineTypesDto> content) {
			this.content = content;
			return this;
		}

		public LoadEngineTypesResult build() {
			return new LoadEngineTypesResult(totalElements, content);
		}
	}
}
