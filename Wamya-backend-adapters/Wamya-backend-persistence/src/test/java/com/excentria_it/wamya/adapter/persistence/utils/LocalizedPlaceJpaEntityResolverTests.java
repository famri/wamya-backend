package com.excentria_it.wamya.adapter.persistence.utils;

import static com.excentria_it.wamya.test.data.common.DelegationJpaTestData.*;
import static com.excentria_it.wamya.test.data.common.DepartmentJpaTestData.*;
import static com.excentria_it.wamya.test.data.common.GeoPlaceJpaTestData.*;
import static com.excentria_it.wamya.test.data.common.LocalityJpaTestData.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.excentria_it.wamya.adapter.persistence.entity.DelegationJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.DepartmentJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.GeoPlaceJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.LocalityJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.LocalizedPlaceJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.PlaceId;
import com.excentria_it.wamya.adapter.persistence.repository.DelegationRepository;
import com.excentria_it.wamya.adapter.persistence.repository.DepartmentRepository;
import com.excentria_it.wamya.adapter.persistence.repository.GeoPlaceRepository;
import com.excentria_it.wamya.adapter.persistence.repository.LocalityRepository;
import com.excentria_it.wamya.domain.PlaceType;

@ExtendWith(MockitoExtension.class)
public class LocalizedPlaceJpaEntityResolverTests {
	@Mock
	private LocalityRepository localityRepository;
	@Mock
	private DelegationRepository delegationRepository;
	@Mock
	private DepartmentRepository departementRepository;
	@Mock
	private GeoPlaceRepository geoPlaceRepository;

	@InjectMocks
	private LocalizedPlaceJpaEntityResolver localizedPlaceJpaEntityResolver;

	@Test
	void testResolveLocalizedPlacesOfDepartmentType() {
		// given
		DepartmentJpaEntity department = defaultExistentDepartmentJpaEntity();
		given(departementRepository.findById(any(Long.class))).willReturn(Optional.of(department));
		// When
		List<LocalizedPlaceJpaEntity> localizedPlaces = localizedPlaceJpaEntityResolver
				.resolveLocalizedPlaces(department.getId(), PlaceType.DEPARTMENT);
		// Then
		List<PlaceId> placeIds = localizedPlaces.stream().map(l -> l.getLocalizedPlaceId().getPlaceId())
				.collect(Collectors.toList());
		placeIds.forEach(p -> {
			assertEquals(department.getId(), p.getId());
			assertEquals(PlaceType.DEPARTMENT, p.getType());
		});

		Map<String, String> localizedPlacesMap = new HashMap<>();
		localizedPlaces.stream().map(l -> localizedPlacesMap.put(l.getLocalizedPlaceId().getLocale(), l.getName()));

		localizedPlacesMap.forEach((k, v) -> {
			assertEquals(department.getLocalizations().get(k), v);
		});
	}

	@Test
	void testResolveEmptyLocalizedPlacesOfDepartmentType() {
		// given
		DepartmentJpaEntity department = defaultExistentDepartmentJpaEntity();
		given(departementRepository.findById(any(Long.class))).willReturn(Optional.empty());
		// When
		List<LocalizedPlaceJpaEntity> localizedPlaces = localizedPlaceJpaEntityResolver
				.resolveLocalizedPlaces(department.getId(), PlaceType.DEPARTMENT);
		// Then
		assertThat(localizedPlaces).isEmpty();
	}

	@Test
	void testResolveLocalizedPlacesOfDelegationType() {
		// given
		DelegationJpaEntity delegation = defaultExistentDelegationJpaEntity();
		given(delegationRepository.findById(any(Long.class))).willReturn(Optional.of(delegation));
		// When
		List<LocalizedPlaceJpaEntity> localizedPlaces = localizedPlaceJpaEntityResolver
				.resolveLocalizedPlaces(delegation.getId(), PlaceType.DELEGATION);
		// Then
		List<PlaceId> placeIds = localizedPlaces.stream().map(l -> l.getLocalizedPlaceId().getPlaceId())
				.collect(Collectors.toList());
		placeIds.forEach(p -> {
			assertEquals(delegation.getId(), p.getId());
			assertEquals(PlaceType.DELEGATION, p.getType());
		});

		Map<String, String> localizedPlacesMap = new HashMap<>();
		localizedPlaces.stream().map(l -> localizedPlacesMap.put(l.getLocalizedPlaceId().getLocale(), l.getName()));

		localizedPlacesMap.forEach((k, v) -> {
			assertEquals(delegation.getName(k) + ", " + delegation.getDepartment().getName(k), v);
		});
	}

