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
public class JourneyRequest {

	private Long id;

	private PlaceDto departurePlace;

	private PlaceDto arrivalPlace;

	private Double distance;

	private LocalDateTime dateTime;

	private LocalDateTime endDateTime;

	private EngineTypeDto engineType;

	private Integer workers;

	private String description;

	private ClientDto client;

}
