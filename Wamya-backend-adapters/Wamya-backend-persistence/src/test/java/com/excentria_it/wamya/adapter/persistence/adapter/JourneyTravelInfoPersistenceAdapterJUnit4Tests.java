package com.excentria_it.wamya.adapter.persistence.adapter;

import static com.excentria_it.wamya.test.data.common.DelegationJpaTestData.*;
import static com.excentria_it.wamya.test.data.common.DepartmentJpaTestData.*;
import static com.excentria_it.wamya.test.data.common.GeoPlaceJpaTestData.*;
import static com.excentria_it.wamya.test.data.common.JourneyTravelInfoTestData.*;
import static com.excentria_it.wamya.test.data.common.LocalityJpaTestData.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.excentria_it.wamya.adapter.persistence.entity.DelegationJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.DelegationToDelegationTravelInfoJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.DelegationToDepartmentTravelInfoJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.DepartmentJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.DepartmentToDepartmentTravelInfoJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.GeoPlaceJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.GeoPlaceToDelegationTravelInfoJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.GeoPlaceToDepartmentTravelInfoJpaEntity;
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

@RunWith(PowerMockRunner.class)
public class JourneyTravelInfoPersistenceAdapterJUnit4Tests {
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
	@PrepareForTest(PlaceType.class)
	public void testLoadTravelInfoOfLocalityToUnknownPlaceType() throws Exception {

		PlaceType unknownPlaceType = PowerMockito.mock(PlaceType.class);
		PowerMockito.when(unknownPlaceType.ordinal()).thenReturn(4);
		PowerMockito.mockStatic(PlaceType.class);

		PlaceType[] placeTypes = new PlaceType[] { PlaceType.LOCALITY, PlaceType.DELEGATION, PlaceType.DEPARTMENT,
				PlaceType.GEO_PLACE, unknownPlaceType };
		PowerMockito.when(PlaceType.values()).thenAnswer((Answer<PlaceType[]>) invocation -> placeTypes);
		// when
		Optional<JourneyTravelInfo> journeyTravelInfo = journeyTravelInfoPersistenceAdapter.loadTravelInfo(1L,
				PlaceType.LOCALITY, 2L, unknownPlaceType);
		// then
		assertThat(journeyTravelInfo).isEmpty();
	}

	@Test
	@PrepareForTest(PlaceType.class)
	public void testLoadTravelInfoOfDelegationToUnknownPlaceType() throws Exception {

		PlaceType unknownPlaceType = PowerMockito.mock(PlaceType.class);
		PowerMockito.when(unknownPlaceType.ordinal()).thenReturn(4);
		PowerMockito.mockStatic(PlaceType.class);

		PlaceType[] placeTypes = new PlaceType[] { PlaceType.LOCALITY, PlaceType.DELEGATION, PlaceType.DEPARTMENT,
				PlaceType.GEO_PLACE, unknownPlaceType };
		PowerMockito.when(PlaceType.values()).thenAnswer((Answer<PlaceType[]>) invocation -> placeTypes);
		// when
		Optional<JourneyTravelInfo> journeyTravelInfo = journeyTravelInfoPersistenceAdapter.loadTravelInfo(1L,
				PlaceType.DELEGATION, 2L, unknownPlaceType);
		// then
		assertThat(journeyTravelInfo).isEmpty();
	}

	@Test
	@PrepareForTest(PlaceType.class)
	public void testLoadTravelInfoOfDepartmentToUnknownPlaceType() throws Exception {

		PlaceType unknownPlaceType = PowerMockito.mock(PlaceType.class);
		PowerMockito.when(unknownPlaceType.ordinal()).thenReturn(4);
		PowerMockito.mockStatic(PlaceType.class);

		PlaceType[] placeTypes = new PlaceType[] { PlaceType.LOCALITY, PlaceType.DELEGATION, PlaceType.DEPARTMENT,
				PlaceType.GEO_PLACE, unknownPlaceType };
		PowerMockito.when(PlaceType.values()).thenAnswer((Answer<PlaceType[]>) invocation -> placeTypes);
		// when
		Optional<JourneyTravelInfo> journeyTravelInfo = journeyTravelInfoPersistenceAdapter.loadTravelInfo(1L,
				PlaceType.DEPARTMENT, 2L, unknownPlaceType);
		// then
		assertThat(journeyTravelInfo).isEmpty();
	}

