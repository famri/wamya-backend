package com.excentria_it.wamya.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Value;

@Value
@JsonDeserialize(builder = LoadLocalesDto.Builder.class)
public class LoadLocalesDto {

	private Long id;

	private String name;

	private String countryCode;

	private String languageCode;

	@JsonPOJOBuilder
	static class Builder {
		Long id;

		String name;

		String countryCode;

		String languageCode;

		Builder withId(Long id) {
			this.id = id;
			return this;
		}

		Builder withName(String name) {
			this.name = name;
			return this;
		}

		Builder withLanguageCode(String languageCode) {
			this.languageCode = languageCode;
			return this;
		}

		Builder withCountryCode(String countryCode) {
			this.countryCode = countryCode;
			return this;
		}

		public LoadLocalesDto build() {
			return new LoadLocalesDto(id, name, countryCode, languageCode);
		}
	}

}
