package com.excentria_it.wamya.domain;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;

public interface JourneyRequestSearchDto {

	Long getId();

	@Value("#{T(com.excentria_it.wamya.application.utils.MapperUtility).buildPlaceDto(target.departurePlaceId, target.departurePlaceRegionId, target.departurePlaceName)}")
	PlaceDto getDeparturePlace();

	@Value("#{T(com.excentria_it.wamya.application.utils.MapperUtility).buildPlaceDto(target.arrivalPlaceId, target.arrivalPlaceRegionId, target.arrivalPlaceName)}")
	PlaceDto getArrivalPlace();

	@Value("#{T(com.excentria_it.wamya.application.utils.MapperUtility).buildEngineTypeDto(target.engineTypeId, target.engineTypeName)}")
	EngineTypeDto getEngineType();

	Double getDistance();

	LocalDateTime getDateTime();

	LocalDateTime getEndDateTime();

	Integer getWorkers();

	String getDescription();

	@Value("#{T(com.excentria_it.wamya.application.utils.MapperUtility).buildClientDto(target.clientId, target.clientFirstname, target.clientPhotoUrl)}")
	ClientDto getClient();

	Integer getMinPrice();

}