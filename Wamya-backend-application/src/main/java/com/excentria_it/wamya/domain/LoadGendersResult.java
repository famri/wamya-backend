package com.excentria_it.wamya.domain;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Value;

@Value
@JsonDeserialize(builder = LoadGendersResult.Builder.class)
public class LoadGendersResult {

	private Integer totalElements;
	private List<LoadGendersDto> content;

	@JsonPOJOBuilder
	static class Builder {
		Integer totalElements;

		List<LoadGendersDto> content;

		Builder withTotalElements(Integer totalElements) {
			this.totalElements = totalElements;
			return this;
		}

		Builder withContent(List<LoadGendersDto> content) {
			this.content = content;
			return this;
		}

		public LoadGendersResult build() {
			return new LoadGendersResult(totalElements, content);
		}
	}
}
