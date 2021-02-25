package com.excentria_it.wamya.domain;

import java.math.BigDecimal;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Value;

@Value
@JsonDeserialize(builder = GeoPlaceDto.Builder.class)
public class GeoPlaceDto {
	private Long id;
	private String name;
	private BigDecimal latitude;
	private BigDecimal longitude;
	private DepartmentDto department;

	@JsonPOJOBuilder
	static class Builder {
		Long id;

		String name;

		BigDecimal latitude;

		BigDecimal longitude;

		DepartmentDto department;

		Builder withId(Long id) {
			this.id = id;
			return this;
		}

		Builder withName(String name) {
			this.name = name;
			return this;
		}

		Builder withLatitude(BigDecimal latitude) {
			this.latitude = latitude;
			return this;
		}

		Builder withLongitude(BigDecimal longitude) {
			this.longitude = longitude;
			return this;
		}

		Builder withDepartment(DepartmentDto department) {
			this.department = department;
			return this;
		}

		public GeoPlaceDto build() {
			return new GeoPlaceDto(id, name, latitude, longitude, department);
		}
	}


}
