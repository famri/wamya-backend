package com.excentria_it.wamya.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Value;

@Value
@JsonDeserialize(builder = AutoCompletePlaceDto.Builder.class)
public class AutoCompletePlaceDto {
	private Long id;

	private PlaceType type;

	private String name;

	private String delegation;

	private String department;

	private String country;

	@JsonPOJOBuilder
	static class Builder {
		Long id;

		PlaceType type;

		String name;

		String delegation;

		String department;

		String country;

		Builder withId(Long id) {
			this.id = id;
			return this;
		}

		Builder withType(PlaceType type) {
			this.type = type;
			return this;
		}

		Builder withName(String name) {
			this.name = name;
			return this;
		}

		Builder withDelegation(String delegation) {
			this.delegation = delegation;
			return this;
		}

		Builder withDepartment(String department) {
			this.department = department;
			return this;
		}

		Builder withCountry(String country) {
			this.country = country;
			return this;
		}

		public AutoCompletePlaceDto build() {
			return new AutoCompletePlaceDto(id, type, name, delegation, department, country);
		}
	}


}
