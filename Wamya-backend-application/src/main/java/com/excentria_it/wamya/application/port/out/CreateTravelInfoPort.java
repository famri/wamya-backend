package com.excentria_it.wamya.application.port.out;

import com.excentria_it.wamya.domain.JourneyTravelInfo;
import com.excentria_it.wamya.domain.PlaceType;

public interface CreateTravelInfoPort {

	void createTravelInfo(Long departurePlaceId, PlaceType departureType, Long arrivalPlaceId, PlaceType arrivalType,
			JourneyTravelInfo journeyTravelInfo);

}