	@Test
	@PrepareForTest(PlaceType.class)
	public void testLoadTravelInfoOfGeoPlaceToUnknownPlaceType() throws Exception {

		PlaceType unknownPlaceType = PowerMockito.mock(PlaceType.class);
		PowerMockito.when(unknownPlaceType.ordinal()).thenReturn(4);
		PowerMockito.mockStatic(PlaceType.class);

		PlaceType[] placeTypes = new PlaceType[] { PlaceType.LOCALITY, PlaceType.DELEGATION, PlaceType.DEPARTMENT,
				PlaceType.GEO_PLACE, unknownPlaceType };
		PowerMockito.when(PlaceType.values()).thenAnswer((Answer<PlaceType[]>) invocation -> placeTypes);
		// when
		Optional<JourneyTravelInfo> journeyTravelInfo = journeyTravelInfoPersistenceAdapter.loadTravelInfo(1L,
				PlaceType.GEO_PLACE, 2L, unknownPlaceType);
		// then
		assertThat(journeyTravelInfo).isEmpty();
	}

	@Test
	@PrepareForTest(PlaceType.class)
	public void testLoadTravelInfoOfUnknownPlaceTypeToUnknownPlaceType() throws Exception {

		PlaceType unknownPlaceType = PowerMockito.mock(PlaceType.class);
		PowerMockito.when(unknownPlaceType.ordinal()).thenReturn(4);
		PowerMockito.mockStatic(PlaceType.class);

		PlaceType[] placeTypes = new PlaceType[] { PlaceType.LOCALITY, PlaceType.DELEGATION, PlaceType.DEPARTMENT,
				PlaceType.GEO_PLACE, unknownPlaceType };
		PowerMockito.when(PlaceType.values()).thenAnswer((Answer<PlaceType[]>) invocation -> placeTypes);
		// when
		Optional<JourneyTravelInfo> journeyTravelInfo = journeyTravelInfoPersistenceAdapter.loadTravelInfo(1L,
				unknownPlaceType, 2L, unknownPlaceType);
		// then
		assertThat(journeyTravelInfo).isEmpty();
	}

	@Test
	@PrepareForTest(PlaceType.class)
	public void testCreateTravelInfoOfLocalityToUnknownPlaceType() throws Exception {

		PlaceType unknownPlaceType = PowerMockito.mock(PlaceType.class);
		PowerMockito.when(unknownPlaceType.ordinal()).thenReturn(4);
		PowerMockito.mockStatic(PlaceType.class);

		PlaceType[] placeTypes = new PlaceType[] { PlaceType.LOCALITY, PlaceType.DELEGATION, PlaceType.DEPARTMENT,
				PlaceType.GEO_PLACE, unknownPlaceType };
		PowerMockito.when(PlaceType.values()).thenAnswer((Answer<PlaceType[]>) invocation -> placeTypes);

		JourneyTravelInfo journeyTravelInfo = defaultJourneyTravelInfo();
		LocalityJpaEntity locality = defaultExistentLocalityJpaEntity();
		given(localityRepository.findById(any(Long.class))).willReturn(Optional.of(locality));
		// when
		journeyTravelInfoPersistenceAdapter.createTravelInfo(1L, PlaceType.LOCALITY, 2L, unknownPlaceType,
				journeyTravelInfo);
		// then
		then(localityToLocalityTravelInfoRepository).should(never())
				.save(any(LocalityToLocalityTravelInfoJpaEntity.class));
		then(localityToDelegationTravelInfoRepository).should(never())
				.save(any(LocalityToDelegationTravelInfoJpaEntity.class));
		then(localityToDepartmentTravelInfoRepository).should(never())
				.save(any(LocalityToDepartmentTravelInfoJpaEntity.class));
		then(geoPlaceToLocalityTravelInfoRepository).should(never())
				.save(any(GeoPlaceToLocalityTravelInfoJpaEntity.class));

	}

