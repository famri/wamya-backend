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
public class JourneyRequestInputOutput {

	private Long id;

	private Place departurePlace;

	private Place arrivalPlace;

	private Integer distance;

	private Integer hours;

	private Integer minutes;

	private Instant dateTime;

	private EngineType engineType;

	private Integer workers;

	private String description;

	private String status;

	private Instant creationDateTime;

	@AllArgsConstructor
	@NoArgsConstructor
	@Data
	public static class EngineType {

		private Long id;

		private String name;
	}

	@AllArgsConstructor
	@NoArgsConstructor
	@Data
	public static class Place {

		private Long id;

		private PlaceType type;

		private BigDecimal latitude;

		private BigDecimal longitude;

	}

	@AllArgsConstructor
	@NoArgsConstructor
	@Data
	public static class Client {

		private Long id;

		private String firstname;

		private String photoUrl;
	}
}
