package com.excentria_it.wamya.application.port.out;

import java.math.BigDecimal;

import com.excentria_it.wamya.domain.GeoPlaceDto;

public interface CreateFavoriteGeoPlacePort {

	GeoPlaceDto createFavoriteGeoPlace(String name, BigDecimal latitude, BigDecimal longitude, Long departmentId,
			String username, String locale);

}
