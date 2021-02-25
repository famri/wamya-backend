package com.excentria_it.wamya.adapter.persistence.adapter;

import static com.excentria_it.wamya.test.data.common.DelegationJpaTestData.*;
import static com.excentria_it.wamya.test.data.common.DepartmentJpaTestData.*;
import static com.excentria_it.wamya.test.data.common.GeoPlaceJpaTestData.*;
import static com.excentria_it.wamya.test.data.common.JourneyTravelInfoTestData.*;
import static com.excentria_it.wamya.test.data.common.LocalityJpaTestData.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.excentria_it.wamya.adapter.persistence.entity.DelegationJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.DelegationToDelegationTravelInfoJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.DelegationToDepartmentTravelInfoJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.DepartmentJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.DepartmentToDepartmentTravelInfoJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.GeoPlaceJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.GeoPlaceToDelegationTravelInfoJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.GeoPlaceToDepartmentTravelInfoJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.GeoPlaceToGeoPlaceTravelInfoJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.GeoPlaceToLocalityTravelInfoJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.LocalityJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.LocalityToDelegationTravelInfoJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.LocalityToDepartmentTravelInfoJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.LocalityToLocalityTravelInfoJpaEntity;
import com.excentria_it.wamya.adapter.persistence.repository.DelegationRepository;
import com.excentria_it.wamya.adapter.persistence.repository.DelegationToDelegationTravelInfoRepository;
import com.excentria_it.wamya.adapter.persistence.repository.DelegationToDepartmentTravelInfoRepository;
import com.excentria_it.wamya.adapter.persistence.repository.DepartmentRepository;
import com.excentria_it.wamya.adapter.persistence.repository.DepartmentToDepartmentTravelInfoRepository;
import com.excentria_it.wamya.adapter.persistence.repository.GeoPlaceRepository;
import com.excentria_it.wamya.adapter.persistence.repository.GeoPlaceToDelegationTravelInfoRepository;
import com.excentria_it.wamya.adapter.persistence.repository.GeoPlaceToDepartmentTravelInfoRepository;
import com.excentria_it.wamya.adapter.persistence.repository.GeoPlaceToGeoPlaceTravelInfoRepository;
import com.excentria_it.wamya.adapter.persistence.repository.GeoPlaceToLocalityTravelInfoRepository;
import com.excentria_it.wamya.adapter.persistence.repository.LocalityRepository;
import com.excentria_it.wamya.adapter.persistence.repository.LocalityToDelegationTravelInfoRepository;
import com.excentria_it.wamya.adapter.persistence.repository.LocalityToDepartmentTravelInfoRepository;
import com.excentria_it.wamya.adapter.persistence.repository.LocalityToLocalityTravelInfoRepository;
import com.excentria_it.wamya.domain.JourneyTravelInfo;
import com.excentria_it.wamya.domain.PlaceType;

@ExtendWith(MockitoExtension.class)
public class JourneyTravelInfoPersistenceAdapterTests {

	@Mock
	private LocalityToLocalityTravelInfoRepository localityToLocalityTravelInfoRepository;
	@Mock
	private LocalityToDelegationTravelInfoRepository localityToDelegationTravelInfoRepository;
	@Mock
	private LocalityToDepartmentTravelInfoRepository localityToDepartmentTravelInfoRepository;

	@Mock
	private DelegationToDelegationTravelInfoRepository delegationToDelegationTravelInfoRepository;
	@Mock
	private DelegationToDepartmentTravelInfoRepository delegationToDepartmentTravelInfoRepository;

	@Mock
	private DepartmentToDepartmentTravelInfoRepository departmentToDepartmentTravelInfoRepository;

	@Mock
	private GeoPlaceToGeoPlaceTravelInfoRepository geoPlaceToGeoPlaceTravelInfoRepository;
	@Mock
	private GeoPlaceToDepartmentTravelInfoRepository geoPlaceToDepartmentTravelInfoRepository;
	@Mock
	private GeoPlaceToDelegationTravelInfoRepository geoPlaceToDelegationTravelInfoRepository;
	@Mock
	private GeoPlaceToLocalityTravelInfoRepository geoPlaceToLocalityTravelInfoRepository;

	@Mock
	private LocalityRepository localityRepository;
	@Mock
	private DelegationRepository delegationRepository;
	@Mock
	private DepartmentRepository departmentRepository;
	@Mock
	private GeoPlaceRepository geoPlaceRepository;

	@InjectMocks
	private JourneyTravelInfoPersistenceAdapter journeyTravelInfoPersistenceAdapter;

	@Test
	void testLoadTravelInfoOfLocalityToLocality() {
		// given
		JourneyTravelInfo journeyTravelInfo = defaultJourneyTravelInfo();
		given(localityToLocalityTravelInfoRepository.findByLocalityOne_IdAndLocalityTwo_Id(any(Long.class),
				any(Long.class))).willReturn(Optional.of(journeyTravelInfo));
		// When
		Optional<JourneyTravelInfo> result = journeyTravelInfoPersistenceAdapter.loadTravelInfo(1L, PlaceType.LOCALITY,
				2L, PlaceType.LOCALITY);
		// then
		assertThat(result).isNotEmpty();
		assertEquals(journeyTravelInfo, result.get());
	}

