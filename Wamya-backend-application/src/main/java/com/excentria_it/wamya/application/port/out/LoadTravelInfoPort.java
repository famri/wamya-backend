package com.excentria_it.wamya.application.port.out;

import java.util.Optional;

import com.excentria_it.wamya.domain.JourneyTravelInfo;
import com.excentria_it.wamya.domain.PlaceType;

public interface LoadTravelInfoPort {

	Optional<JourneyTravelInfo> loadTravelInfo(Long departurePlaceId, PlaceType departureType, Long arrivalPlaceId,
			PlaceType arrivalType);

}