	@Test
	@PrepareForTest(PlaceType.class)
	public void testCreateTravelInfoOfDelegationToUnknownPlaceType() throws Exception {

		PlaceType unknownPlaceType = PowerMockito.mock(PlaceType.class);
		PowerMockito.when(unknownPlaceType.ordinal()).thenReturn(4);
		PowerMockito.mockStatic(PlaceType.class);

		PlaceType[] placeTypes = new PlaceType[] { PlaceType.LOCALITY, PlaceType.DELEGATION, PlaceType.DEPARTMENT,
				PlaceType.GEO_PLACE, unknownPlaceType };
		PowerMockito.when(PlaceType.values()).thenAnswer((Answer<PlaceType[]>) invocation -> placeTypes);

		JourneyTravelInfo journeyTravelInfo = defaultJourneyTravelInfo();
		DelegationJpaEntity delegation = defaultExistentDelegationJpaEntity();
		given(delegationRepository.findById(any(Long.class))).willReturn(Optional.of(delegation));
		// when
		journeyTravelInfoPersistenceAdapter.createTravelInfo(1L, PlaceType.DELEGATION, 2L, unknownPlaceType,
				journeyTravelInfo);
		// then
		then(delegationToDelegationTravelInfoRepository).should(never())
				.save(any(DelegationToDelegationTravelInfoJpaEntity.class));
		then(localityToDelegationTravelInfoRepository).should(never())
				.save(any(LocalityToDelegationTravelInfoJpaEntity.class));
		then(delegationToDepartmentTravelInfoRepository).should(never())
				.save(any(DelegationToDepartmentTravelInfoJpaEntity.class));
		then(geoPlaceToDelegationTravelInfoRepository).should(never())
				.save(any(GeoPlaceToDelegationTravelInfoJpaEntity.class));

	}

	@Test
	@PrepareForTest(PlaceType.class)
	public void testCreateTravelInfoOfDepartmentToUnknownPlaceType() throws Exception {

		PlaceType unknownPlaceType = PowerMockito.mock(PlaceType.class);
		PowerMockito.when(unknownPlaceType.ordinal()).thenReturn(4);
		PowerMockito.mockStatic(PlaceType.class);

		PlaceType[] placeTypes = new PlaceType[] { PlaceType.LOCALITY, PlaceType.DELEGATION, PlaceType.DEPARTMENT,
				PlaceType.GEO_PLACE, unknownPlaceType };
		PowerMockito.when(PlaceType.values()).thenAnswer((Answer<PlaceType[]>) invocation -> placeTypes);

		JourneyTravelInfo journeyTravelInfo = defaultJourneyTravelInfo();
		DepartmentJpaEntity department = defaultExistentDepartmentJpaEntity();
		given(departmentRepository.findById(any(Long.class))).willReturn(Optional.of(department));
		// when
		journeyTravelInfoPersistenceAdapter.createTravelInfo(1L, PlaceType.DEPARTMENT, 2L, unknownPlaceType,
				journeyTravelInfo);
		// then
		then(departmentToDepartmentTravelInfoRepository).should(never())
				.save(any(DepartmentToDepartmentTravelInfoJpaEntity.class));
		then(localityToDepartmentTravelInfoRepository).should(never())
				.save(any(LocalityToDepartmentTravelInfoJpaEntity.class));
		then(delegationToDepartmentTravelInfoRepository).should(never())
				.save(any(DelegationToDepartmentTravelInfoJpaEntity.class));
		then(geoPlaceToDepartmentTravelInfoRepository).should(never())
				.save(any(GeoPlaceToDepartmentTravelInfoJpaEntity.class));

	}

