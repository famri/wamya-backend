package com.excentria_it.wamya.domain;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ClientJourneyRequestDto {

	private Long id;

	private PlaceDto departurePlace;

	private PlaceDto arrivalPlace;

	private EngineTypeDto engineType;

	private Integer distance;

	private LocalDateTime dateTime;

	private Instant creationDateTime;

	private Integer workers;

	private String description;

	private Integer proposalsCount;

	@AllArgsConstructor
	@Data
	public static class EngineTypeDto {

		private Long id;

		private String name;

		private String code;

	}

	@AllArgsConstructor
	@Data
	public static class PlaceDto {

		private Long id;

		private String type;

		private String name;

		private BigDecimal latitude;

		private BigDecimal longitude;

		private Long departmentId;

	}

}
