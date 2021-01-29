package com.excentria_it.wamya.domain;

import java.util.List;

import com.excentria_it.wamya.domain.AutoCompleteDepartmentsResult.Builder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Value;

@Value
@JsonDeserialize(builder = AutoCompleteLocalitiesResult.Builder.class)
public class AutoCompleteLocalitiesResult {
	private Integer totalElements;
	private List<AutoCompleteLocalitiesDto> content;

	@JsonPOJOBuilder
	static class Builder {
		Integer totalElements;

		List<AutoCompleteLocalitiesDto> content;

		Builder withTotalElements(Integer totalElements) {
			this.totalElements = totalElements;
			return this;
		}

		Builder withContent(List<AutoCompleteLocalitiesDto> content) {
			this.content = content;
			return this;
		}

		public AutoCompleteLocalitiesResult build() {
			return new AutoCompleteLocalitiesResult(totalElements, content);
		}
	}
}