	@Test
	void testLoadTravelInfoOfLocalityToDelegation() {
		// given
		JourneyTravelInfo journeyTravelInfo = defaultJourneyTravelInfo();
		given(localityToDelegationTravelInfoRepository.findByLocality_IdAndDelegation_Id(any(Long.class),
				any(Long.class))).willReturn(Optional.of(journeyTravelInfo));
		// When
		Optional<JourneyTravelInfo> result = journeyTravelInfoPersistenceAdapter.loadTravelInfo(1L, PlaceType.LOCALITY,
				2L, PlaceType.DELEGATION);
		// then
		assertThat(result).isNotEmpty();
		assertEquals(journeyTravelInfo, result.get());
	}

	@Test
	void testLoadTravelInfoOfLocalityToDepartment() {
		// given
		JourneyTravelInfo journeyTravelInfo = defaultJourneyTravelInfo();
		given(localityToDepartmentTravelInfoRepository.findByLocality_IdAndDepartment_Id(any(Long.class),
				any(Long.class))).willReturn(Optional.of(journeyTravelInfo));
		// When
		Optional<JourneyTravelInfo> result = journeyTravelInfoPersistenceAdapter.loadTravelInfo(1L, PlaceType.LOCALITY,
				2L, PlaceType.DEPARTMENT);
		// then
		assertThat(result).isNotEmpty();
		assertEquals(journeyTravelInfo, result.get());
	}

	@Test
	void testLoadTravelInfoOfLocalityToGeoPlace() {
		// given
		JourneyTravelInfo journeyTravelInfo = defaultJourneyTravelInfo();
		given(geoPlaceToLocalityTravelInfoRepository.findByGeoPlace_IdAndLocality_Id(any(Long.class), any(Long.class)))
				.willReturn(Optional.of(journeyTravelInfo));
		// When
		Optional<JourneyTravelInfo> result = journeyTravelInfoPersistenceAdapter.loadTravelInfo(1L, PlaceType.LOCALITY,
				2L, PlaceType.GEO_PLACE);
		// then
		assertThat(result).isNotEmpty();
		assertEquals(journeyTravelInfo, result.get());

	}

	@Test
	void testLoadTravelInfoOfDelegationToDelegation() {
		// given
		JourneyTravelInfo journeyTravelInfo = defaultJourneyTravelInfo();
		given(delegationToDelegationTravelInfoRepository.findByDelegationOne_IdAndDelegationTwo_Id(any(Long.class),
				any(Long.class))).willReturn(Optional.of(journeyTravelInfo));
		// When
		Optional<JourneyTravelInfo> result = journeyTravelInfoPersistenceAdapter.loadTravelInfo(1L,
				PlaceType.DELEGATION, 2L, PlaceType.DELEGATION);
		// then
		assertThat(result).isNotEmpty();
		assertEquals(journeyTravelInfo, result.get());
	}

	@Test
	void testLoadTravelInfoOfDelegationToLocality() {
		// given
		JourneyTravelInfo journeyTravelInfo = defaultJourneyTravelInfo();
		given(localityToDelegationTravelInfoRepository.findByLocality_IdAndDelegation_Id(any(Long.class),
				any(Long.class))).willReturn(Optional.of(journeyTravelInfo));
		// When
		Optional<JourneyTravelInfo> result = journeyTravelInfoPersistenceAdapter.loadTravelInfo(1L,
				PlaceType.DELEGATION, 2L, PlaceType.LOCALITY);
		// then
		assertThat(result).isNotEmpty();
		assertEquals(journeyTravelInfo, result.get());
	}

	@Test
	void testLoadTravelInfoOfDelegationToDepartment() {
		// given
		JourneyTravelInfo journeyTravelInfo = defaultJourneyTravelInfo();
		given(delegationToDepartmentTravelInfoRepository.findByDelegation_IdAndDepartment_Id(any(Long.class),
				any(Long.class))).willReturn(Optional.of(journeyTravelInfo));
		// When
		Optional<JourneyTravelInfo> result = journeyTravelInfoPersistenceAdapter.loadTravelInfo(1L,
				PlaceType.DELEGATION, 2L, PlaceType.DEPARTMENT);
		// then
		assertThat(result).isNotEmpty();
		assertEquals(journeyTravelInfo, result.get());
	}

	@Test
	void testLoadTravelInfoOfDelegationToGeoPlace() {
		// given

		JourneyTravelInfo journeyTravelInfo = defaultJourneyTravelInfo();
		given(geoPlaceToDelegationTravelInfoRepository.findByGeoPlace_IdAndDelegation_Id(any(Long.class),
				any(Long.class))).willReturn(Optional.of(journeyTravelInfo));
		// When
		Optional<JourneyTravelInfo> result = journeyTravelInfoPersistenceAdapter.loadTravelInfo(1L,
				PlaceType.DELEGATION, 2L, PlaceType.GEO_PLACE);
		// then
		assertThat(result).isNotEmpty();
		assertEquals(journeyTravelInfo, result.get());

	}

