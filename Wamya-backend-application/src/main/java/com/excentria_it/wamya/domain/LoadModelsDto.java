package com.excentria_it.wamya.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Value;

@Value
@JsonDeserialize(builder = LoadModelsDto.Builder.class)
public class LoadModelsDto {

	private Long id;

	private String name;

	private Double length;

	private Double width;

	private Double height;

	@JsonPOJOBuilder
	static class Builder {
		Long id;

		String name;

		Double length;

		Double width;

		Double height;

		Builder id(Long id) {
			this.id = id;
			return this;
		}

		Builder name(String name) {
			this.name = name;
			return this;
		}

		Builder length(Double length) {
			this.length = length;
			return this;
		}

		Builder width(Double width) {
			this.width = width;
			return this;
		}

		Builder height(Double height) {
			this.height = height;
			return this;
		}

		public LoadModelsDto build() {
			return new LoadModelsDto(id, name, length, width, height);
		}
	}
}
