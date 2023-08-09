package com.excentria_it.wamya.adapter.persistence.utils;

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
import com.excentria_it.wamya.domain.PlaceType;

@ExtendWith(MockitoExtension.class)
public class DepartmentJpaEntityResolverTests {
	@Mock
	private LocalityRepository localityRepository;
	@Mock
	private DelegationRepository delegationRepository;
	@Mock
	private DepartmentRepository departementRepository;
	@Mock  
	private  GeoPlaceRepository geoPlaceRepository;

	@InjectMocks
	private DepartmentJpaEntityResolver departmentJpaEntityResolver;

	@Test
	void testResolveDepartmentOfDepartmentPlaceType() {
		DepartmentJpaEntity department = defaultExistentDepartmentJpaEntity();
		// given
		given(departementRepository.findById(any(Long.class))).willReturn(Optional.of(department));
		// when
		Optional<DepartmentJpaEntity> optionalDepartment = departmentJpaEntityResolver.resolveDepartment(1L,
				PlaceType.DEPARTMENT);
		// then
		assertEquals(department, optionalDepartment.get());
	}

	@Test
	void testResolveDepartmentOfDelegationPlaceType() {
		DelegationJpaEntity delegation = defaultExistentDelegationJpaEntity();
		// given
		given(delegationRepository.findById(any(Long.class))).willReturn(Optional.of(delegation));
		// when
		Optional<DepartmentJpaEntity> optionalDepartment = departmentJpaEntityResolver.resolveDepartment(1L,
				PlaceType.DELEGATION);
		// then
		assertEquals(delegation.getDepartment(), optionalDepartment.get());
	}

	@Test
	void testResolveDepartmentOfLocalityPlaceType() {
		LocalityJpaEntity locality = defaultExistentLocalityJpaEntity();
		// given
		given(localityRepository.findById(any(Long.class))).willReturn(Optional.of(locality));
		// when
		Optional<DepartmentJpaEntity> optionalDepartment = departmentJpaEntityResolver.resolveDepartment(1L,
				PlaceType.LOCALITY);
		// then
		assertEquals(locality.getDelegation().getDepartment(), optionalDepartment.get());
	}

	// @Test
	void testResolveDepartmentOfGeoCoordinatesPlaceType() {

		// given

		// when
		Optional<DepartmentJpaEntity> optionalDepartment = departmentJpaEntityResolver.resolveDepartment(1L,
				PlaceType.GEO_PLACE);
		// then
		assertThat(optionalDepartment).isEmpty();
	}

	@Test
	void testResolveEmptyDepartmentOfDelegationPlaceType() {

		// given
		given(delegationRepository.findById(any(Long.class))).willReturn(Optional.empty());
		// when
		Optional<DepartmentJpaEntity> optionalDepartment = departmentJpaEntityResolver.resolveDepartment(1L,
				PlaceType.DELEGATION);
		// then
		assertThat(optionalDepartment).isEmpty();
	}

	@Test
	void testResolveDepartmentOfNullLocalityPlaceTypeHavingNullDelegation() {

		// given
		LocalityJpaEntity locality = defaultExistentLocalityJpaEntity();
		locality.setDelegation(null);
		given(localityRepository.findById(any(Long.class))).willReturn(Optional.of(locality));
		// when
		Optional<DepartmentJpaEntity> optionalDepartment = departmentJpaEntityResolver.resolveDepartment(1L,
				PlaceType.LOCALITY);
		// then
		assertThat(optionalDepartment).isEmpty();
	}

	@Test
	void testResolveEmptyDepartmentOfLocalityPlaceType() {

		// given
		given(localityRepository.findById(any(Long.class))).willReturn(Optional.empty());
		// when
		Optional<DepartmentJpaEntity> optionalDepartment = departmentJpaEntityResolver.resolveDepartment(1L,
				PlaceType.LOCALITY);
		// then
		assertThat(optionalDepartment).isEmpty();
	}

	@Test
	void testResolveDepartmentOfGeoPlaceType() {
		GeoPlaceJpaEntity geoPlace = defaultGeoPlaceJpaEntity();
		// given
		given(geoPlaceRepository.findById(any(Long.class))).willReturn(Optional.of(geoPlace));
		// when
		Optional<DepartmentJpaEntity> optionalDepartment = departmentJpaEntityResolver.resolveDepartment(1L,
				PlaceType.GEO_PLACE);
		// then
		assertEquals(geoPlace.getDepartment(), optionalDepartment.get());
	}
}