	@Test
	void testLoadTravelInfoOfDepartmentToDepartment() {
		// given
		JourneyTravelInfo journeyTravelInfo = defaultJourneyTravelInfo();
		given(departmentToDepartmentTravelInfoRepository.findByDepartmentOne_IdAndDepartmentTwo_Id(any(Long.class),
				any(Long.class))).willReturn(Optional.of(journeyTravelInfo));
		// When
		Optional<JourneyTravelInfo> result = journeyTravelInfoPersistenceAdapter.loadTravelInfo(1L,
				PlaceType.DEPARTMENT, 2L, PlaceType.DEPARTMENT);
		// then
		assertThat(result).isNotEmpty();
		assertEquals(journeyTravelInfo, result.get());
	}

	@Test
	void testLoadTravelInfoOfDepartmentToLocality() {
		// given
		JourneyTravelInfo journeyTravelInfo = defaultJourneyTravelInfo();
		given(localityToDepartmentTravelInfoRepository.findByLocality_IdAndDepartment_Id(any(Long.class),
				any(Long.class))).willReturn(Optional.of(journeyTravelInfo));
		// When
		Optional<JourneyTravelInfo> result = journeyTravelInfoPersistenceAdapter.loadTravelInfo(1L,
				PlaceType.DEPARTMENT, 2L, PlaceType.LOCALITY);
		// then
		assertThat(result).isNotEmpty();
		assertEquals(journeyTravelInfo, result.get());
	}

	@Test
	void testLoadTravelInfoOfDepartmentToDelegation() {
		// given
		JourneyTravelInfo journeyTravelInfo = defaultJourneyTravelInfo();
		given(delegationToDepartmentTravelInfoRepository.findByDelegation_IdAndDepartment_Id(any(Long.class),
				any(Long.class))).willReturn(Optional.of(journeyTravelInfo));
		// When
		Optional<JourneyTravelInfo> result = journeyTravelInfoPersistenceAdapter.loadTravelInfo(1L,
				PlaceType.DEPARTMENT, 2L, PlaceType.DELEGATION);
		// then
		assertThat(result).isNotEmpty();
		assertEquals(journeyTravelInfo, result.get());
	}

	@Test
	void testLoadTravelInfoOfDepartmentToGeoPlace() {
		// given

		JourneyTravelInfo journeyTravelInfo = defaultJourneyTravelInfo();
		given(geoPlaceToDepartmentTravelInfoRepository.findByGeoPlace_IdAndDepartment_Id(any(Long.class),
				any(Long.class))).willReturn(Optional.of(journeyTravelInfo));
		// When
		Optional<JourneyTravelInfo> result = journeyTravelInfoPersistenceAdapter.loadTravelInfo(1L,
				PlaceType.DEPARTMENT, 2L, PlaceType.GEO_PLACE);
		// then
		assertThat(result).isNotEmpty();
		assertEquals(journeyTravelInfo, result.get());
	}

	@Test
	void testLoadTravelInfoOfGeoPlaceToLocality() {
		// given
		JourneyTravelInfo journeyTravelInfo = defaultJourneyTravelInfo();
		given(geoPlaceToLocalityTravelInfoRepository.findByGeoPlace_IdAndLocality_Id(any(Long.class), any(Long.class)))
				.willReturn(Optional.of(journeyTravelInfo));
		// When
		Optional<JourneyTravelInfo> result = journeyTravelInfoPersistenceAdapter.loadTravelInfo(1L, PlaceType.GEO_PLACE,
				2L, PlaceType.LOCALITY);
		// then
		assertThat(result).isNotEmpty();
		assertEquals(journeyTravelInfo, result.get());
	}

	@Test
	void testLoadTravelInfoOfGeoPlaceToDelegation() {
		// given
		JourneyTravelInfo journeyTravelInfo = defaultJourneyTravelInfo();
		given(geoPlaceToDelegationTravelInfoRepository.findByGeoPlace_IdAndDelegation_Id(any(Long.class),
				any(Long.class))).willReturn(Optional.of(journeyTravelInfo));
		// When
		Optional<JourneyTravelInfo> result = journeyTravelInfoPersistenceAdapter.loadTravelInfo(1L, PlaceType.GEO_PLACE,
				2L, PlaceType.DELEGATION);
		// then
		assertThat(result).isNotEmpty();
		assertEquals(journeyTravelInfo, result.get());
	}

	@Test
	void testLoadTravelInfoOfGeoPlaceToDepartment() {
		// given
		JourneyTravelInfo journeyTravelInfo = defaultJourneyTravelInfo();
		given(geoPlaceToDepartmentTravelInfoRepository.findByGeoPlace_IdAndDepartment_Id(any(Long.class),
				any(Long.class))).willReturn(Optional.of(journeyTravelInfo));
		// When
		Optional<JourneyTravelInfo> result = journeyTravelInfoPersistenceAdapter.loadTravelInfo(1L, PlaceType.GEO_PLACE,
				2L, PlaceType.DEPARTMENT);
		// then
		assertThat(result).isNotEmpty();
		assertEquals(journeyTravelInfo, result.get());
	}

	@Test
	void testLoadTravelInfoOfGeoPlaceToGeoPlace() {
		// given
		JourneyTravelInfo journeyTravelInfo = defaultJourneyTravelInfo();
		given(geoPlaceToGeoPlaceTravelInfoRepository.findByGeoPlaceOne_IdAndGeoPlaceTwo_Id(any(Long.class),
				any(Long.class))).willReturn(Optional.of(journeyTravelInfo));

		// When
		Optional<JourneyTravelInfo> result = journeyTravelInfoPersistenceAdapter.loadTravelInfo(1L, PlaceType.GEO_PLACE,
				2L, PlaceType.GEO_PLACE);
		// then
		assertThat(result).isNotEmpty();
		assertEquals(journeyTravelInfo, result.get());
	}

