package com.excentria_it.wamya.application.port.out;

import java.util.Optional;

import com.excentria_it.wamya.domain.GeoCoordinates;
import com.excentria_it.wamya.domain.PlaceType;

public interface LoadPlaceGeoCoordinatesPort {
	Optional<GeoCoordinates> loadPlaceGeoCoordinates(Long placeId, PlaceType placeType);
}
