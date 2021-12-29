package com.excentria_it.wamya.domain;

import java.math.BigDecimal;
import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class JourneyRequestOutput {

	private Long id;

	private PlaceDto departurePlace;

	private PlaceDto arrivalPlace;

	private EngineTypeDto engineType;

	private Integer distance;

	private Integer hours;

	private Integer minutes;

	private Instant dateTime;

	private Integer workers;

	private String description;

	private ClientDto client;

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

	@AllArgsConstructor
	@Data
	public static class ClientDto {

		private Long id;

		private String firstname;

		private Long imageId;

		private String imageHash;
	}

}
