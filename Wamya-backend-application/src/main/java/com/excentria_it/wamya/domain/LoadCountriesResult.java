package com.excentria_it.wamya.domain;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Value;

@Value
@JsonDeserialize(builder = LoadCountriesResult.Builder.class)
public class LoadCountriesResult {
	private Integer totalElements;
	private List<LoadCountriesDto> content;

	@JsonPOJOBuilder
	static class Builder {
		Integer totalElements;

		List<LoadCountriesDto> content;

		Builder withTotalElements(Integer totalElements) {
			this.totalElements = totalElements;
			return this;
		}

		Builder withContent(List<LoadCountriesDto> content) {
			this.content = content;
			return this;
		}

		public LoadCountriesResult build() {
			return new LoadCountriesResult(totalElements, content);
		}
	}
}
