package com.excentria_it.wamya.application.port.out;

import java.util.Map;
import java.util.Set;

import com.excentria_it.wamya.domain.PlaceType;

public interface LoadPlaceNamesPort {

	Map<String, String> loadPlaceNames(Long placeId, PlaceType placeType, Set<String> locales);

}
