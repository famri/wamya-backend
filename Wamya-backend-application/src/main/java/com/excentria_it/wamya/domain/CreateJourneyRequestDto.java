package com.excentria_it.wamya.domain;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CreateJourneyRequestDto {

	private Long id;

	private PlaceDto departurePlace;

	private PlaceDto arrivalPlace;

	private Double distance;

	private LocalDateTime dateTime;

	private LocalDateTime endDateTime;

	private EngineTypeDto engineType;

	private Integer workers;

	private String description;

	@AllArgsConstructor
	@Data
	public static class EngineTypeDto {

		private Long id;

		private String name;

	}

	@AllArgsConstructor
	@Data

	public static class PlaceDto {

		private String placeId;

		private String placeRegionId;

		private String placeName;
	}

	@AllArgsConstructor
	@Data
	public static class ClientDto {

		private Long id;

		private String firstname;

		private String photoUrl;
	}
}