	@Test
	void testCreateTravelInfoOfLocalityToLocality() {
		// given
		JourneyTravelInfo journeyTravelInfo = defaultJourneyTravelInfo();
		LocalityJpaEntity locality = defaultExistentLocalityJpaEntity();
		given(localityRepository.findById(any(Long.class))).willReturn(Optional.of(locality));
		// When
		journeyTravelInfoPersistenceAdapter.createTravelInfo(1L, PlaceType.LOCALITY, 2L, PlaceType.LOCALITY,
				journeyTravelInfo);
		// then
		ArgumentCaptor<LocalityToLocalityTravelInfoJpaEntity> captor = ArgumentCaptor
				.forClass(LocalityToLocalityTravelInfoJpaEntity.class);
		then(localityToLocalityTravelInfoRepository).should(times(1)).save(captor.capture());
		assertEquals(locality, captor.getValue().getLocalityOne());
		assertEquals(locality, captor.getValue().getLocalityTwo());
		assertEquals(journeyTravelInfo.getHours(), captor.getValue().getHours());
		assertEquals(journeyTravelInfo.getMinutes(), captor.getValue().getMinutes());
		assertEquals(journeyTravelInfo.getDistance(), captor.getValue().getDistance());
	}

	@Test
	void testCreateTravelInfoOfLocalityToSameLocality() {
		// given
		JourneyTravelInfo journeyTravelInfo = defaultJourneyTravelInfo();
		LocalityJpaEntity locality = defaultExistentLocalityJpaEntity();
		given(localityRepository.findById(any(Long.class))).willReturn(Optional.of(locality));
		// When
		journeyTravelInfoPersistenceAdapter.createTravelInfo(1L, PlaceType.LOCALITY, 1L, PlaceType.LOCALITY,
				journeyTravelInfo);
		// then
		ArgumentCaptor<LocalityToLocalityTravelInfoJpaEntity> captor = ArgumentCaptor
				.forClass(LocalityToLocalityTravelInfoJpaEntity.class);
		then(localityToLocalityTravelInfoRepository).should(times(1)).save(captor.capture());
		assertEquals(locality, captor.getValue().getLocalityOne());
		assertEquals(locality, captor.getValue().getLocalityTwo());
		assertEquals(journeyTravelInfo.getHours(), captor.getValue().getHours());
		assertEquals(journeyTravelInfo.getMinutes(), captor.getValue().getMinutes());
		assertEquals(journeyTravelInfo.getDistance(), captor.getValue().getDistance());
	}

	@Test
	void testCreateTravelInfoOfLocalityToDelegation() {
		// given
		JourneyTravelInfo journeyTravelInfo = defaultJourneyTravelInfo();
		LocalityJpaEntity locality = defaultExistentLocalityJpaEntity();
		DelegationJpaEntity delegation = defaultExistentDelegationJpaEntity();
		given(localityRepository.findById(any(Long.class))).willReturn(Optional.of(locality));
		given(delegationRepository.findById(any(Long.class))).willReturn(Optional.of(delegation));
		// When
		journeyTravelInfoPersistenceAdapter.createTravelInfo(1L, PlaceType.LOCALITY, 2L, PlaceType.DELEGATION,
				journeyTravelInfo);
		// then
		ArgumentCaptor<LocalityToDelegationTravelInfoJpaEntity> captor = ArgumentCaptor
				.forClass(LocalityToDelegationTravelInfoJpaEntity.class);
		then(localityToDelegationTravelInfoRepository).should(times(1)).save(captor.capture());
		assertEquals(locality, captor.getValue().getLocality());
		assertEquals(delegation, captor.getValue().getDelegation());
		assertEquals(journeyTravelInfo.getHours(), captor.getValue().getHours());
		assertEquals(journeyTravelInfo.getMinutes(), captor.getValue().getMinutes());
		assertEquals(journeyTravelInfo.getDistance(), captor.getValue().getDistance());
	}

	@Test
	void testCreateTravelInfoOfLocalityToDepartment() {
		// given
		JourneyTravelInfo journeyTravelInfo = defaultJourneyTravelInfo();
		LocalityJpaEntity locality = defaultExistentLocalityJpaEntity();
		DepartmentJpaEntity department = defaultExistentDepartmentJpaEntity();
		given(localityRepository.findById(any(Long.class))).willReturn(Optional.of(locality));
		given(departmentRepository.findById(any(Long.class))).willReturn(Optional.of(department));
		// When
		journeyTravelInfoPersistenceAdapter.createTravelInfo(1L, PlaceType.LOCALITY, 2L, PlaceType.DEPARTMENT,
				journeyTravelInfo);
		// then
		ArgumentCaptor<LocalityToDepartmentTravelInfoJpaEntity> captor = ArgumentCaptor
				.forClass(LocalityToDepartmentTravelInfoJpaEntity.class);
		then(localityToDepartmentTravelInfoRepository).should(times(1)).save(captor.capture());
		assertEquals(locality, captor.getValue().getLocality());
		assertEquals(department, captor.getValue().getDepartment());
		assertEquals(journeyTravelInfo.getHours(), captor.getValue().getHours());
		assertEquals(journeyTravelInfo.getMinutes(), captor.getValue().getMinutes());
		assertEquals(journeyTravelInfo.getDistance(), captor.getValue().getDistance());
	}

