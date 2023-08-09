package com.excentria_it.wamya.application.port.out;

import java.util.Optional;

import com.excentria_it.wamya.domain.GeoCoordinates;
import com.excentria_it.wamya.domain.JourneyTravelInfo;

public interface FetchTravelInfoPort {
	Optional<JourneyTravelInfo> fetchTravelInfo(GeoCoordinates departureGeoCoordinates,
			GeoCoordinates arrivalGeoCoordinates);
}
