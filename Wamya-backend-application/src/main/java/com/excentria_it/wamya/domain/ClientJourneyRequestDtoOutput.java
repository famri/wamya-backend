package com.excentria_it.wamya.domain;

import java.math.BigDecimal;
import java.time.Instant;

import org.springframework.beans.factory.annotation.Value;

import lombok.AllArgsConstructor;
import lombok.Data;

public interface ClientJourneyRequestDtoOutput {

	Long getId();

	@Value("#{new  com.excentria_it.wamya.domain.ClientJourneyRequestDtoOutput.PlaceDto(target.departurePlaceId, target.departurePlaceType.name(), target.departurePlaceName, target.departurePlaceLatitude, target.departurePlaceLongitude, target.departurePlaceDepartmentId)}")
	PlaceDto getDeparturePlace();

	@Value("#{new com.excentria_it.wamya.domain.ClientJourneyRequestDtoOutput.PlaceDto(target.arrivalPlaceId, target.arrivalPlaceType.name(), target.arrivalPlaceName, target.arrivalPlaceLatitude, target.arrivalPlaceLongitude, target.arrivalPlaceDepartmentId)}")
	PlaceDto getArrivalPlace();

	@Value("#{new com.excentria_it.wamya.domain.ClientJourneyRequestDtoOutput.EngineTypeDto(target.engineTypeId, target.engineTypeName, target.engineTypeCode)}")
	EngineTypeDto getEngineType();

	Integer getDistance();

	Instant getDateTime();

	Instant getCreationDateTime();

	Integer getWorkers();

	String getDescription();

	Integer getProposalsCount();

	@AllArgsConstructor
	@Data
	class EngineTypeDto {

		private Long id;

		private String name;
		
		private String code;
		

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

}