	@Test
	void testCreateTravelInfoOfLocalityToGeoPlace() {
		// given
		JourneyTravelInfo journeyTravelInfo = defaultJourneyTravelInfo();
		LocalityJpaEntity locality = defaultExistentLocalityJpaEntity();
		GeoPlaceJpaEntity geoPlace = defaultGeoPlaceJpaEntity();
		given(localityRepository.findById(any(Long.class))).willReturn(Optional.of(locality));
		given(geoPlaceRepository.findById(any(Long.class))).willReturn(Optional.of(geoPlace));
		// When
		journeyTravelInfoPersistenceAdapter.createTravelInfo(1L, PlaceType.LOCALITY, 2L, PlaceType.GEO_PLACE,
				journeyTravelInfo);
		// then
		ArgumentCaptor<GeoPlaceToLocalityTravelInfoJpaEntity> captor = ArgumentCaptor
				.forClass(GeoPlaceToLocalityTravelInfoJpaEntity.class);
		then(geoPlaceToLocalityTravelInfoRepository).should(times(1)).save(captor.capture());
		assertEquals(locality, captor.getValue().getLocality());
		assertEquals(geoPlace, captor.getValue().getGeoPlace());
		assertEquals(journeyTravelInfo.getHours(), captor.getValue().getHours());
		assertEquals(journeyTravelInfo.getMinutes(), captor.getValue().getMinutes());
		assertEquals(journeyTravelInfo.getDistance(), captor.getValue().getDistance());

	}

	@Test
	void testCreateTravelInfoOfDelegationToDelegation() {
		// given
		JourneyTravelInfo journeyTravelInfo = defaultJourneyTravelInfo();
		DelegationJpaEntity delegation = defaultExistentDelegationJpaEntity();
		given(delegationRepository.findById(any(Long.class))).willReturn(Optional.of(delegation));
		// When
		journeyTravelInfoPersistenceAdapter.createTravelInfo(1L, PlaceType.DELEGATION, 2L, PlaceType.DELEGATION,
				journeyTravelInfo);
		// then
		ArgumentCaptor<DelegationToDelegationTravelInfoJpaEntity> captor = ArgumentCaptor
				.forClass(DelegationToDelegationTravelInfoJpaEntity.class);
		then(delegationToDelegationTravelInfoRepository).should(times(1)).save(captor.capture());
		assertEquals(delegation, captor.getValue().getDelegationOne());
		assertEquals(delegation, captor.getValue().getDelegationTwo());
		assertEquals(journeyTravelInfo.getHours(), captor.getValue().getHours());
		assertEquals(journeyTravelInfo.getMinutes(), captor.getValue().getMinutes());
		assertEquals(journeyTravelInfo.getDistance(), captor.getValue().getDistance());
	}

	@Test
	void testCreateTravelInfoOfDelegationToDepartment() {
		// given
		JourneyTravelInfo journeyTravelInfo = defaultJourneyTravelInfo();
		DelegationJpaEntity delegation = defaultExistentDelegationJpaEntity();
		DepartmentJpaEntity department = defaultExistentDepartmentJpaEntity();
		given(delegationRepository.findById(any(Long.class))).willReturn(Optional.of(delegation));
		given(departmentRepository.findById(any(Long.class))).willReturn(Optional.of(department));
		// When
		journeyTravelInfoPersistenceAdapter.createTravelInfo(1L, PlaceType.DELEGATION, 2L, PlaceType.DEPARTMENT,
				journeyTravelInfo);
		// then
		ArgumentCaptor<DelegationToDepartmentTravelInfoJpaEntity> captor = ArgumentCaptor
				.forClass(DelegationToDepartmentTravelInfoJpaEntity.class);
		then(delegationToDepartmentTravelInfoRepository).should(times(1)).save(captor.capture());
		assertEquals(delegation, captor.getValue().getDelegation());
		assertEquals(department, captor.getValue().getDepartment());
		assertEquals(journeyTravelInfo.getHours(), captor.getValue().getHours());
		assertEquals(journeyTravelInfo.getMinutes(), captor.getValue().getMinutes());
		assertEquals(journeyTravelInfo.getDistance(), captor.getValue().getDistance());
	}

	@Test
	void testCreateTravelInfoOfDelegationToLocality() {
		// given
		JourneyTravelInfo journeyTravelInfo = defaultJourneyTravelInfo();
		DelegationJpaEntity delegation = defaultExistentDelegationJpaEntity();
		LocalityJpaEntity locality = defaultExistentLocalityJpaEntity();
		given(delegationRepository.findById(any(Long.class))).willReturn(Optional.of(delegation));
		given(localityRepository.findById(any(Long.class))).willReturn(Optional.of(locality));
		// When
		journeyTravelInfoPersistenceAdapter.createTravelInfo(1L, PlaceType.DELEGATION, 2L, PlaceType.LOCALITY,
				journeyTravelInfo);
		// then
		ArgumentCaptor<LocalityToDelegationTravelInfoJpaEntity> captor = ArgumentCaptor
				.forClass(LocalityToDelegationTravelInfoJpaEntity.class);
		then(localityToDelegationTravelInfoRepository).should(times(1)).save(captor.capture());
		assertEquals(delegation, captor.getValue().getDelegation());
		assertEquals(locality, captor.getValue().getLocality());
		assertEquals(journeyTravelInfo.getHours(), captor.getValue().getHours());
		assertEquals(journeyTravelInfo.getMinutes(), captor.getValue().getMinutes());
		assertEquals(journeyTravelInfo.getDistance(), captor.getValue().getDistance());
	}

