package com.excentria_it.wamya.adapter.persistence.adapter;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.excentria_it.wamya.domain.GeoCoordinates;
import com.excentria_it.wamya.domain.PlaceType;

@RunWith(PowerMockRunner.class)
public class PlacePersistenceAdapterJUnit4Tests {

	@InjectMocks
	private PlacePersistenceAdapter placePersistenceAdapter;

	@Test
	@PrepareForTest(PlaceType.class)
	public void testLoadPlaceGeoCoordinatesOfUnknownPlaceType() throws Exception {
		// given

		PlaceType unknownPlaceType = PowerMockito.mock(PlaceType.class);
		PowerMockito.when(unknownPlaceType.ordinal()).thenReturn(4);
		PowerMockito.mockStatic(PlaceType.class);

		PlaceType[] placeTypes = new PlaceType[] { PlaceType.LOCALITY, PlaceType.DELEGATION, PlaceType.DEPARTMENT,
				PlaceType.GEO_PLACE, unknownPlaceType };
		PowerMockito.when(PlaceType.values()).thenAnswer((Answer<PlaceType[]>) invocation -> placeTypes);
		// when
		Optional<GeoCoordinates> geoCoordinates = placePersistenceAdapter.loadPlaceGeoCoordinates(1L, unknownPlaceType);
		// then
		assertThat(geoCoordinates).isEmpty();

	}
}
