package com.excentria_it.wamya.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class JourneyRequestSearchDto {

	private Long id;

	private Place departurePlace;

	private Place arrivalPlace;

	private EngineType engineType;

	private Integer distance;

	private Integer hours;

	private Integer minutes;

	private LocalDateTime dateTime;

	private Integer workers;

	private String description;

	private Client client;

	private Double minPrice;

	@AllArgsConstructor
	@Data
	public static class EngineType {

		private Long id;

		private String name;

		private String code;

	}

	@AllArgsConstructor
	@Data
	public static class Place {

		private Long id;

		private String type;

		private String name;

		private BigDecimal latitude;

		private BigDecimal longitude;

		private Long departmentId;
	}

	@AllArgsConstructor
	@Data
	public static class Client {

		private Long id;

		private String firstname;

		private String photoUrl;
	}

}