	@Test
	void testCreateTravelInfoOfDelegationToSameDelegation() {
		// given
		JourneyTravelInfo journeyTravelInfo = defaultJourneyTravelInfo();
		DelegationJpaEntity delegation = defaultExistentDelegationJpaEntity();
		given(delegationRepository.findById(any(Long.class))).willReturn(Optional.of(delegation));
		// When
		journeyTravelInfoPersistenceAdapter.createTravelInfo(1L, PlaceType.DELEGATION, 1L, PlaceType.DELEGATION,
				journeyTravelInfo);
		// then
		ArgumentCaptor<DelegationToDelegationTravelInfoJpaEntity> captor = ArgumentCaptor
				.forClass(DelegationToDelegationTravelInfoJpaEntity.class);
		then(delegationToDelegationTravelInfoRepository).should(times(1)).save(captor.capture());
		assertEquals(delegation, captor.getValue().getDelegationOne());
		assertEquals(delegation, captor.getValue().getDelegationTwo());
		assertEquals(journeyTravelInfo.getHours(), captor.getValue().getHours());
		assertEquals(journeyTravelInfo.getMinutes(), captor.getValue().getMinutes());
		assertEquals(journeyTravelInfo.getDistance(), captor.getValue().getDistance());
	}

	@Test
	void testCreateTravelInfoOfDelegationToGeoPlace() {
		// given
		JourneyTravelInfo journeyTravelInfo = defaultJourneyTravelInfo();
		DelegationJpaEntity delegation = defaultExistentDelegationJpaEntity();
		GeoPlaceJpaEntity geoPlace = defaultGeoPlaceJpaEntity();
		given(delegationRepository.findById(any(Long.class))).willReturn(Optional.of(delegation));
		given(geoPlaceRepository.findById(any(Long.class))).willReturn(Optional.of(geoPlace));
		// When
		journeyTravelInfoPersistenceAdapter.createTravelInfo(1L, PlaceType.DELEGATION, 2L, PlaceType.GEO_PLACE,
				journeyTravelInfo);
		// then
		ArgumentCaptor<GeoPlaceToDelegationTravelInfoJpaEntity> captor = ArgumentCaptor
				.forClass(GeoPlaceToDelegationTravelInfoJpaEntity.class);
		then(geoPlaceToDelegationTravelInfoRepository).should(times(1)).save(captor.capture());
		assertEquals(delegation, captor.getValue().getDelegation());
		assertEquals(geoPlace, captor.getValue().getGeoPlace());
		assertEquals(journeyTravelInfo.getHours(), captor.getValue().getHours());
		assertEquals(journeyTravelInfo.getMinutes(), captor.getValue().getMinutes());
		assertEquals(journeyTravelInfo.getDistance(), captor.getValue().getDistance());

	}

	@Test
	void testCreateTravelInfoOfDepartmentToDepartment() {
		// given
		JourneyTravelInfo journeyTravelInfo = defaultJourneyTravelInfo();
		DepartmentJpaEntity department = defaultExistentDepartmentJpaEntity();
		given(departmentRepository.findById(any(Long.class))).willReturn(Optional.of(department));
		// When
		journeyTravelInfoPersistenceAdapter.createTravelInfo(1L, PlaceType.DEPARTMENT, 2L, PlaceType.DEPARTMENT,
				journeyTravelInfo);
		// then
		ArgumentCaptor<DepartmentToDepartmentTravelInfoJpaEntity> captor = ArgumentCaptor
				.forClass(DepartmentToDepartmentTravelInfoJpaEntity.class);
		then(departmentToDepartmentTravelInfoRepository).should(times(1)).save(captor.capture());
		assertEquals(department, captor.getValue().getDepartmentOne());
		assertEquals(department, captor.getValue().getDepartmentTwo());
		assertEquals(journeyTravelInfo.getHours(), captor.getValue().getHours());
		assertEquals(journeyTravelInfo.getMinutes(), captor.getValue().getMinutes());
		assertEquals(journeyTravelInfo.getDistance(), captor.getValue().getDistance());
	}

	@Test
	void testCreateTravelInfoOfDepartmentToDelegation() {
		// given
		JourneyTravelInfo journeyTravelInfo = defaultJourneyTravelInfo();
		DepartmentJpaEntity department = defaultExistentDepartmentJpaEntity();
		DelegationJpaEntity delegation = defaultExistentDelegationJpaEntity();
		given(departmentRepository.findById(any(Long.class))).willReturn(Optional.of(department));
		given(delegationRepository.findById(any(Long.class))).willReturn(Optional.of(delegation));
		// When
		journeyTravelInfoPersistenceAdapter.createTravelInfo(1L, PlaceType.DEPARTMENT, 2L, PlaceType.DELEGATION,
				journeyTravelInfo);
		// then
		ArgumentCaptor<DelegationToDepartmentTravelInfoJpaEntity> captor = ArgumentCaptor
				.forClass(DelegationToDepartmentTravelInfoJpaEntity.class);
		then(delegationToDepartmentTravelInfoRepository).should(times(1)).save(captor.capture());
		assertEquals(department, captor.getValue().getDepartment());
		assertEquals(delegation, captor.getValue().getDelegation());
		assertEquals(journeyTravelInfo.getHours(), captor.getValue().getHours());
		assertEquals(journeyTravelInfo.getMinutes(), captor.getValue().getMinutes());
		assertEquals(journeyTravelInfo.getDistance(), captor.getValue().getDistance());
	}

