package com.excentria_it.wamya.application.service;

import java.util.Optional;

import javax.transaction.Transactional;

import com.excentria_it.wamya.application.port.in.LoadJourneyTravelInfoUseCase;
import com.excentria_it.wamya.application.port.out.CreateTravelInfoPort;
import com.excentria_it.wamya.application.port.out.FetchTravelInfoPort;
import com.excentria_it.wamya.application.port.out.LoadPlaceGeoCoordinatesPort;
import com.excentria_it.wamya.application.port.out.LoadTravelInfoPort;
import com.excentria_it.wamya.common.annotation.UseCase;
import com.excentria_it.wamya.domain.GeoCoordinates;
import com.excentria_it.wamya.domain.JourneyTravelInfo;
import com.excentria_it.wamya.domain.PlaceType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@UseCase
@Transactional
@RequiredArgsConstructor
@Slf4j
public class JourneyTravelInfoService implements LoadJourneyTravelInfoUseCase {

	private final LoadPlaceGeoCoordinatesPort loadPlaceGeoCoordinatesPort;

	private final LoadTravelInfoPort loadTravelInfoPort;

	private final FetchTravelInfoPort fetchTravelInfoPort;

	private final CreateTravelInfoPort createTravelInfoPort;

	@Override
	public Optional<JourneyTravelInfo> loadTravelInfo(Long departurePlaceId, PlaceType departurePlaceType,
			Long arrivalPlaceId, PlaceType arrivalPlaceType) {

		Optional<JourneyTravelInfo> travelInfoOptional1 = loadTravelInfoPort.loadTravelInfo(departurePlaceId,
				departurePlaceType, arrivalPlaceId, arrivalPlaceType);

		if (travelInfoOptional1.isPresent()) {
			return travelInfoOptional1;
		}

		Optional<GeoCoordinates> departureGeoCoordinates = loadPlaceGeoCoordinatesPort
				.loadPlaceGeoCoordinates(departurePlaceId, departurePlaceType);
		if (departureGeoCoordinates.isEmpty()) {
			log.error(String.format("Place does not have geo-coordinates: placeId %d, placeType:%s", departurePlaceId,
					departurePlaceType.name()));
			return Optional.empty();
		}

		Optional<GeoCoordinates> arrivalGeoCoordinates = loadPlaceGeoCoordinatesPort
				.loadPlaceGeoCoordinates(arrivalPlaceId, arrivalPlaceType);
		if (arrivalGeoCoordinates.isEmpty()) {
			log.error(String.format("Place does not have geo-coordinates: placeId %d, placeType:%s", arrivalPlaceId,
					arrivalPlaceType.name()));
			return Optional.empty();
		}

		Optional<JourneyTravelInfo> travelInfoOptional2 = fetchTravelInfoPort
				.fetchTravelInfo(departureGeoCoordinates.get(), arrivalGeoCoordinates.get());

		if (travelInfoOptional2.isPresent()) {
			createTravelInfoPort.createTravelInfo(departurePlaceId, departurePlaceType, arrivalPlaceId,
					arrivalPlaceType, travelInfoOptional2.get());
		}

		return travelInfoOptional2;

	}

}
