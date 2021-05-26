package com.excentria_it.wamya.domain;

import java.math.BigDecimal;
import java.time.Instant;

import org.springframework.beans.factory.annotation.Value;

import lombok.AllArgsConstructor;
import lombok.Data;

public interface JourneyRequestSearchOutput {

	Long getId();

	@Value("#{new  com.excentria_it.wamya.domain.JourneyRequestSearchOutput.PlaceDto(target.departurePlaceId, target.departurePlaceType, target.departurePlaceName, target.departurePlaceLatitude, target.departurePlaceLongitude, target.departurePlaceDepartmentId)}")
	PlaceDto getDeparturePlace();

	@Value("#{new com.excentria_it.wamya.domain.JourneyRequestSearchOutput.PlaceDto(target.arrivalPlaceId, target.arrivalPlaceType,  target.arrivalPlaceName, target.arrivalPlaceLatitude, target.arrivalPlaceLongitude, target.arrivalPlaceDepartmentId)}")
	PlaceDto getArrivalPlace();

	@Value("#{new com.excentria_it.wamya.domain.JourneyRequestSearchOutput.EngineTypeDto(target.engineTypeId, target.engineTypeName)}")
	EngineTypeDto getEngineType();

	Integer getDistance();

	Integer getHours();

	Integer getMinutes();

	Instant getDateTime();

	Integer getWorkers();

	String getDescription();

	@Value("#{new com.excentria_it.wamya.domain.JourneyRequestSearchOutput.ClientDto(target.clientOauthId, target.clientFirstname, T(com.excentria_it.wamya.application.utils.DocumentUrlResolver).resolveUrl(target.clientProfileImageId, target.clientProfileImageHash))}")
	ClientDto getClient();

	Double getMinPrice();

	@AllArgsConstructor
	@Data
	class EngineTypeDto {

		private Long id;

		private String name;

	}

	@AllArgsConstructor
	@Data
	class PlaceDto {

		private Long id;

		private String type;

		private String name;

		private BigDecimal latitude;

		private BigDecimal longitude;

		private Long departmentId;
	}

	@AllArgsConstructor
	@Data
	class ClientDto {

		private Long id;

		private String firstname;

		private String photoUrl;
	}

}