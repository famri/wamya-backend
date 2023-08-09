package com.excentria_it.wamya.application.port.out;

import java.util.List;

import com.excentria_it.wamya.domain.GeoPlaceDto;

public interface LoadFavoriteGeoPlacePort {

	List<GeoPlaceDto> loadFavoriteGeoPlaces(String userName, String locale);

}
