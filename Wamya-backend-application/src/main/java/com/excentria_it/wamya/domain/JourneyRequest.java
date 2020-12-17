package com.excentria_it.wamya.domain;

import java.util.Date;

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

	private EngineTypeDto engineType;

	private Integer distance;

	private Date date;

	private Date endDate;

	private Integer workers;

	private String description;

	private ClientDto client;

	private Integer minPrice;

}
