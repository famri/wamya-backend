package com.excentria_it.wamya.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Value;

@Value
@JsonDeserialize(builder = LoadGendersDto.Builder.class)
public class LoadGendersDto {
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

		public LoadGendersDto build() {
			return new LoadGendersDto(id, name);
		}
	}

}
