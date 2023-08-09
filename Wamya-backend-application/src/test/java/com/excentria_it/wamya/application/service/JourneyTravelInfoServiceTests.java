package com.excentria_it.wamya.application.service;

import static com.excentria_it.wamya.test.data.common.JourneyTravelInfoTestData.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.excentria_it.wamya.application.port.out.CreateTravelInfoPort;
import com.excentria_it.wamya.application.port.out.FetchTravelInfoPort;
import com.excentria_it.wamya.application.port.out.LoadPlaceGeoCoordinatesPort;
import com.excentria_it.wamya.application.port.out.LoadTravelInfoPort;
import com.excentria_it.wamya.domain.GeoCoordinates;
import com.excentria_it.wamya.domain.JourneyTravelInfo;
import com.excentria_it.wamya.domain.PlaceType;

@ExtendWith(MockitoExtension.class)
public class JourneyTravelInfoServiceTests {

	@Mock
	private LoadPlaceGeoCoordinatesPort loadPlaceGeoCoordinatesPort;

	@Mock
	private LoadTravelInfoPort loadTravelInfoPort;

	@Mock
	private FetchTravelInfoPort fetchTravelInfoPort;

	@Mock
	private CreateTravelInfoPort createTravelInfoPort;

	@InjectMocks
	private JourneyTravelInfoService journeyTravelInfoService;

	@Test
	void givenExistentJourneyTravelInfoInDb_WhenLoadTravelInfo_ThenReturnJourneyTravelInfo() {
		// given
		Optional<JourneyTravelInfo> jti1 = Optional.of(defaultJourneyTravelInfo());
		given(loadTravelInfoPort.loadTravelInfo(any(Long.class), any(PlaceType.class), any(Long.class),
				any(PlaceType.class))).willReturn(jti1);

		// when
		Optional<JourneyTravelInfo> jti2 = journeyTravelInfoService.loadTravelInfo(1L, PlaceType.LOCALITY, 2L,
				PlaceType.LOCALITY);
		// then
		assertEquals(jti1, jti2);
	}

	@Test
	void givenInexistentJourneyTravelInfoInDbAndNullDeparturePlaceGeoCoordinates_WhenLoadTravelInfo_ThenReturnNull() {
		// given

		given(loadTravelInfoPort.loadTravelInfo(any(Long.class), any(PlaceType.class), any(Long.class),
				any(PlaceType.class))).willReturn(Optional.empty());
		given(loadPlaceGeoCoordinatesPort.loadPlaceGeoCoordinates(eq(1L), eq(PlaceType.LOCALITY)))
				.willReturn(Optional.empty());
		// when
		Optional<JourneyTravelInfo> jti = journeyTravelInfoService.loadTravelInfo(1L, PlaceType.LOCALITY, 2L,
				PlaceType.LOCALITY);
		// then
		assertThat(jti).isEmpty();
	}

	@Test
	void givenInexistentJourneyTravelInfoInDbAndNullArrivalPlaceGeoCoordinates_WhenLoadTravelInfo_ThenReturnNull() {
		// given

		given(loadTravelInfoPort.loadTravelInfo(any(Long.class), any(PlaceType.class), any(Long.class),
				any(PlaceType.class))).willReturn(Optional.empty());
		Optional<GeoCoordinates> geoCoordinates = Optional
				.of(new GeoCoordinates(new BigDecimal(36.80157399848794), new BigDecimal(10.178896922512495)));
		given(loadPlaceGeoCoordinatesPort.loadPlaceGeoCoordinates(eq(1L), eq(PlaceType.LOCALITY)))
				.willReturn(geoCoordinates);
		given(loadPlaceGeoCoordinatesPort.loadPlaceGeoCoordinates(eq(2L), eq(PlaceType.LOCALITY)))
				.willReturn(Optional.empty());
		// when
		Optional<JourneyTravelInfo> jti = journeyTravelInfoService.loadTravelInfo(1L, PlaceType.LOCALITY, 2L,
				PlaceType.LOCALITY);
		// then
		assertThat(jti).isEmpty();
	}

	@Test
	void givenInexistentJourneyTravelInfoInDbAndExistentJourneyTravelInfoFromFetch_WhenLoadTravelInfo_ThenReturnJourneyTravelInfoFromFetch() {
		// given

		given(loadTravelInfoPort.loadTravelInfo(any(Long.class), any(PlaceType.class), any(Long.class),
				any(PlaceType.class))).willReturn(Optional.empty());
		Optional<GeoCoordinates> geoCoordinates1 = Optional
				.of(new GeoCoordinates(new BigDecimal(36.80157399848794), new BigDecimal(10.178896922512495)));
		Optional<GeoCoordinates> geoCoordinates2 = Optional
				.of(new GeoCoordinates(new BigDecimal(37.80157399848794), new BigDecimal(11.178896922512495)));
		given(loadPlaceGeoCoordinatesPort.loadPlaceGeoCoordinates(eq(1L), eq(PlaceType.LOCALITY)))
				.willReturn(geoCoordinates1);
		given(loadPlaceGeoCoordinatesPort.loadPlaceGeoCoordinates(eq(2L), eq(PlaceType.LOCALITY)))
				.willReturn(geoCoordinates2);
		Optional<JourneyTravelInfo> jti1 = Optional.of(defaultJourneyTravelInfo());
		given(fetchTravelInfoPort.fetchTravelInfo(any(GeoCoordinates.class), any(GeoCoordinates.class)))
				.willReturn(jti1);
		// when
		Optional<JourneyTravelInfo> jti2 = journeyTravelInfoService.loadTravelInfo(1L, PlaceType.LOCALITY, 2L,
				PlaceType.LOCALITY);
		// then
		assertEquals(jti1, jti2);
		then(createTravelInfoPort).should(times(1)).createTravelInfo(1L, PlaceType.LOCALITY, 2L, PlaceType.LOCALITY,
				jti1.get());

	}

	@Test
	void givenInexistentJourneyTravelInfoInDbAndInexistentJourneyTravelInfoFromFetch_WhenLoadTravelInfo_ThenReturnNull() {
		// given

		given(loadTravelInfoPort.loadTravelInfo(any(Long.class), any(PlaceType.class), any(Long.class),
				any(PlaceType.class))).willReturn(Optional.empty());
		Optional<GeoCoordinates> geoCoordinates1 = Optional
				.of(new GeoCoordinates(new BigDecimal(36.80157399848794), new BigDecimal(10.178896922512495)));
		Optional<GeoCoordinates> geoCoordinates2 = Optional
				.of(new GeoCoordinates(new BigDecimal(37.80157399848794), new BigDecimal(11.178896922512495)));
		given(loadPlaceGeoCoordinatesPort.loadPlaceGeoCoordinates(eq(1L), eq(PlaceType.LOCALITY)))
				.willReturn(geoCoordinates1);
		given(loadPlaceGeoCoordinatesPort.loadPlaceGeoCoordinates(eq(2L), eq(PlaceType.LOCALITY)))
				.willReturn(geoCoordinates2);

		given(fetchTravelInfoPort.fetchTravelInfo(any(GeoCoordinates.class), any(GeoCoordinates.class)))
				.willReturn(Optional.empty());
		// when
		Optional<JourneyTravelInfo> jti = journeyTravelInfoService.loadTravelInfo(1L, PlaceType.LOCALITY, 2L,
				PlaceType.LOCALITY);
		// then
		assertThat(jti).isEmpty();
	}
}
