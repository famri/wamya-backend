package com.excentria_it.wamya.domain;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;

import lombok.AllArgsConstructor;
import lombok.Data;

public interface ClientJourneyRequestDto {

	Long getId();

	@Value("#{new  com.excentria_it.wamya.domain.ClientJourneyRequestDto.PlaceDto(target.departurePlaceId, target.departurePlaceRegionId, target.departurePlaceName)}")
	PlaceDto getDeparturePlace();

	@Value("#{new com.excentria_it.wamya.domain.ClientJourneyRequestDto.PlaceDto(target.arrivalPlaceId, target.arrivalPlaceRegionId, target.arrivalPlaceName)}")
	PlaceDto getArrivalPlace();

	@Value("#{new com.excentria_it.wamya.domain.ClientJourneyRequestDto.EngineTypeDto(target.engineTypeId, target.engineTypeName)}")
	EngineTypeDto getEngineType();

	Double getDistance();

	LocalDateTime getDateTime();

	LocalDateTime getEndDateTime();

	LocalDateTime getCreationDateTime();

	Integer getWorkers();

	String getDescription();

	Integer getProposalsCount();

	@AllArgsConstructor
	@Data
	class EngineTypeDto {

		private Long id;

		private String name;

	}

	@AllArgsConstructor
	@Data
	class PlaceDto {

		private String placeId;

		private String placeRegionId;

		private String placeName;
	}

}
