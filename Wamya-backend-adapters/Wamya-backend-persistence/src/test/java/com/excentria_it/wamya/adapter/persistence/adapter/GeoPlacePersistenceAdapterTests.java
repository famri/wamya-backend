package com.excentria_it.wamya.adapter.persistence.adapter;

import static com.excentria_it.wamya.test.data.common.DepartmentJpaTestData.*;
import static com.excentria_it.wamya.test.data.common.GeoPlaceJpaTestData.*;
import static com.excentria_it.wamya.test.data.common.GeoPlaceTestData.*;
import static com.excentria_it.wamya.test.data.common.UserAccountJpaEntityTestData.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import com.excentria_it.wamya.adapter.persistence.entity.ClientJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.DepartmentJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.GeoPlaceJpaEntity;
import com.excentria_it.wamya.adapter.persistence.mapper.GeoPlaceMapper;
import com.excentria_it.wamya.adapter.persistence.repository.ClientRepository;
import com.excentria_it.wamya.adapter.persistence.repository.DepartmentRepository;
import com.excentria_it.wamya.adapter.persistence.repository.GeoPlaceRepository;
import com.excentria_it.wamya.common.exception.DepartmentNotFoundException;
import com.excentria_it.wamya.common.exception.UserAccountNotFoundException;
import com.excentria_it.wamya.domain.GeoPlaceDto;
import com.excentria_it.wamya.test.data.common.TestConstants;

@ExtendWith(MockitoExtension.class)
public class GeoPlacePersistenceAdapterTests {
	@Mock
	private GeoPlaceRepository geoPlaceRepository;
	@Mock
	private DepartmentRepository departmentRepository;
	@Mock
	private ClientRepository clientRepository;
	@Mock
	private GeoPlaceMapper geoPlaceMapper;
	@InjectMocks
	private GeoPlacePersistenceAdapter geoPlacePersistenceAdapter;

	@Test
	void testLoadFavoriteGeoPlaceWithEmailUsername() {
		// given
		GeoPlaceJpaEntity geoPlace = defaultGeoPlaceJpaEntity();
		GeoPlaceDto geoPlaceDto = defaultGeoPlaceDto();
		given(geoPlaceRepository.findByClient_Email(any(String.class), any(Sort.class))).willReturn(List.of(geoPlace));
		List<GeoPlaceDto> expectedResponse = List.of(geoPlaceDto);
		given(geoPlaceMapper.mapToDomainEntities(any(List.class), any(String.class))).willReturn(expectedResponse);
		// When
		List<GeoPlaceDto> geoPlaces = geoPlacePersistenceAdapter.loadFavoriteGeoPlaces(TestConstants.DEFAULT_EMAIL,
				"fr_FR");
		// then
		assertEquals(expectedResponse, geoPlaces);

	}

	@Test
	void testLoadFavoriteGeoPlaceWithMobileNumberUsername() {
		// given
		GeoPlaceJpaEntity geoPlace = defaultGeoPlaceJpaEntity();
		GeoPlaceDto geoPlaceDto = defaultGeoPlaceDto();
		given(geoPlaceRepository.findByClient_Icc_ValueAndClient_MobileNumber(any(String.class), any(String.class),
				any(Sort.class))).willReturn(List.of(geoPlace));
		List<GeoPlaceDto> expectedResponse = List.of(geoPlaceDto);
		given(geoPlaceMapper.mapToDomainEntities(any(List.class), any(String.class))).willReturn(expectedResponse);
		// When

		List<GeoPlaceDto> geoPlaces = geoPlacePersistenceAdapter
				.loadFavoriteGeoPlaces(TestConstants.DEFAULT_MOBILE_NUMBER_USERNAME, "fr_FR");
		// then
		assertEquals(expectedResponse, geoPlaces);

	}

	@Test
	void testLoadFavoriteGeoPlaceWithBadUsername() {
		// given

		// When
		List<GeoPlaceDto> geoPlaces = geoPlacePersistenceAdapter.loadFavoriteGeoPlaces("THIS IS A BAD USERNAME",
				"fr_FR");
		// then
		assertThat(geoPlaces).isEmpty();

	}

