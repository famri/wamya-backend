package com.excentria_it.wamya.application.port.in;

import com.excentria_it.wamya.domain.UserFavoriteGeoPlaces;

public interface LoadFavoriteGeoPlacesUseCase {

	UserFavoriteGeoPlaces loadFavoriteGeoPlaces(String userName, String locale);

}