	@Test
	@PrepareForTest(PlaceType.class)
	public void testCreateTravelInfoOfGeoPlaceToUnknownPlaceType() throws Exception {

		PlaceType unknownPlaceType = PowerMockito.mock(PlaceType.class);
		PowerMockito.when(unknownPlaceType.ordinal()).thenReturn(4);
		PowerMockito.mockStatic(PlaceType.class);

		PlaceType[] placeTypes = new PlaceType[] { PlaceType.LOCALITY, PlaceType.DELEGATION, PlaceType.DEPARTMENT,
				PlaceType.GEO_PLACE, unknownPlaceType };
		PowerMockito.when(PlaceType.values()).thenAnswer((Answer<PlaceType[]>) invocation -> placeTypes);

		JourneyTravelInfo journeyTravelInfo = defaultJourneyTravelInfo();
		GeoPlaceJpaEntity geoPlace = defaultGeoPlaceJpaEntity();
		given(geoPlaceRepository.findById(any(Long.class))).willReturn(Optional.of(geoPlace));
		// when
		journeyTravelInfoPersistenceAdapter.createTravelInfo(1L, PlaceType.GEO_PLACE, 2L, unknownPlaceType,
				journeyTravelInfo);
		// then

		then(geoPlaceToDelegationTravelInfoRepository).should(never())
				.save(any(GeoPlaceToDelegationTravelInfoJpaEntity.class));
		then(geoPlaceToDepartmentTravelInfoRepository).should(never())
				.save(any(GeoPlaceToDepartmentTravelInfoJpaEntity.class));
		then(geoPlaceToLocalityTravelInfoRepository).should(never())
				.save(any(GeoPlaceToLocalityTravelInfoJpaEntity.class));

	}

	@Test
	@PrepareForTest(PlaceType.class)
	public void testCreateTravelInfoOfUnknownPlaceTypeToUnknownPlaceType() throws Exception {

		PlaceType unknownPlaceType = PowerMockito.mock(PlaceType.class);
		PowerMockito.when(unknownPlaceType.ordinal()).thenReturn(4);
		PowerMockito.mockStatic(PlaceType.class);

		PlaceType[] placeTypes = new PlaceType[] { PlaceType.LOCALITY, PlaceType.DELEGATION, PlaceType.DEPARTMENT,
				PlaceType.GEO_PLACE, unknownPlaceType };
		PowerMockito.when(PlaceType.values()).thenAnswer((Answer<PlaceType[]>) invocation -> placeTypes);

		JourneyTravelInfo journeyTravelInfo = defaultJourneyTravelInfo();

		// when
		journeyTravelInfoPersistenceAdapter.createTravelInfo(1L, unknownPlaceType, 2L, unknownPlaceType,
				journeyTravelInfo);
		// then
		then(departmentToDepartmentTravelInfoRepository).should(never())
				.save(any(DepartmentToDepartmentTravelInfoJpaEntity.class));
		then(localityToDepartmentTravelInfoRepository).should(never())
				.save(any(LocalityToDepartmentTravelInfoJpaEntity.class));
		then(delegationToDepartmentTravelInfoRepository).should(never())
				.save(any(DelegationToDepartmentTravelInfoJpaEntity.class));
		then(geoPlaceToDepartmentTravelInfoRepository).should(never())
				.save(any(GeoPlaceToDepartmentTravelInfoJpaEntity.class));

		then(delegationToDelegationTravelInfoRepository).should(never())
				.save(any(DelegationToDelegationTravelInfoJpaEntity.class));
		then(localityToDelegationTravelInfoRepository).should(never())
				.save(any(LocalityToDelegationTravelInfoJpaEntity.class));

		then(geoPlaceToDelegationTravelInfoRepository).should(never())
				.save(any(GeoPlaceToDelegationTravelInfoJpaEntity.class));

		then(localityToLocalityTravelInfoRepository).should(never())
				.save(any(LocalityToLocalityTravelInfoJpaEntity.class));

		then(geoPlaceToLocalityTravelInfoRepository).should(never())
				.save(any(GeoPlaceToLocalityTravelInfoJpaEntity.class));

	}

}
