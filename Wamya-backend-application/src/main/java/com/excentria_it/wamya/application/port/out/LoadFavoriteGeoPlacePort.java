package com.excentria_it.wamya.application.port.out;

import java.util.Set;

import com.excentria_it.wamya.domain.GeoPlaceDto;

public interface LoadFavoriteGeoPlacePort {

	Set<GeoPlaceDto> loadFavoriteGeoPlaces(String userName, String locale);

}