	@Test
	void testResolveEmptyLocalizedPlacesOfDelegationType() {
		// given
		DelegationJpaEntity delegation = defaultExistentDelegationJpaEntity();
		given(delegationRepository.findById(any(Long.class))).willReturn(Optional.empty());
		// When
		List<LocalizedPlaceJpaEntity> localizedPlaces = localizedPlaceJpaEntityResolver
				.resolveLocalizedPlaces(delegation.getId(), PlaceType.DELEGATION);
		// Then
		assertThat(localizedPlaces).isEmpty();
	}

	@Test
	void testResolveLocalizedPlacesOfLocalityType() {
		// given
		LocalityJpaEntity locality = defaultExistentLocalityJpaEntity();
		given(localityRepository.findById(any(Long.class))).willReturn(Optional.of(locality));
		// When
		List<LocalizedPlaceJpaEntity> localizedPlaces = localizedPlaceJpaEntityResolver
				.resolveLocalizedPlaces(locality.getId(), PlaceType.LOCALITY);
		// Then
		List<PlaceId> placeIds = localizedPlaces.stream().map(l -> l.getLocalizedPlaceId().getPlaceId())
				.collect(Collectors.toList());
		placeIds.forEach(p -> {
			assertEquals(locality.getId(), p.getId());
			assertEquals(PlaceType.LOCALITY, p.getType());
		});

		Map<String, String> localizedPlacesMap = new HashMap<>();
		localizedPlaces.stream().map(l -> localizedPlacesMap.put(l.getLocalizedPlaceId().getLocale(), l.getName()));

		localizedPlacesMap.forEach((k, v) -> {
			assertEquals(locality.getLocalizations().get(k) + ", " + locality.getDelegation().getName(k) + ", "
					+ locality.getDelegation().getDepartment().getName(k), v);
		});
	}

	@Test
	void testResolveEmptyLocalizedPlacesOfLocalityType() {
		// given
		LocalityJpaEntity locality = defaultExistentLocalityJpaEntity();
		given(localityRepository.findById(any(Long.class))).willReturn(Optional.empty());
		// When
		List<LocalizedPlaceJpaEntity> localizedPlaces = localizedPlaceJpaEntityResolver
				.resolveLocalizedPlaces(locality.getId(), PlaceType.LOCALITY);
		// Then
		assertThat(localizedPlaces).isEmpty();
	}

	@Test
	void testResolveLocalizedPlacesOfGeoPlaceType() {
		// given
		GeoPlaceJpaEntity geoPlace = defaultGeoPlaceJpaEntity();
		given(geoPlaceRepository.findById(any(Long.class))).willReturn(Optional.of(geoPlace));
		// When
		List<LocalizedPlaceJpaEntity> localizedPlaces = localizedPlaceJpaEntityResolver
				.resolveLocalizedPlaces(geoPlace.getId(), PlaceType.GEO_PLACE);
		// Then
		List<PlaceId> placeIds = localizedPlaces.stream().map(l -> l.getLocalizedPlaceId().getPlaceId())
				.collect(Collectors.toList());
		placeIds.forEach(p -> {
			assertEquals(geoPlace.getId(), p.getId());
			assertEquals(PlaceType.GEO_PLACE, p.getType());
		});

		Map<String, String> localizedPlacesMap = new HashMap<>();
		localizedPlaces.stream().map(l -> localizedPlacesMap.put(l.getLocalizedPlaceId().getLocale(), l.getName()));

		localizedPlacesMap.forEach((k, v) -> {
			assertEquals(geoPlace.getName() + ", " + geoPlace.getDepartment().getName(k), v);
		});
	}

	@Test
	void testResolveEmptyLocalizedPlacesOfGeoPlaceType() {
		// given
		GeoPlaceJpaEntity geoPlace = defaultGeoPlaceJpaEntity();
		given(geoPlaceRepository.findById(any(Long.class))).willReturn(Optional.empty());
		// When
		List<LocalizedPlaceJpaEntity> localizedPlaces = localizedPlaceJpaEntityResolver
				.resolveLocalizedPlaces(geoPlace.getId(), PlaceType.GEO_PLACE);
		// Then
		assertThat(localizedPlaces).isEmpty();
	}

}
