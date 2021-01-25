package com.excentria_it.wamya.domain;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Value;

@Value
@JsonDeserialize(builder = AutoCompleteDepartmentsResult.Builder.class)
public class AutoCompleteDepartmentsResult {
	private Integer totalElements;
	private List<AutoCompleteDepartmentsDto> content;

	@JsonPOJOBuilder
	static class Builder {
		Integer totalElements;

		List<AutoCompleteDepartmentsDto> content;

		Builder withTotalElements(Integer totalElements) {
			this.totalElements = totalElements;
			return this;
		}

		Builder withContent(List<AutoCompleteDepartmentsDto> content) {
			this.content = content;
			return this;
		}

		public AutoCompleteDepartmentsResult build() {
			return new AutoCompleteDepartmentsResult(totalElements, content);
		}
	}
}
