package com.excentria_it.wamya.test.data.common;

import java.math.BigDecimal;
import java.util.Set;

import com.excentria_it.wamya.application.port.in.CreateFavoriteGeoPlaceUseCase.CreateFavoriteGeoPlaceCommand;
import com.excentria_it.wamya.application.port.in.CreateFavoriteGeoPlaceUseCase.CreateFavoriteGeoPlaceCommand.CreateFavoriteGeoPlaceCommandBuilder;
import com.excentria_it.wamya.domain.DepartmentDto;
import com.excentria_it.wamya.domain.GeoPlaceDto;
import com.excentria_it.wamya.domain.UserFavoriteGeoPlaces;

public class GeoPlaceTestData {
	private static final GeoPlaceDto g1 = new GeoPlaceDto(1L, "GeoPlace1", new BigDecimal(36.8888),
			new BigDecimal(10.7777), new DepartmentDto(1L, "Department1"));

	private static final GeoPlaceDto g2 = new GeoPlaceDto(1L, "GeoPlace2", new BigDecimal(36.1111),
			new BigDecimal(10.1111), new DepartmentDto(2L, "Department2"));

	public static Set<GeoPlaceDto> defaultGeoPlaceDtoSet() {

		return Set.of(g1, g2);
	}

	public static GeoPlaceDto defaultGeoPlaceDto() {
		return g1;
	}

	public static CreateFavoriteGeoPlaceCommandBuilder defaultCreateFavoriteGeoPlaceCommandBuilder() {
		return CreateFavoriteGeoPlaceCommand.builder().name("My Favorite Place")
				.latitude(new BigDecimal(34.73221703872734)).longitude(new BigDecimal(10.763960197911267));

	}

	public static UserFavoriteGeoPlaces defaultUserFavoriteGeoPlaces() {
		return new UserFavoriteGeoPlaces(2, Set.of(g1, g2));
	}
}
