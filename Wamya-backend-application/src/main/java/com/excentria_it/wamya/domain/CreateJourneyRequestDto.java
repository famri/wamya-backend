package com.excentria_it.wamya.domain;

import java.math.BigDecimal;
import java.time.Instant;

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

	private Integer distance;

	private Integer hours;

	private Integer minutes;

	private Instant dateTime;

	private EngineTypeDto engineType;

	private Integer workers;

	private String description;

	private String status;

	private Instant creationDateTime;

	@AllArgsConstructor
	@Data
	public static class EngineTypeDto {

		private Long id;

		private String name;

	}

	@AllArgsConstructor
	@Data
	public static class PlaceDto {

		private Long id;

		private PlaceType type;

		private BigDecimal latitude;

		private BigDecimal longitude;

	}

	@AllArgsConstructor
	@Data
	public static class ClientDto {

		private Long id;

		private String firstname;

		private String photoUrl;
	}
}
