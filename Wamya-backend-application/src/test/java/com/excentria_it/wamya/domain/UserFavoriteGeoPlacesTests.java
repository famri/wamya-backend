package com.excentria_it.wamya.domain;

import static com.excentria_it.wamya.test.data.common.GeoPlaceTestData.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

public class UserFavoriteGeoPlacesTests {
	@Test
	void tesBuilder() {
		List<GeoPlaceDto> geoPlaces = defaultGeoPlaceDtoList();
		UserFavoriteGeoPlaces userGeoPlaces = new UserFavoriteGeoPlaces.Builder().withTotalElements(1)
				.withContent(geoPlaces).build();
		assertEquals(1, userGeoPlaces.getTotalElements());
		assertEquals(geoPlaces, userGeoPlaces.getContent());
	}
}
