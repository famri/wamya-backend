package com.excentria_it.wamya.adapter.persistence.utils;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.excentria_it.wamya.adapter.persistence.entity.LocalizedPlaceJpaEntity;
import com.excentria_it.wamya.domain.PlaceType;

@RunWith(PowerMockRunner.class)
public class LocalizedPlaceJpaEntityResolverJUnit4Tests {
	@InjectMocks
	private LocalizedPlaceJpaEntityResolver localizedPlaceJpaEntityResolver;

	@Test
	@PrepareForTest(PlaceType.class)
	public void testResolveLocalizedPlacesOfUnknownPlaceType() throws Exception {

		PlaceType unknownPlaceType = PowerMockito.mock(PlaceType.class);
		PowerMockito.when(unknownPlaceType.ordinal()).thenReturn(4);
		PowerMockito.mockStatic(PlaceType.class);

		PlaceType[] placeTypes = new PlaceType[] { PlaceType.LOCALITY, PlaceType.DELEGATION, PlaceType.DEPARTMENT,
				PlaceType.GEO_PLACE, unknownPlaceType };
		PowerMockito.when(PlaceType.values()).thenAnswer((Answer<PlaceType[]>) invocation -> placeTypes);
		// when
		List<LocalizedPlaceJpaEntity> localizedPlaces = localizedPlaceJpaEntityResolver.resolveLocalizedPlaces(1L,
				unknownPlaceType);
		// then
		assertThat(localizedPlaces).isEmpty();
	}

}
