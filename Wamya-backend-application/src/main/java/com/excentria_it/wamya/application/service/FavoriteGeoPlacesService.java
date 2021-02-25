package com.excentria_it.wamya.application.service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import com.excentria_it.wamya.application.port.in.CreateFavoriteGeoPlaceUseCase;
import com.excentria_it.wamya.application.port.in.LoadFavoriteGeoPlacesUseCase;
import com.excentria_it.wamya.application.port.out.CreateFavoriteGeoPlacePort;
import com.excentria_it.wamya.application.port.out.FetchGeoPlaceDepartmentNamePort;
import com.excentria_it.wamya.application.port.out.LoadDepartmentPort;
import com.excentria_it.wamya.application.port.out.LoadFavoriteGeoPlacePort;
import com.excentria_it.wamya.common.annotation.UseCase;
import com.excentria_it.wamya.common.exception.DepartmentNotFoundException;
import com.excentria_it.wamya.domain.DepartmentDto;
import com.excentria_it.wamya.domain.GeoPlaceDto;
import com.excentria_it.wamya.domain.UserFavoriteGeoPlaces;

import lombok.RequiredArgsConstructor;

@UseCase
@Transactional
@RequiredArgsConstructor
public class FavoriteGeoPlacesService implements LoadFavoriteGeoPlacesUseCase, CreateFavoriteGeoPlaceUseCase {

	private final CreateFavoriteGeoPlacePort createFavoriteGeoPlacePort;
	private final LoadFavoriteGeoPlacePort loadFavoriteGeoPlacePort;
	private final FetchGeoPlaceDepartmentNamePort fetchGeoPlaceDepartmentNamePort;
	private final LoadDepartmentPort loadDepartmentPort;

	@Override
	public UserFavoriteGeoPlaces loadFavoriteGeoPlaces(String username, String locale) {

		Set<GeoPlaceDto> geoPlaces = loadFavoriteGeoPlacePort.loadFavoriteGeoPlaces(username, locale);
		if (geoPlaces.isEmpty()) {
			return new UserFavoriteGeoPlaces(0, Collections.emptySet());
		} else {
			return new UserFavoriteGeoPlaces(geoPlaces.size(), geoPlaces);
		}

	}

	@Override
	public GeoPlaceDto createFavoriteGeoPlace(String geoPlaceName, BigDecimal geoPlaceLatitude,
			BigDecimal geoPlaceLongitude, String username, String locale) {

		Optional<String> departmentName = fetchGeoPlaceDepartmentNamePort.fetchDepartmentName(geoPlaceLatitude,
				geoPlaceLongitude, locale);
		if (departmentName.isEmpty()) {
			throw new DepartmentNotFoundException(
					String.format("Department name not found by latitude: %.4f and longitude: %.4f", geoPlaceLatitude,
							geoPlaceLongitude));
		} else {
			Optional<DepartmentDto> department = loadDepartmentPort.loadDepartmentByName(departmentName.get(), locale);
			if (department.isEmpty()) {

				throw new DepartmentNotFoundException(String
						.format("Unable to find department by name: %s and locale %s", departmentName.get(), locale));
			}
			return createFavoriteGeoPlacePort.createFavoriteGeoPlace(geoPlaceName, geoPlaceLatitude, geoPlaceLongitude,
					department.get().getId(), username, locale);
		}

	}
}
