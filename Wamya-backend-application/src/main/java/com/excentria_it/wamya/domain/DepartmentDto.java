package com.excentria_it.wamya.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Value;

@Value
@JsonDeserialize(builder = DepartmentDto.Builder.class)
public class DepartmentDto {
	private Long id;

	private String name;

	@JsonPOJOBuilder
	static class Builder {
		Long id;

		String name;

		Builder withId(Long id) {
			this.id = id;
			return this;
		}

		Builder withName(String name) {
			this.name = name;
			return this;
		}

		public DepartmentDto build() {
			return new DepartmentDto(id, name);
		}
	}
}