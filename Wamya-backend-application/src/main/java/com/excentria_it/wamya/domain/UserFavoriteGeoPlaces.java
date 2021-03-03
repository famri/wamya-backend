package com.excentria_it.wamya.domain;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lombok.Value;

@Value
@JsonDeserialize(builder = UserFavoriteGeoPlaces.Builder.class)
public class UserFavoriteGeoPlaces {

	private Integer totalElements;
	private List<GeoPlaceDto> content;

	static class Builder {
		Integer totalElements;

		List<GeoPlaceDto> content;

		Builder withTotalElements(Integer totalElements) {
			this.totalElements = totalElements;
			return this;
		}

		Builder withContent(List<GeoPlaceDto> content) {
			this.content = content;
			return this;
		}

		public UserFavoriteGeoPlaces build() {
			return new UserFavoriteGeoPlaces(totalElements, content);
		}
	}
}
