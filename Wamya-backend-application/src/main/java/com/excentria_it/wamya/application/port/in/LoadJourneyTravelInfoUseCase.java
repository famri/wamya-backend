package com.excentria_it.wamya.application.port.in;

import java.util.Optional;

import com.excentria_it.wamya.domain.JourneyTravelInfo;
import com.excentria_it.wamya.domain.PlaceType;

public interface LoadJourneyTravelInfoUseCase {

	Optional<JourneyTravelInfo> loadTravelInfo(Long departurePlaceId, PlaceType departurePlaceType, Long arrivalPlaceId,
			PlaceType arrivalPlaceType);

}
