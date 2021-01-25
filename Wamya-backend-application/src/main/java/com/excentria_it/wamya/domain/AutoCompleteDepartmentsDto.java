package com.excentria_it.wamya.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Value;

@Value
@JsonDeserialize(builder = AutoCompleteDepartmentsDto.Builder.class)
public class AutoCompleteDepartmentsDto {

	private Long id;

	private String name;

	private String country;

	@JsonPOJOBuilder
	static class Builder {
		Long id;

		String name;

		String country;

		Builder withId(Long id) {
			this.id = id;
			return this;
		}

		Builder withName(String name) {
			this.name = name;
			return this;
		}

		Builder withCountry(String country) {
			this.country = country;
			return this;
		}

		public AutoCompleteDepartmentsDto build() {
			return new AutoCompleteDepartmentsDto(id, name, country);
		}
	}
}
