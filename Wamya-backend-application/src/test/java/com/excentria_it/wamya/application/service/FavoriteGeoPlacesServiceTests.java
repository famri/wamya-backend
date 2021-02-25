package com.excentria_it.wamya.application.service;

import static com.excentria_it.wamya.test.data.common.GeoPlaceTestData.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.excentria_it.wamya.application.port.out.CreateFavoriteGeoPlacePort;
import com.excentria_it.wamya.application.port.out.FetchGeoPlaceDepartmentNamePort;
import com.excentria_it.wamya.application.port.out.LoadDepartmentPort;
import com.excentria_it.wamya.application.port.out.LoadFavoriteGeoPlacePort;
import com.excentria_it.wamya.common.exception.DepartmentNotFoundException;
import com.excentria_it.wamya.domain.DepartmentDto;
import com.excentria_it.wamya.domain.GeoPlaceDto;
import com.excentria_it.wamya.domain.UserFavoriteGeoPlaces;
import com.excentria_it.wamya.test.data.common.TestConstants;

@ExtendWith(MockitoExtension.class)
public class FavoriteGeoPlacesServiceTests {
	@Mock
	private CreateFavoriteGeoPlacePort createFavoriteGeoPlacePort;
	@Mock
	private LoadFavoriteGeoPlacePort loadFavoriteGeoPlacePort;
	@Mock
	private FetchGeoPlaceDepartmentNamePort fetchGeoPlaceDepartmentNamePort;
	@Mock
	private LoadDepartmentPort loadDepartmentPort;

	@InjectMocks
	private FavoriteGeoPlacesService favoriteGeoPlacesService;

	@Test
	void givenEmptyUserFavoritePlaces_WhenLoadFavoriteGeoPlace_ThenReturnUserFavoriteGeoPlacesWithEmptyContent() {
		// given
		given(loadFavoriteGeoPlacePort.loadFavoriteGeoPlaces(any(String.class), any(String.class)))
				.willReturn(Collections.emptySet());
		// when

		UserFavoriteGeoPlaces userFavoriteGeoPlaces = favoriteGeoPlacesService
				.loadFavoriteGeoPlaces(TestConstants.DEFAULT_EMAIL, "fr_FR");
		// then
		assertEquals(0, userFavoriteGeoPlaces.getTotalElements());
		assertThat(userFavoriteGeoPlaces.getContent()).isEmpty();
	}

	@Test
	void givenUserFavoritePlaces_WhenLoadFavoriteGeoPlace_ThenReturnUserFavoriteGeoPlacesWithEmptyContent() {
		// given
		Set<GeoPlaceDto> geoPlaces = defaultGeoPlaceDtoSet();
		given(loadFavoriteGeoPlacePort.loadFavoriteGeoPlaces(any(String.class), any(String.class)))
				.willReturn(geoPlaces);
		// when

		UserFavoriteGeoPlaces userFavoriteGeoPlaces = favoriteGeoPlacesService
				.loadFavoriteGeoPlaces(TestConstants.DEFAULT_EMAIL, "fr_FR");
		// then
		assertEquals(geoPlaces.size(), userFavoriteGeoPlaces.getTotalElements());
		assertEquals(geoPlaces, userFavoriteGeoPlaces.getContent());
	}

	@Test
	void givenEmptyDepartmentNameFromFetch_WhenCreateFavoriteGeoPlace_ThenThrowDepartmentNotFoundException() {
		// given
		given(fetchGeoPlaceDepartmentNamePort.fetchDepartmentName(any(BigDecimal.class), any(BigDecimal.class),
				any(String.class))).willReturn(Optional.empty());
		// when //then
		assertThrows(DepartmentNotFoundException.class,
				() -> favoriteGeoPlacesService.createFavoriteGeoPlace("GeoPlace", new BigDecimal(36.7777),
						new BigDecimal(10.7777), TestConstants.DEFAULT_EMAIL, "fr_FR"));
	}

	@Test
	void givenEmptyDepartmentDtoFromDb_WhenCreateFavoriteGeoPlace_ThenThrowDepartmentNotFoundException() {
		// given
		given(fetchGeoPlaceDepartmentNamePort.fetchDepartmentName(any(BigDecimal.class), any(BigDecimal.class),
				any(String.class))).willReturn(Optional.of("Department"));

		given(loadDepartmentPort.loadDepartmentByName(any(String.class), any(String.class)))
				.willReturn(Optional.empty());
		// when //then
		assertThrows(DepartmentNotFoundException.class,
				() -> favoriteGeoPlacesService.createFavoriteGeoPlace("GeoPlace", new BigDecimal(36.7777),
						new BigDecimal(10.7777), TestConstants.DEFAULT_EMAIL, "fr_FR"));

	}

	@Test
	void givenExistentDepartmentName_WhenCreateFavoriteGeoPlace_ThenThrowDepartmentNotFoundException() {
		// given
		given(fetchGeoPlaceDepartmentNamePort.fetchDepartmentName(any(BigDecimal.class), any(BigDecimal.class),
				any(String.class))).willReturn(Optional.of("Department"));

		given(loadDepartmentPort.loadDepartmentByName(any(String.class), any(String.class)))
				.willReturn(Optional.of(new DepartmentDto(1L, "Department")));

		GeoPlaceDto geoPlaceDto = defaultGeoPlaceDto();
		given(createFavoriteGeoPlacePort.createFavoriteGeoPlace(any(String.class), any(BigDecimal.class),
				any(BigDecimal.class), any(Long.class), any(String.class), any(String.class))).willReturn(geoPlaceDto);
		// when
		BigDecimal latitude = new BigDecimal(36.7777);
		BigDecimal longitude = new BigDecimal(10.7777);
		GeoPlaceDto result = favoriteGeoPlacesService.createFavoriteGeoPlace("GeoPlace", latitude, longitude,
				TestConstants.DEFAULT_EMAIL, "fr_FR");
		// then
		then(createFavoriteGeoPlacePort).should(times(1)).createFavoriteGeoPlace(eq("GeoPlace"), eq(latitude),
				eq(longitude), eq(1L), eq(TestConstants.DEFAULT_EMAIL), eq("fr_FR"));
		assertEquals(geoPlaceDto.getId(), result.getId());
		assertEquals(geoPlaceDto.getName(), result.getName());
		assertEquals(geoPlaceDto.getLatitude(), result.getLatitude());
		assertEquals(geoPlaceDto.getLongitude(), result.getLongitude());
		assertEquals(geoPlaceDto.getDepartment(), result.getDepartment());
	}
}