	@Test
	void testCreateFavoriteGeoPlaceWithEmailUserName() {
		// given
		DepartmentJpaEntity department = defaultExistentDepartmentJpaEntity();
		given(departmentRepository.findById(any(Long.class))).willReturn(Optional.of(department));

		ClientJpaEntity client = defaultExistentClientJpaEntity();
		given(clientRepository.findClientByEmail(any(String.class))).willReturn(Optional.of(client));

		GeoPlaceJpaEntity geoPlace = defaultGeoPlaceJpaEntity();
		given(geoPlaceRepository.save(any(GeoPlaceJpaEntity.class))).willReturn(geoPlace);

		GeoPlaceDto expectedGeoPlaceDto = defaultGeoPlaceDto();

		given(geoPlaceMapper.mapToDomainEntity(any(GeoPlaceJpaEntity.class), any(String.class)))
				.willReturn(expectedGeoPlaceDto);
		// when
		GeoPlaceDto geoPlaceDto = geoPlacePersistenceAdapter.createFavoriteGeoPlace("My Favorite Place",
				new BigDecimal(36.8465752255), new BigDecimal(10.8465752255), 1L, TestConstants.DEFAULT_EMAIL, "fr_FR");
		// then

		assertEquals(expectedGeoPlaceDto, geoPlaceDto);
	}

	@Test
	void testCreateFavoriteGeoPlaceWithMobileNumberUserName() {
		// given
		DepartmentJpaEntity department = defaultExistentDepartmentJpaEntity();
		given(departmentRepository.findById(any(Long.class))).willReturn(Optional.of(department));

		ClientJpaEntity client = defaultExistentClientJpaEntity();
		given(clientRepository.findClientByIcc_ValueAndMobileNumber(any(String.class), any(String.class)))
				.willReturn(Optional.of(client));

		GeoPlaceJpaEntity geoPlace = defaultGeoPlaceJpaEntity();
		given(geoPlaceRepository.save(any(GeoPlaceJpaEntity.class))).willReturn(geoPlace);

		GeoPlaceDto expectedGeoPlaceDto = defaultGeoPlaceDto();

		given(geoPlaceMapper.mapToDomainEntity(any(GeoPlaceJpaEntity.class), any(String.class)))
				.willReturn(expectedGeoPlaceDto);
		// when
		GeoPlaceDto geoPlaceDto = geoPlacePersistenceAdapter.createFavoriteGeoPlace("My Favorite Place",
				new BigDecimal(36.8465752255), new BigDecimal(10.8465752255), 1L,
				TestConstants.DEFAULT_MOBILE_NUMBER_USERNAME, "fr_FR");
		// then

		assertEquals(expectedGeoPlaceDto, geoPlaceDto);
	}

	@Test
	void testCreateFavoriteGeoPlaceWithEmptyDepartment() {
		// given
		given(departmentRepository.findById(any(Long.class))).willReturn(Optional.empty());

		// when // then
		assertThrows(DepartmentNotFoundException.class,
				() -> geoPlacePersistenceAdapter.createFavoriteGeoPlace("My Favorite Place",
						new BigDecimal(36.8465752255), new BigDecimal(10.8465752255), 1L, TestConstants.DEFAULT_EMAIL,
						"fr_FR"));

	}

	@Test
	void testCreateFavoriteGeoPlaceWithBadUserName() {
		// given
		DepartmentJpaEntity department = defaultExistentDepartmentJpaEntity();
		given(departmentRepository.findById(any(Long.class))).willReturn(Optional.of(department));

		// when // then
		assertThrows(UserAccountNotFoundException.class,
				() -> geoPlacePersistenceAdapter.createFavoriteGeoPlace("My Favorite Place",
						new BigDecimal(36.8465752255), new BigDecimal(10.8465752255), 1L, "THIS IS A BAD USERNAME",
						"fr_FR"));
	}

	@Test
	void testCreateFavoriteGeoPlaceWithEmptyClient() {
		// given
		DepartmentJpaEntity department = defaultExistentDepartmentJpaEntity();
		given(departmentRepository.findById(any(Long.class))).willReturn(Optional.of(department));

		given(clientRepository.findClientByEmail(any(String.class))).willReturn(Optional.empty());

		// when
		assertThrows(UserAccountNotFoundException.class,
				() -> geoPlacePersistenceAdapter.createFavoriteGeoPlace("My Favorite Place",
						new BigDecimal(36.8465752255), new BigDecimal(10.8465752255), 1L, TestConstants.DEFAULT_EMAIL,
						"fr_FR"));

	}

}
