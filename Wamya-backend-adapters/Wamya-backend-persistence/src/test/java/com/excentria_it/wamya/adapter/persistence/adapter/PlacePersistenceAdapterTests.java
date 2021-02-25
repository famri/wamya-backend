package com.excentria_it.wamya.adapter.persistence.adapter;

import static com.excentria_it.wamya.test.data.common.DelegationJpaTestData.*;
import static com.excentria_it.wamya.test.data.common.DepartmentJpaTestData.*;
import static com.excentria_it.wamya.test.data.common.GeoPlaceJpaTestData.*;
import static com.excentria_it.wamya.test.data.common.LocalityJpaTestData.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.excentria_it.wamya.adapter.persistence.entity.DelegationJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.DepartmentJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.GeoPlaceJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.LocalityJpaEntity;
import com.excentria_it.wamya.adapter.persistence.repository.DelegationRepository;
import com.excentria_it.wamya.adapter.persistence.repository.DepartmentRepository;
import com.excentria_it.wamya.adapter.persistence.repository.GeoPlaceRepository;
import com.excentria_it.wamya.adapter.persistence.repository.LocalityRepository;
import com.excentria_it.wamya.domain.GeoCoordinates;
import com.excentria_it.wamya.domain.PlaceType;

@ExtendWith(MockitoExtension.class)
public class PlacePersistenceAdapterTests {
	@Mock
	private LocalityRepository localityRepository;
	@Mock
	private DelegationRepository delegationRepository;
	@Mock
	private DepartmentRepository departmentRepository;
	@Mock
	private GeoPlaceRepository geoPlaceRepository;

	@InjectMocks
	private PlacePersistenceAdapter placePersistenceAdapter;

	@Test
	void testLoadPlaceGeoCoordinatesOfLocalityPlaceType() {
		// given
		LocalityJpaEntity locality = defaultExistentLocalityJpaEntity();
		given(localityRepository.findById(any(Long.class))).willReturn(Optional.of(locality));

		// when
		Optional<GeoCoordinates> geoCoordinates = placePersistenceAdapter.loadPlaceGeoCoordinates(1L,
				PlaceType.LOCALITY);
		// then
		assertEquals(locality.getLatitude(), geoCoordinates.get().getLatitude());
		assertEquals(locality.getLongitude(), geoCoordinates.get().getLongitude());
	}

	@Test
	void testLoadEmptyPlaceGeoCoordinatesOfLocalityPlaceType() {
		// given
		given(localityRepository.findById(any(Long.class))).willReturn(Optional.empty());

		// when
		Optional<GeoCoordinates> geoCoordinates = placePersistenceAdapter.loadPlaceGeoCoordinates(1L,
				PlaceType.LOCALITY);
		// then
		assertThat(geoCoordinates).isEmpty();
	}

	@Test
	void testLoadPlaceGeoCoordinatesOfDelegationPlaceType() {
		// given
		DelegationJpaEntity delegation = defaultExistentDelegationJpaEntity();
		given(delegationRepository.findById(any(Long.class))).willReturn(Optional.of(delegation));

		// when
		Optional<GeoCoordinates> geoCoordinates = placePersistenceAdapter.loadPlaceGeoCoordinates(1L,
				PlaceType.DELEGATION);
		// then
		assertEquals(delegation.getLatitude(), geoCoordinates.get().getLatitude());
		assertEquals(delegation.getLongitude(), geoCoordinates.get().getLongitude());
	}

	@Test
	void testLoadEmptyPlaceGeoCoordinatesOfDelegationPlaceType() {
		// given
		given(delegationRepository.findById(any(Long.class))).willReturn(Optional.empty());

		// when
		Optional<GeoCoordinates> geoCoordinates = placePersistenceAdapter.loadPlaceGeoCoordinates(1L,
				PlaceType.DELEGATION);
		// then
		assertThat(geoCoordinates).isEmpty();
	}

	@Test
	void testLoadPlaceGeoCoordinatesOfDepartmentPlaceType() {
		// given
		DepartmentJpaEntity department = defaultExistentDepartmentJpaEntity();
		given(departmentRepository.findById(any(Long.class))).willReturn(Optional.of(department));

		// when
		Optional<GeoCoordinates> geoCoordinates = placePersistenceAdapter.loadPlaceGeoCoordinates(1L,
				PlaceType.DEPARTMENT);
		// then
		assertEquals(department.getLatitude(), geoCoordinates.get().getLatitude());
		assertEquals(department.getLongitude(), geoCoordinates.get().getLongitude());
	}

	@Test
	void testLoadEmptyPlaceGeoCoordinatesOfDepartmentPlaceType() {
		// given
		given(departmentRepository.findById(any(Long.class))).willReturn(Optional.empty());

		// when
		Optional<GeoCoordinates> geoCoordinates = placePersistenceAdapter.loadPlaceGeoCoordinates(1L,
				PlaceType.DEPARTMENT);
		// then
		assertThat(geoCoordinates).isEmpty();

	}

	@Test
	void testLoadPlaceGeoCoordinatesOfGeoPlaceType() {
		// given
		GeoPlaceJpaEntity geoPlace = defaultGeoPlaceJpaEntity();

		given(geoPlaceRepository.findById(any(Long.class))).willReturn(Optional.of(geoPlace));

		// when
		Optional<GeoCoordinates> geoCoordinates = placePersistenceAdapter.loadPlaceGeoCoordinates(1L,
				PlaceType.GEO_PLACE);
		// then
		assertEquals(geoPlace.getLatitude(), geoCoordinates.get().getLatitude());
		assertEquals(geoPlace.getLongitude(), geoCoordinates.get().getLongitude());
	}

	@Test
	void testLoadEmptyPlaceGeoCoordinatesOfGeoCoordinatesPlaceType() {
		// given

		given(geoPlaceRepository.findById(any(Long.class))).willReturn(Optional.empty());

		// when
		Optional<GeoCoordinates> geoCoordinates = placePersistenceAdapter.loadPlaceGeoCoordinates(1L,
				PlaceType.GEO_PLACE);
		// then
		assertThat(geoCoordinates).isEmpty();
	}

}
