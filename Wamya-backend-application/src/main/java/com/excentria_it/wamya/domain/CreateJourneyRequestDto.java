package com.excentria_it.wamya.domain;

import java.math.BigDecimal;
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

	private Integer distance;

	private Integer hours;

	private Integer minutes;

	private LocalDateTime dateTime;

	private EngineTypeDto engineType;

	private Integer workers;

	private String description;

	private String status;

	private LocalDateTime creationDateTime;

	@AllArgsConstructor
	@NoArgsConstructor
	@Data
	public static class EngineTypeDto {

		private Long id;

		private String name;

	}

	@AllArgsConstructor
	@NoArgsConstructor
	@Data
	public static class PlaceDto {

		private Long id;

		private PlaceType type;

		private BigDecimal latitude;

		private BigDecimal longitude;

	}

	@AllArgsConstructor
	@NoArgsConstructor
	@Data
	public static class ClientDto {

		private Long id;

		private String firstname;

		private String photoUrl;
	}
}