	@Test
	void testCreateTravelInfoOfDepartmentToLocality() {
		// given
		JourneyTravelInfo journeyTravelInfo = defaultJourneyTravelInfo();
		DepartmentJpaEntity department = defaultExistentDepartmentJpaEntity();
		LocalityJpaEntity locality = defaultExistentLocalityJpaEntity();
		given(departmentRepository.findById(any(Long.class))).willReturn(Optional.of(department));
		given(localityRepository.findById(any(Long.class))).willReturn(Optional.of(locality));
		// When
		journeyTravelInfoPersistenceAdapter.createTravelInfo(1L, PlaceType.DEPARTMENT, 2L, PlaceType.LOCALITY,
				journeyTravelInfo);
		// then
		ArgumentCaptor<LocalityToDepartmentTravelInfoJpaEntity> captor = ArgumentCaptor
				.forClass(LocalityToDepartmentTravelInfoJpaEntity.class);
		then(localityToDepartmentTravelInfoRepository).should(times(1)).save(captor.capture());
		assertEquals(department, captor.getValue().getDepartment());
		assertEquals(locality, captor.getValue().getLocality());
		assertEquals(journeyTravelInfo.getHours(), captor.getValue().getHours());
		assertEquals(journeyTravelInfo.getMinutes(), captor.getValue().getMinutes());
		assertEquals(journeyTravelInfo.getDistance(), captor.getValue().getDistance());
	}

	@Test
	void testCreateTravelInfoOfDepartmentToSameDepartment() {
		// given
		JourneyTravelInfo journeyTravelInfo = defaultJourneyTravelInfo();
		DepartmentJpaEntity department = defaultExistentDepartmentJpaEntity();
		given(departmentRepository.findById(any(Long.class))).willReturn(Optional.of(department));
		// When
		journeyTravelInfoPersistenceAdapter.createTravelInfo(1L, PlaceType.DEPARTMENT, 1L, PlaceType.DEPARTMENT,
				journeyTravelInfo);
		// then
		ArgumentCaptor<DepartmentToDepartmentTravelInfoJpaEntity> captor = ArgumentCaptor
				.forClass(DepartmentToDepartmentTravelInfoJpaEntity.class);
		then(departmentToDepartmentTravelInfoRepository).should(times(1)).save(captor.capture());
		assertEquals(department, captor.getValue().getDepartmentOne());
		assertEquals(department, captor.getValue().getDepartmentTwo());
		assertEquals(journeyTravelInfo.getHours(), captor.getValue().getHours());
		assertEquals(journeyTravelInfo.getMinutes(), captor.getValue().getMinutes());
		assertEquals(journeyTravelInfo.getDistance(), captor.getValue().getDistance());
	}

	@Test
	void testCreateTravelInfoOfDepartmentToGeoPlace() {
		// given
		JourneyTravelInfo journeyTravelInfo = defaultJourneyTravelInfo();
		DepartmentJpaEntity department = defaultExistentDepartmentJpaEntity();
		GeoPlaceJpaEntity geoPlace = defaultGeoPlaceJpaEntity();
		given(departmentRepository.findById(any(Long.class))).willReturn(Optional.of(department));
		given(geoPlaceRepository.findById(any(Long.class))).willReturn(Optional.of(geoPlace));
		// When
		journeyTravelInfoPersistenceAdapter.createTravelInfo(1L, PlaceType.DEPARTMENT, 2L, PlaceType.GEO_PLACE,
				journeyTravelInfo);
		// then
		ArgumentCaptor<GeoPlaceToDepartmentTravelInfoJpaEntity> captor = ArgumentCaptor
				.forClass(GeoPlaceToDepartmentTravelInfoJpaEntity.class);
		then(geoPlaceToDepartmentTravelInfoRepository).should(times(1)).save(captor.capture());
		assertEquals(department, captor.getValue().getDepartment());
		assertEquals(geoPlace, captor.getValue().getGeoPlace());
		assertEquals(journeyTravelInfo.getHours(), captor.getValue().getHours());
		assertEquals(journeyTravelInfo.getMinutes(), captor.getValue().getMinutes());
		assertEquals(journeyTravelInfo.getDistance(), captor.getValue().getDistance());

	}

