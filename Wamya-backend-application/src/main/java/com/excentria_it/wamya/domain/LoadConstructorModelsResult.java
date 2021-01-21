package com.excentria_it.wamya.domain;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.Value;

@Value
@JsonDeserialize(builder = LoadConstructorModelsResult.Builder.class)
public class LoadConstructorModelsResult {
	private Integer totalElements;
	private List<LoadModelsDto> content;

	static class Builder {
		Integer totalElements;

		List<LoadModelsDto> content;

		Builder withTotalElements(Integer totalElements) {
			this.totalElements = totalElements;
			return this;
		}

		Builder withContent(List<LoadModelsDto> content) {
			this.content = content;
			return this;
		}

		public LoadConstructorModelsResult build() {
			return new LoadConstructorModelsResult(totalElements, content);
		}
	}
}
