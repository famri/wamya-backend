package com.excentria_it.wamya.domain;

import java.time.ZonedDateTime;

import org.springframework.beans.factory.annotation.Value;

import lombok.AllArgsConstructor;
import lombok.Data;

public interface JourneyRequestSearchDto {

	Long getId();

	@Value("#{new  com.excentria_it.wamya.domain.JourneyRequestSearchDto.PlaceDto(target.departurePlaceId, target.departurePlaceRegionId, target.departurePlaceName)}")
	PlaceDto getDeparturePlace();

	@Value("#{new com.excentria_it.wamya.domain.JourneyRequestSearchDto.PlaceDto(target.arrivalPlaceId, target.arrivalPlaceRegionId, target.arrivalPlaceName)}")
	PlaceDto getArrivalPlace();

	@Value("#{new com.excentria_it.wamya.domain.JourneyRequestSearchDto.EngineTypeDto(target.engineTypeId, target.engineTypeName)}")
	EngineTypeDto getEngineType();

	Double getDistance();

	ZonedDateTime getDateTime();

	ZonedDateTime getEndDateTime();

	Integer getWorkers();

	String getDescription();

	@Value("#{new com.excentria_it.wamya.domain.JourneyRequestSearchDto.ClientDto(target.clientId, target.clientFirstname, target.clientPhotoUrl)}")
	ClientDto getClient();

	Integer getMinPrice();

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

	@AllArgsConstructor
	@Data
	class ClientDto {

		private Long id;

		private String firstname;

		private String photoUrl;
	}

}