	@Test
	void testCreateTravelInfoOfGeoPlaceToLocality() {
		// given
		JourneyTravelInfo journeyTravelInfo = defaultJourneyTravelInfo();
		GeoPlaceJpaEntity geoPlace = defaultGeoPlaceJpaEntity();
		LocalityJpaEntity locality = defaultExistentLocalityJpaEntity();

		given(geoPlaceRepository.findById(any(Long.class))).willReturn(Optional.of(geoPlace));
		given(localityRepository.findById(any(Long.class))).willReturn(Optional.of(locality));
		// When
		journeyTravelInfoPersistenceAdapter.createTravelInfo(1L, PlaceType.GEO_PLACE, 2L, PlaceType.LOCALITY,
				journeyTravelInfo);
		// then
		ArgumentCaptor<GeoPlaceToLocalityTravelInfoJpaEntity> captor = ArgumentCaptor
				.forClass(GeoPlaceToLocalityTravelInfoJpaEntity.class);
		then(geoPlaceToLocalityTravelInfoRepository).should(times(1)).save(captor.capture());
		assertEquals(geoPlace, captor.getValue().getGeoPlace());
		assertEquals(locality, captor.getValue().getLocality());
		assertEquals(journeyTravelInfo.getHours(), captor.getValue().getHours());
		assertEquals(journeyTravelInfo.getMinutes(), captor.getValue().getMinutes());
		assertEquals(journeyTravelInfo.getDistance(), captor.getValue().getDistance());
	}

	@Test
	void testCreateTravelInfoOfGeoPlaceToDelegation() {
		// given
		JourneyTravelInfo journeyTravelInfo = defaultJourneyTravelInfo();
		GeoPlaceJpaEntity geoPlace = defaultGeoPlaceJpaEntity();
		DelegationJpaEntity delegation = defaultExistentDelegationJpaEntity();

		given(geoPlaceRepository.findById(any(Long.class))).willReturn(Optional.of(geoPlace));
		given(delegationRepository.findById(any(Long.class))).willReturn(Optional.of(delegation));
		// When
		journeyTravelInfoPersistenceAdapter.createTravelInfo(1L, PlaceType.GEO_PLACE, 2L, PlaceType.DELEGATION,
				journeyTravelInfo);
		// then
		ArgumentCaptor<GeoPlaceToDelegationTravelInfoJpaEntity> captor = ArgumentCaptor
				.forClass(GeoPlaceToDelegationTravelInfoJpaEntity.class);
		then(geoPlaceToDelegationTravelInfoRepository).should(times(1)).save(captor.capture());
		assertEquals(geoPlace, captor.getValue().getGeoPlace());
		assertEquals(delegation, captor.getValue().getDelegation());
		assertEquals(journeyTravelInfo.getHours(), captor.getValue().getHours());
		assertEquals(journeyTravelInfo.getMinutes(), captor.getValue().getMinutes());
		assertEquals(journeyTravelInfo.getDistance(), captor.getValue().getDistance());
	}

	@Test
	void testCreateTravelInfoOfGeoPlaceToDepartment() {
		// given
		JourneyTravelInfo journeyTravelInfo = defaultJourneyTravelInfo();
		GeoPlaceJpaEntity geoPlace = defaultGeoPlaceJpaEntity();
		DepartmentJpaEntity department = defaultExistentDepartmentJpaEntity();

		given(geoPlaceRepository.findById(any(Long.class))).willReturn(Optional.of(geoPlace));
		given(departmentRepository.findById(any(Long.class))).willReturn(Optional.of(department));
		// When
		journeyTravelInfoPersistenceAdapter.createTravelInfo(1L, PlaceType.GEO_PLACE, 2L, PlaceType.DEPARTMENT,
				journeyTravelInfo);
		// then
		ArgumentCaptor<GeoPlaceToDepartmentTravelInfoJpaEntity> captor = ArgumentCaptor
				.forClass(GeoPlaceToDepartmentTravelInfoJpaEntity.class);
		then(geoPlaceToDepartmentTravelInfoRepository).should(times(1)).save(captor.capture());
		assertEquals(geoPlace, captor.getValue().getGeoPlace());
		assertEquals(department, captor.getValue().getDepartment());
		assertEquals(journeyTravelInfo.getHours(), captor.getValue().getHours());
		assertEquals(journeyTravelInfo.getMinutes(), captor.getValue().getMinutes());
		assertEquals(journeyTravelInfo.getDistance(), captor.getValue().getDistance());
	}

	@Test
	void testCreateTravelInfoOfGeoPlaceToGeoPlace() {
		// given
		JourneyTravelInfo journeyTravelInfo = defaultJourneyTravelInfo();
		GeoPlaceJpaEntity geoPlace = defaultGeoPlaceJpaEntity();

		given(geoPlaceRepository.findById(any(Long.class))).willReturn(Optional.of(geoPlace));

		// When
		journeyTravelInfoPersistenceAdapter.createTravelInfo(1L, PlaceType.GEO_PLACE, 2L, PlaceType.GEO_PLACE,
				journeyTravelInfo);
		// then
		ArgumentCaptor<GeoPlaceToGeoPlaceTravelInfoJpaEntity> captor = ArgumentCaptor
				.forClass(GeoPlaceToGeoPlaceTravelInfoJpaEntity.class);
		then(geoPlaceToGeoPlaceTravelInfoRepository).should(times(1)).save(captor.capture());
		assertEquals(geoPlace, captor.getValue().getGeoPlaceOne());
		assertEquals(geoPlace, captor.getValue().getGeoPlaceTwo());
		assertEquals(journeyTravelInfo.getHours(), captor.getValue().getHours());
		assertEquals(journeyTravelInfo.getMinutes(), captor.getValue().getMinutes());
		assertEquals(journeyTravelInfo.getDistance(), captor.getValue().getDistance());
	}
}
