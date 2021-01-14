package com.excentria_it.wamya.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Value;

@Value
@JsonDeserialize(builder = LoadConstructorsDto.Builder.class)
public class LoadConstructorsDto {
	private Long id;
	private String name;

	@JsonPOJOBuilder
	static class Builder {
		Long id;

		String name;

		Builder id(Long id) {
			this.id = id;
			return this;
		}

		Builder name(String name) {
			this.name = name;
			return this;
		}

		public LoadConstructorsDto build() {
			return new LoadConstructorsDto(id, name);
		}
	}
}
