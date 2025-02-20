package com.excentria_it.wamya.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Value;

@Value
@JsonDeserialize(builder = LoadEngineTypesDto.Builder.class)
public class LoadEngineTypesDto {

	private Long id;

	private String name;

	private String code;

	private String description;

	@JsonPOJOBuilder
	static class Builder {
		Long id;

		String name;

		String code;

		String description;

		Builder withId(Long id) {
			this.id = id;
			return this;
		}

		Builder withName(String name) {
			this.name = name;
			return this;
		}

		Builder withCode(String code) {
			this.code = code;
			return this;
		}

		Builder withDescription(String description) {
			this.description = description;
			return this;
		}

		public LoadEngineTypesDto build() {
			return new LoadEngineTypesDto(id, name, code, description);
		}
	}

}
