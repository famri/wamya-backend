package com.excentria_it.wamya.adapter.persistence.adapter;

import static com.excentria_it.wamya.test.data.common.DepartmentJpaTestData.*;
import static com.excentria_it.wamya.test.data.common.JourneyRequestJpaTestData.*;
import static com.excentria_it.wamya.test.data.common.JourneyRequestTestData.*;
import static com.excentria_it.wamya.test.data.common.UserAccountJpaEntityTestData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

import com.excentria_it.wamya.adapter.persistence.entity.ClientJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.DepartmentJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.EngineTypeJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.JourneyRequestJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.JourneyRequestStatusJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.PlaceId;
import com.excentria_it.wamya.adapter.persistence.entity.PlaceJpaEntity;
import com.excentria_it.wamya.adapter.persistence.mapper.JourneyRequestMapper;
import com.excentria_it.wamya.adapter.persistence.mapper.PlaceMapper;
import com.excentria_it.wamya.adapter.persistence.repository.ClientRepository;
import com.excentria_it.wamya.adapter.persistence.repository.EngineTypeRepository;
import com.excentria_it.wamya.adapter.persistence.repository.JourneyRequestRepository;
import com.excentria_it.wamya.adapter.persistence.repository.JourneyRequestStatusRepository;
import com.excentria_it.wamya.adapter.persistence.repository.PlaceRepository;
import com.excentria_it.wamya.adapter.persistence.utils.DepartmentJpaEntityResolver;
import com.excentria_it.wamya.adapter.persistence.utils.LocalizedPlaceJpaEntityResolver;
import com.excentria_it.wamya.common.SortCriterion;
import com.excentria_it.wamya.domain.ClientJourneyRequestDtoOutput;
import com.excentria_it.wamya.domain.ClientJourneyRequestsOutput;
import com.excentria_it.wamya.domain.JourneyRequestInputOutput;
import com.excentria_it.wamya.domain.JourneyRequestSearchDto;
import com.excentria_it.wamya.domain.JourneyRequestSearchOutput;
import com.excentria_it.wamya.domain.JourneyRequestStatusCode;
import com.excentria_it.wamya.domain.JourneyRequestsSearchOutputResult;
import com.excentria_it.wamya.domain.LoadClientJourneyRequestsCriteria;
import com.excentria_it.wamya.domain.SearchJourneyRequestsInput;
import com.excentria_it.wamya.test.data.common.DepartmentJpaTestData;
import com.excentria_it.wamya.test.data.common.EngineTypeJpaTestData;
import com.excentria_it.wamya.test.data.common.JourneyRequestJpaTestData;
import com.excentria_it.wamya.test.data.common.JourneyRequestTestData;
import com.excentria_it.wamya.test.data.common.PlaceJpaTestData;
import com.excentria_it.wamya.test.data.common.TestConstants;

@ExtendWith(MockitoExtension.class)
public class JourneyRequestsPersistenceAdapterTests {
	@Mock
	private JourneyRequestRepository journeyRequestRepository;
	@Mock
	private EngineTypeRepository engineTypeRepository;
	@Mock
	private ClientRepository clientRepository;
	@Mock
	private PlaceRepository placeRepository;
	@Mock
	private JourneyRequestMapper journeyRequestMapper;
	@Mock
	private JourneyRequestStatusRepository journeyRequestStatusRepository;
	@Mock
	private PlaceMapper placeMapper;
	@Mock
	private DepartmentJpaEntityResolver departmentResolver;
	@Mock
	private LocalizedPlaceJpaEntityResolver localizedPlaceResolver;

	@InjectMocks
	private JourneyRequestsPersistenceAdapter journeyRequestsPersistenceAdapter;

	@Test
	void givenNotNullJourneyRequestsPage_WhenSearchJourneyRequests_ThenReturnJourneyRequestsSearchResult() {

		// given
		SearchJourneyRequestsInput command = defaultSearchJourneyRequestsInputBuilder().build();

		Page<JourneyRequestSearchOutput> expectedResult = givenNotNullJourneyRequestsPageByDeparturePlace_RegionIdAndArrivalPlace_RegionIdInAndEngineType_IdInAndDateBetween();

		// when

		JourneyRequestsSearchOutputResult result = journeyRequestsPersistenceAdapter.searchJourneyRequests(command);
		// then

		assertEquals(expectedResult.getTotalPages(), result.getTotalPages());
		assertEquals(expectedResult.getTotalElements(), result.getTotalElements());
		assertEquals(expectedResult.getNumber(), result.getPageNumber());
		assertEquals(expectedResult.getSize(), result.getPageSize());
		assertEquals(expectedResult.hasNext(), result.isHasNext());
		assertEquals(expectedResult.getContent(), result.getContent());

	}

	@Test
	void givenNotNullJourneyRequestsPage_WhenSearchJourneyRequestsByAnyArrivalPlace_ThenReturnJourneyRequestsSearchResult() {

		// given
		SearchJourneyRequestsInput command = defaultSearchJourneyRequestsInputBuilder().build();

		command.setArrivalPlaceDepartmentIds(Set.of(-1L));

		Page<JourneyRequestSearchOutput> expectedResult = givenNotNullJourneyRequestsPageByDeparturePlace_RegionIdAndEngineType_IdInAndDateBetween();

		// when

		JourneyRequestsSearchOutputResult result = journeyRequestsPersistenceAdapter.searchJourneyRequests(command);
		// then

		assertEquals(expectedResult.getTotalPages(), result.getTotalPages());
		assertEquals(expectedResult.getTotalElements(), result.getTotalElements());
		assertEquals(expectedResult.getNumber(), result.getPageNumber());
		assertEquals(expectedResult.getSize(), result.getPageSize());
		assertEquals(expectedResult.hasNext(), result.isHasNext());
		assertEquals(expectedResult.getContent(), result.getContent());

	}

	@Test
	void givenExistentDeparturePlaceAndExistentArrivalPlace_WhenCreateJourneyRequest_ThenSaveJourneyRequestJpaEntity() {

		// given
		JourneyRequestInputOutput journeyRequest = defaultJourneyRequestInputOutputBuilder().build();

		EngineTypeJpaEntity engineTypeJpaEntity = defaultEngineTypeJpaEntity();
		given(engineTypeRepository.findById(journeyRequest.getEngineType().getId()))
				.willReturn(Optional.of(engineTypeJpaEntity));

		ClientJpaEntity clientJpaEntity = defaultExistentClientJpaEntity();
		given(clientRepository.findByEmail(TestConstants.DEFAULT_EMAIL)).willReturn(Optional.of(clientJpaEntity));

		PlaceJpaEntity departurePlaceJpaEntity = defaultDeparturePlaceJpaEntity();
		given(placeRepository.findById(
				new PlaceId(journeyRequest.getDeparturePlace().getId(), journeyRequest.getDeparturePlace().getType())))
						.willReturn(Optional.of(departurePlaceJpaEntity));

		PlaceJpaEntity arrivalPlaceJpaEntity = defaultArrivalPlaceJpaEntity();
		given(placeRepository.findById(
				new PlaceId(journeyRequest.getArrivalPlace().getId(), journeyRequest.getArrivalPlace().getType())))
						.willReturn(Optional.of(arrivalPlaceJpaEntity));

		JourneyRequestJpaEntity journeyRequestJpaEntity = defaultExistentJourneyRequestJpaEntity();
		given(journeyRequestMapper.mapToJpaEntity(journeyRequest, departurePlaceJpaEntity, arrivalPlaceJpaEntity,
				engineTypeJpaEntity, clientJpaEntity)).willReturn(journeyRequestJpaEntity);

		given(journeyRequestRepository.save(journeyRequestJpaEntity)).willReturn(journeyRequestJpaEntity);

		JourneyRequestStatusJpaEntity journeyRequestStatusJpaEntity = defaultJourneyRequestStatusJpaEntityBuilder()
				.build();
		given(journeyRequestStatusRepository.findByCode(JourneyRequestStatusCode.OPENED))
				.willReturn(journeyRequestStatusJpaEntity);
		DepartmentJpaEntity departureDep = defaultExistentDepartureDepartmentJpaEntity();
		DepartmentJpaEntity arrivalDep = defaultExistentArrivalDepartmentJpaEntity();
		given(departmentResolver.resolveDepartment(eq(journeyRequest.getDeparturePlace().getId()),
				eq(journeyRequest.getDeparturePlace().getType()))).willReturn(Optional.of(departureDep));
		given(departmentResolver.resolveDepartment(eq(journeyRequest.getArrivalPlace().getId()),
				eq(journeyRequest.getArrivalPlace().getType()))).willReturn(Optional.of(arrivalDep));
		// when
		journeyRequestsPersistenceAdapter.createJourneyRequest(journeyRequest, TestConstants.DEFAULT_EMAIL, "en_US");

		// then
		then(journeyRequestRepository).should(times(1)).save(journeyRequestJpaEntity);
	}

	@Test
	void givenInexistentDeparturePlaceAndInexistentArrivalPlace_WhenCreateJourneyRequest_ThenCreateDepartureAndArrivalPlacesAndSaveJourneyRequestJpaEntity() {

		// given
		JourneyRequestInputOutput journeyRequest = defaultJourneyRequestInputOutputBuilder().build();

		EngineTypeJpaEntity engineTypeJpaEntity = defaultEngineTypeJpaEntity();
		given(engineTypeRepository.findById(journeyRequest.getEngineType().getId()))
				.willReturn(Optional.of(engineTypeJpaEntity));

		ClientJpaEntity clientJpaEntity = defaultExistentClientJpaEntity();
		given(clientRepository.findByEmail(TestConstants.DEFAULT_EMAIL)).willReturn(Optional.of(clientJpaEntity));

		given(placeRepository.findById(
				new PlaceId(journeyRequest.getDeparturePlace().getId(), journeyRequest.getDeparturePlace().getType())))
						.willReturn(Optional.empty());

		given(placeRepository.findById(
				new PlaceId(journeyRequest.getArrivalPlace().getId(), journeyRequest.getArrivalPlace().getType())))
						.willReturn(Optional.empty());

		PlaceJpaEntity departurePlaceJpaEntity = defaultDeparturePlaceJpaEntity();
		PlaceJpaEntity arrivalPlaceJpaEntity = defaultArrivalPlaceJpaEntity();

		given(placeMapper.mapToJpaEntity(eq(journeyRequest.getDeparturePlace()),
				eq(departurePlaceJpaEntity.getDepartment()))).willReturn(departurePlaceJpaEntity);

		given(placeMapper.mapToJpaEntity(eq(journeyRequest.getArrivalPlace()),
				eq(arrivalPlaceJpaEntity.getDepartment()))).willReturn(arrivalPlaceJpaEntity);

		given(localizedPlaceResolver.resolveLocalizedPlaces(eq(journeyRequest.getDeparturePlace().getId()),
				eq(journeyRequest.getDeparturePlace().getType())))
						.willReturn(new ArrayList<>(departurePlaceJpaEntity.getLocalizations().values()));

		given(localizedPlaceResolver.resolveLocalizedPlaces(eq(journeyRequest.getArrivalPlace().getId()),
				eq(journeyRequest.getArrivalPlace().getType())))
						.willReturn(new ArrayList<>(arrivalPlaceJpaEntity.getLocalizations().values()));

		given(placeRepository.save(eq(departurePlaceJpaEntity))).willReturn(departurePlaceJpaEntity);
		given(placeRepository.save(eq(arrivalPlaceJpaEntity))).willReturn(arrivalPlaceJpaEntity);

		JourneyRequestJpaEntity journeyRequestJpaEntity = defaultExistentJourneyRequestJpaEntity();
		given(journeyRequestMapper.mapToJpaEntity(journeyRequest, departurePlaceJpaEntity, arrivalPlaceJpaEntity,
				engineTypeJpaEntity, clientJpaEntity)).willReturn(journeyRequestJpaEntity);

		given(journeyRequestRepository.save(journeyRequestJpaEntity)).willReturn(journeyRequestJpaEntity);

		JourneyRequestStatusJpaEntity journeyRequestStatusJpaEntity = defaultJourneyRequestStatusJpaEntityBuilder()
				.build();
		given(journeyRequestStatusRepository.findByCode(JourneyRequestStatusCode.OPENED))
				.willReturn(journeyRequestStatusJpaEntity);
		DepartmentJpaEntity departureDep = defaultExistentDepartureDepartmentJpaEntity();
		DepartmentJpaEntity arrivalDep = defaultExistentArrivalDepartmentJpaEntity();
		given(departmentResolver.resolveDepartment(eq(journeyRequest.getDeparturePlace().getId()),
				eq(journeyRequest.getDeparturePlace().getType()))).willReturn(Optional.of(departureDep));
		given(departmentResolver.resolveDepartment(eq(journeyRequest.getArrivalPlace().getId()),
				eq(journeyRequest.getArrivalPlace().getType()))).willReturn(Optional.of(arrivalDep));
		// when
		journeyRequestsPersistenceAdapter.createJourneyRequest(journeyRequest, TestConstants.DEFAULT_EMAIL, "en_US");

		// then
		then(placeRepository).should(times(1)).save(departurePlaceJpaEntity);
		then(placeRepository).should(times(1)).save(arrivalPlaceJpaEntity);
		then(journeyRequestRepository).should(times(1)).save(journeyRequestJpaEntity);
	}

	@Test
	void givenEmptyDepartureDepartment_WhenCreateJourneyRequest_ThenReturnNull() {
		// given
		JourneyRequestInputOutput journeyRequest = defaultJourneyRequestInputOutputBuilder().build();

		given(departmentResolver.resolveDepartment(eq(journeyRequest.getDeparturePlace().getId()),
				eq(journeyRequest.getDeparturePlace().getType()))).willReturn(Optional.empty());
		// when
		JourneyRequestInputOutput result = journeyRequestsPersistenceAdapter.createJourneyRequest(journeyRequest,
				TestConstants.DEFAULT_EMAIL, "en_US");
		// then

		assertNull(result);
	}

	@Test
	void givenEmptyArrivalDepartment_WhenCreateJourneyRequest_ThenReturnNull() {
		// given
		JourneyRequestInputOutput journeyRequest = defaultJourneyRequestInputOutputBuilder().build();

		DepartmentJpaEntity departureDep = defaultExistentDepartureDepartmentJpaEntity();

		given(departmentResolver.resolveDepartment(eq(journeyRequest.getDeparturePlace().getId()),
				eq(journeyRequest.getDeparturePlace().getType()))).willReturn(Optional.of(departureDep));
		given(departmentResolver.resolveDepartment(eq(journeyRequest.getArrivalPlace().getId()),
				eq(journeyRequest.getArrivalPlace().getType()))).willReturn(Optional.empty());
		// when
		JourneyRequestInputOutput result = journeyRequestsPersistenceAdapter.createJourneyRequest(journeyRequest,
				TestConstants.DEFAULT_EMAIL, "en_US");
		// then

		assertNull(result);
	}

	@Test
	void givenEmptyEngineType_WhenCreateJourneyRequest_ThenReturnNull() {
		// given
		JourneyRequestInputOutput journeyRequest = defaultJourneyRequestInputOutputBuilder().build();

		DepartmentJpaEntity departureDep = defaultExistentDepartureDepartmentJpaEntity();
		DepartmentJpaEntity arrivalDep = defaultExistentArrivalDepartmentJpaEntity();

		given(departmentResolver.resolveDepartment(eq(journeyRequest.getDeparturePlace().getId()),
				eq(journeyRequest.getDeparturePlace().getType()))).willReturn(Optional.of(departureDep));
		given(departmentResolver.resolveDepartment(eq(journeyRequest.getArrivalPlace().getId()),
				eq(journeyRequest.getArrivalPlace().getType()))).willReturn(Optional.of(arrivalDep));
		given(engineTypeRepository.findById(eq(journeyRequest.getEngineType().getId()))).willReturn(Optional.empty());
		// when
		JourneyRequestInputOutput result = journeyRequestsPersistenceAdapter.createJourneyRequest(journeyRequest,
				TestConstants.DEFAULT_EMAIL, "en_US");
		// then

		assertNull(result);
	}

	@Test
	void givenEmptyClientAndUserMobileNumber_WhenCreateJourneyRequest_ThenReturnNull() {
		// given
		JourneyRequestInputOutput journeyRequest = defaultJourneyRequestInputOutputBuilder().build();

		DepartmentJpaEntity departureDep = defaultExistentDepartureDepartmentJpaEntity();
		DepartmentJpaEntity arrivalDep = defaultExistentArrivalDepartmentJpaEntity();
		EngineTypeJpaEntity engineTypeJpaEntity = defaultEngineTypeJpaEntity();

		given(departmentResolver.resolveDepartment(eq(journeyRequest.getDeparturePlace().getId()),
				eq(journeyRequest.getDeparturePlace().getType()))).willReturn(Optional.of(departureDep));
		given(departmentResolver.resolveDepartment(eq(journeyRequest.getArrivalPlace().getId()),
				eq(journeyRequest.getArrivalPlace().getType()))).willReturn(Optional.of(arrivalDep));
		given(engineTypeRepository.findById(journeyRequest.getEngineType().getId()))
				.willReturn(Optional.of(engineTypeJpaEntity));

		given(clientRepository.findByEmail(any(String.class))).willReturn(Optional.empty());
		given(clientRepository.findByIcc_ValueAndMobileNumber(any(String.class), any(String.class)))
				.willReturn(Optional.empty());
		// when
		JourneyRequestInputOutput result = journeyRequestsPersistenceAdapter.createJourneyRequest(journeyRequest,
				TestConstants.DEFAULT_MOBILE_NUMBER_USERNAME, "en_US");
		// then

		assertNull(result);
	}

	@Test
	void givenEmptyClientAndUserEmail_WhenCreateJourneyRequest_ThenReturnNull() {
		// given
		JourneyRequestInputOutput journeyRequest = defaultJourneyRequestInputOutputBuilder().build();
		DepartmentJpaEntity departureDep = defaultExistentDepartureDepartmentJpaEntity();
		DepartmentJpaEntity arrivalDep = defaultExistentArrivalDepartmentJpaEntity();
		EngineTypeJpaEntity engineTypeJpaEntity = defaultEngineTypeJpaEntity();

		given(departmentResolver.resolveDepartment(eq(journeyRequest.getDeparturePlace().getId()),
				eq(journeyRequest.getDeparturePlace().getType()))).willReturn(Optional.of(departureDep));
		given(departmentResolver.resolveDepartment(eq(journeyRequest.getArrivalPlace().getId()),
				eq(journeyRequest.getArrivalPlace().getType()))).willReturn(Optional.of(arrivalDep));
		given(engineTypeRepository.findById(journeyRequest.getEngineType().getId()))
				.willReturn(Optional.of(engineTypeJpaEntity));

		given(clientRepository.findByEmail(any(String.class))).willReturn(Optional.empty());

		// when
		JourneyRequestInputOutput result = journeyRequestsPersistenceAdapter.createJourneyRequest(journeyRequest,
				TestConstants.DEFAULT_EMAIL, "en_US");
		// then

		assertNull(result);
	}

	@Test
	void givenExistentUserAccountByPhoneNumberAndInexistentDepartureAndArrivalPlaces_WhenCreateJourneyRequest_ThenSaveJourneyRequestJpaEntity() {

		// given
		JourneyRequestInputOutput journeyRequest = defaultJourneyRequestInputOutputBuilder().build();

		DepartmentJpaEntity departureDep = defaultExistentDepartureDepartmentJpaEntity();
		DepartmentJpaEntity arrivalDep = defaultExistentArrivalDepartmentJpaEntity();

		EngineTypeJpaEntity engineTypeJpaEntity = defaultEngineTypeJpaEntity();
		given(engineTypeRepository.findById(journeyRequest.getEngineType().getId()))
				.willReturn(Optional.of(engineTypeJpaEntity));

		ClientJpaEntity clientJpaEntity = defaultExistentClientJpaEntity();
		given(clientRepository.findByEmail(TestConstants.DEFAULT_MOBILE_NUMBER_USERNAME))
				.willReturn(Optional.ofNullable(null));

		given(clientRepository.findByIcc_ValueAndMobileNumber(any(String.class), any(String.class)))
				.willReturn(Optional.of(clientJpaEntity));

		PlaceJpaEntity departurePlaceJpaEntity = defaultDeparturePlaceJpaEntity();
		given(placeRepository.findById(
				new PlaceId(journeyRequest.getDeparturePlace().getId(), journeyRequest.getDeparturePlace().getType())))
						.willReturn(Optional.ofNullable(null));
		given(placeMapper.mapToJpaEntity(journeyRequest.getDeparturePlace(), departureDep))
				.willReturn(departurePlaceJpaEntity);
		given(placeRepository.save(departurePlaceJpaEntity)).willReturn(departurePlaceJpaEntity);

		PlaceJpaEntity arrivalPlaceJpaEntity = defaultArrivalPlaceJpaEntity();
		given(placeRepository.findById(
				new PlaceId(journeyRequest.getArrivalPlace().getId(), journeyRequest.getArrivalPlace().getType())))
						.willReturn(Optional.ofNullable(null));

		given(placeMapper.mapToJpaEntity(journeyRequest.getArrivalPlace(), arrivalDep))
				.willReturn(arrivalPlaceJpaEntity);
		given(placeRepository.save(arrivalPlaceJpaEntity)).willReturn(arrivalPlaceJpaEntity);

		JourneyRequestJpaEntity journeyRequestJpaEntity = defaultExistentJourneyRequestJpaEntity();
		given(journeyRequestMapper.mapToJpaEntity(journeyRequest, departurePlaceJpaEntity, arrivalPlaceJpaEntity,
				engineTypeJpaEntity, clientJpaEntity)).willReturn(journeyRequestJpaEntity);

		given(journeyRequestRepository.save(journeyRequestJpaEntity)).willReturn(journeyRequestJpaEntity);
		JourneyRequestStatusJpaEntity journeyRequestStatusJpaEntity = defaultJourneyRequestStatusJpaEntityBuilder()
				.build();
		given(journeyRequestStatusRepository.findByCode(JourneyRequestStatusCode.OPENED))
				.willReturn(journeyRequestStatusJpaEntity);

		given(departmentResolver.resolveDepartment(eq(journeyRequest.getDeparturePlace().getId()),
				eq(journeyRequest.getDeparturePlace().getType()))).willReturn(Optional.of(departureDep));
		given(departmentResolver.resolveDepartment(eq(journeyRequest.getArrivalPlace().getId()),
				eq(journeyRequest.getArrivalPlace().getType()))).willReturn(Optional.of(arrivalDep));
		// when
		journeyRequestsPersistenceAdapter.createJourneyRequest(journeyRequest,
				TestConstants.DEFAULT_MOBILE_NUMBER_USERNAME, "en_US");

		// then
		then(journeyRequestRepository).should(times(1)).save(journeyRequestJpaEntity);
	}

	@Test
	void testLoadJourneyRequestById() {

		// given
		JourneyRequestJpaEntity journeyRequestJpaEntity = defaultExistentJourneyRequestJpaEntity();
		given(journeyRequestRepository.findById(any(Long.class))).willReturn(Optional.of(journeyRequestJpaEntity));

		JourneyRequestInputOutput createJourneyRequestDto = defaultJourneyRequestInputOutputBuilder().build();
		given(journeyRequestMapper.mapToDomainEntity(any(JourneyRequestJpaEntity.class), any(String.class)))
				.willReturn(createJourneyRequestDto);
		// when
		Optional<JourneyRequestInputOutput> createJourneyRequestDtoOptional = journeyRequestsPersistenceAdapter
				.loadJourneyRequestById(1L);
		// then

		assertEquals(createJourneyRequestDto, createJourneyRequestDtoOptional.get());

	}

	@Test
	void givenNotNullJourneyRequestsPageAnClientEmail_WhenLoadClientJourneyRequests_ThenReturnClientJourneyRequests() {

		// given

		Page<ClientJourneyRequestDtoOutput> page = createPageFromClientJourneyRequestDto(
				defaultClientJourneyRequestDtoList());
		given(journeyRequestRepository.findByCreationDateTimeBetweenAndClient_Email(any(Instant.class),
				any(Instant.class), any(String.class), any(String.class), any(Pageable.class))).willReturn(page);
		LoadClientJourneyRequestsCriteria criteria = defaultLoadClientJourneyRequestsCriteriaBuilder().build();
		// when
		ClientJourneyRequestsOutput result = journeyRequestsPersistenceAdapter.loadClientJourneyRequests(criteria);
		// then
		assertEquals(page.getTotalPages(), result.getTotalPages());
		assertEquals(page.hasNext(), result.isHasNext());
		assertEquals(page.getSize(), result.getPageSize());
		assertEquals(page.getTotalElements(), result.getTotalElements());
		assertEquals(page.getNumber(), result.getPageNumber());
		assertEquals(page.getContent(), result.getContent());
	}

	@Test
	void givenNullJourneyRequestsPageAndClientEmail_WhenLoadClientJourneyRequests_ThenReturnClientJourneyRequests() {

		// given

		given(journeyRequestRepository.findByCreationDateTimeBetweenAndClient_Email(any(Instant.class),
				any(Instant.class), any(String.class), any(String.class), any(Pageable.class))).willReturn(null);
		LoadClientJourneyRequestsCriteria criteria = defaultLoadClientJourneyRequestsCriteriaBuilder().build();
		// when
		ClientJourneyRequestsOutput result = journeyRequestsPersistenceAdapter.loadClientJourneyRequests(criteria);
		// then
		assertEquals(0, result.getTotalPages());
		assertEquals(criteria.getPageSize(), result.getPageSize());
		assertEquals(0, result.getTotalElements());
		assertEquals(criteria.getPageNumber(), result.getPageNumber());
		assertEquals(false, result.isHasNext());
		assertEquals(Collections.<ClientJourneyRequestDtoOutput>emptyList(), result.getContent());
	}

	@Test
	void testLoadJourneyRequestByIdAndClientEmail() {

		// given

		ClientJourneyRequestDtoOutput clientJourneyRequestDto = defaultClientJourneyRequestDtoOutput();
		given(journeyRequestRepository.findByIdAndClient_Email(eq(1L), eq(TestConstants.DEFAULT_EMAIL), eq("fr_FR")))
				.willReturn(Optional.of(clientJourneyRequestDto));
		// when
		Optional<ClientJourneyRequestDtoOutput> result = journeyRequestsPersistenceAdapter
				.loadJourneyRequestByIdAndClientEmail(1L, TestConstants.DEFAULT_EMAIL, "fr_FR");

		// then
		then(journeyRequestRepository).should(times(1)).findByIdAndClient_Email(eq(1L), eq(TestConstants.DEFAULT_EMAIL),
				eq("fr_FR"));
		assertEquals(clientJourneyRequestDto.getId(), result.get().getId());
	}

	@Test
	void testLoadInexistentJourneyRequestById() {

		// given

		given(journeyRequestRepository.findById(any(Long.class))).willReturn(Optional.ofNullable(null));

		// when
		Optional<JourneyRequestInputOutput> createJourneyRequestDtoOptional = journeyRequestsPersistenceAdapter
				.loadJourneyRequestById(1L);
		// then

		assertTrue(createJourneyRequestDtoOptional.isEmpty());

	}

	@Test
	void testTrueIsExistentJourneyRequestByIdAndClientEmail() {
		// given
		given(journeyRequestRepository.existsAndNotExpiredByIdAndClient_Email(any(Long.class), any(String.class)))
				.willReturn(true);
		// when
		boolean result = journeyRequestsPersistenceAdapter.isExistentAndNotExpiredJourneyRequestByIdAndClientEmail(1L,
				TestConstants.DEFAULT_EMAIL);
		// then
		assertTrue(result);
	}

	@Test
	void testTrueIsExistentJourneyRequestByIdAndClientMobileNumberAndIcc() {
		// given
		given(journeyRequestRepository.existsAndNotExpiredByIdAndClient_Icc_ValueAndClient_MobileNumber(any(Long.class),
				any(String.class), any(String.class))).willReturn(true);
		// when
		String[] mobileNumber = TestConstants.DEFAULT_MOBILE_NUMBER_USERNAME.split("_");
		boolean result = journeyRequestsPersistenceAdapter
				.isExistentAndNotExpiredJourneyRequestByIdAndClientMobileNumberAndIcc(1L, mobileNumber[0],
						mobileNumber[1]);
		// then
		assertTrue(result);
	}

	@Test
	void testFalseIsExistentJourneyRequestByIdAndClientEmail() {
		// given
		given(journeyRequestRepository.existsAndNotExpiredByIdAndClient_Email(any(Long.class), any(String.class)))
				.willReturn(false);
		// when
		boolean result = journeyRequestsPersistenceAdapter.isExistentAndNotExpiredJourneyRequestByIdAndClientEmail(1L,
				TestConstants.DEFAULT_EMAIL);
		// then
		assertFalse(result);
	}

	@Test
	void testFalseIsExistentJourneyRequestByIdAndClientMobileNumberAndIcc() {
		// given
		given(journeyRequestRepository.existsAndNotExpiredByIdAndClient_Icc_ValueAndClient_MobileNumber(any(Long.class),
				any(String.class), any(String.class))).willReturn(false);
		// when
		String[] mobileNumber = TestConstants.DEFAULT_MOBILE_NUMBER_USERNAME.split("_");
		boolean result = journeyRequestsPersistenceAdapter
				.isExistentAndNotExpiredJourneyRequestByIdAndClientMobileNumberAndIcc(1L, mobileNumber[0],
						mobileNumber[1]);
		// then
		assertFalse(result);
	}

	@Test
	void testConvertToSort() {

		Sort sort1 = journeyRequestsPersistenceAdapter.convertToSort(new SortCriterion("min-price", "desc"));
		Sort sort2 = journeyRequestsPersistenceAdapter.convertToSort(new SortCriterion("date-time", "asc"));
		Sort sort3 = journeyRequestsPersistenceAdapter.convertToSort(new SortCriterion("distance", "asc"));

		assertEquals(sort1.get().toArray().length, 1);
		assertTrue(sort1.getOrderFor("(minPrice)") != null
				&& sort1.getOrderFor("(minPrice)").getDirection().isDescending());

		assertEquals(sort2.get().toArray().length, 1);
		assertTrue(sort2.getOrderFor("dateTime") != null && sort2.getOrderFor("dateTime").getDirection().isAscending());

		assertEquals(sort3.get().toArray().length, 1);
		assertTrue(sort3.getOrderFor("distance") != null && sort3.getOrderFor("distance").getDirection().isAscending());
	}

	@Test
	void testUpdateJourneyRequest() {

		// given

		JourneyRequestInputOutput journeyRequestInputOutput = JourneyRequestTestData
				.defaultJourneyRequestInputOutputBuilder().build();

		JourneyRequestJpaEntity journeyRequestJpaEntity = JourneyRequestJpaTestData
				.defaultExistentJourneyRequestJpaEntity();

		given(journeyRequestRepository.findById(any(Long.class))).willReturn(Optional.of(journeyRequestJpaEntity));

		// DeparturePlace data

		DepartmentJpaEntity departureDepartmentJpaEntity = DepartmentJpaTestData
				.defaultExistentDepartureDepartmentJpaEntity();
		given(departmentResolver.resolveDepartment(journeyRequestInputOutput.getDeparturePlace().getId(),
				journeyRequestInputOutput.getDeparturePlace().getType()))
						.willReturn(Optional.of(departureDepartmentJpaEntity));

		PlaceJpaEntity departurePlaceJpaEntity = PlaceJpaTestData.defaultPlaceJpaEntity();
		given(placeRepository.findById(new PlaceId(journeyRequestInputOutput.getDeparturePlace().getId(),
				journeyRequestInputOutput.getDeparturePlace().getType())))
						.willReturn(Optional.of(departurePlaceJpaEntity));

		// ArrivalPlace data

		DepartmentJpaEntity arrivalDepartmentJpaEntity = DepartmentJpaTestData
				.defaultExistentArrivalDepartmentJpaEntity();
		given(departmentResolver.resolveDepartment(journeyRequestInputOutput.getArrivalPlace().getId(),
				journeyRequestInputOutput.getArrivalPlace().getType()))
						.willReturn(Optional.of(arrivalDepartmentJpaEntity));

		PlaceJpaEntity arrivalPlaceJpaEntity = PlaceJpaTestData.defaultPlaceJpaEntity();
		given(placeRepository.findById(new PlaceId(journeyRequestInputOutput.getArrivalPlace().getId(),
				journeyRequestInputOutput.getArrivalPlace().getType()))).willReturn(Optional.of(arrivalPlaceJpaEntity));

		// EngineType data
		EngineTypeJpaEntity engineTypeJpaEntity = EngineTypeJpaTestData.defaultEngineTypeJpaEntity();
		given(engineTypeRepository.findById(journeyRequestInputOutput.getEngineType().getId()))
				.willReturn(Optional.of(engineTypeJpaEntity));

		// when
		journeyRequestsPersistenceAdapter.updateJourneyRequest(journeyRequestInputOutput);
		// then
		ArgumentCaptor<JourneyRequestJpaEntity> captor = ArgumentCaptor.forClass(JourneyRequestJpaEntity.class);
		then(journeyRequestRepository).should(times(1)).save(captor.capture());

		assertEquals(departurePlaceJpaEntity, captor.getValue().getDeparturePlace());
		assertEquals(arrivalPlaceJpaEntity, captor.getValue().getArrivalPlace());
		assertEquals(engineTypeJpaEntity, captor.getValue().getEngineType());
		assertEquals(journeyRequestInputOutput.getWorkers(), captor.getValue().getWorkers());
		assertEquals(journeyRequestInputOutput.getDescription(), captor.getValue().getDescription());
		assertEquals(journeyRequestInputOutput.getDateTime(), captor.getValue().getDateTime());
		assertEquals(journeyRequestInputOutput.getHours(), captor.getValue().getHours());
		assertEquals(journeyRequestInputOutput.getMinutes(), captor.getValue().getMinutes());
		assertEquals(journeyRequestInputOutput.getDistance(), captor.getValue().getDistance());

	}

	@Test
	void givenDeparturePlaceDepartmentNotFound_whentUpdateJourneyRequest_thenNeverUpdate() {

		// given

		JourneyRequestInputOutput journeyRequestInputOutput = JourneyRequestTestData
				.defaultJourneyRequestInputOutputBuilder().build();

		JourneyRequestJpaEntity journeyRequestJpaEntity = JourneyRequestJpaTestData
				.defaultExistentJourneyRequestJpaEntity();

		given(journeyRequestRepository.findById(any(Long.class))).willReturn(Optional.of(journeyRequestJpaEntity));

		// DeparturePlace data

		given(departmentResolver.resolveDepartment(journeyRequestInputOutput.getDeparturePlace().getId(),
				journeyRequestInputOutput.getDeparturePlace().getType())).willReturn(Optional.empty());

		// ArrivalPlace data

		// EngineType data

		// when
		journeyRequestsPersistenceAdapter.updateJourneyRequest(journeyRequestInputOutput);
		// then
		then(journeyRequestRepository).should(never()).save(any(JourneyRequestJpaEntity.class));

	}

	@Test
	void givenExistentDeparturePlaceDepartment_andArrivalPlaceDepartmentNotFound_whentUpdateJourneyRequest_thenNeverUpdate() {

		// given

		JourneyRequestInputOutput journeyRequestInputOutput = JourneyRequestTestData
				.defaultJourneyRequestInputOutputBuilder().build();

		JourneyRequestJpaEntity journeyRequestJpaEntity = JourneyRequestJpaTestData
				.defaultExistentJourneyRequestJpaEntity();

		given(journeyRequestRepository.findById(any(Long.class))).willReturn(Optional.of(journeyRequestJpaEntity));

		// DeparturePlace data
		DepartmentJpaEntity departureDepartmentJpaEntity = DepartmentJpaTestData
				.defaultExistentDepartureDepartmentJpaEntity();
		given(departmentResolver.resolveDepartment(journeyRequestInputOutput.getDeparturePlace().getId(),
				journeyRequestInputOutput.getDeparturePlace().getType()))
						.willReturn(Optional.of(departureDepartmentJpaEntity));

		// ArrivalPlace data
		given(departmentResolver.resolveDepartment(journeyRequestInputOutput.getArrivalPlace().getId(),
				journeyRequestInputOutput.getArrivalPlace().getType())).willReturn(Optional.empty());
		// EngineType data

		// when
		journeyRequestsPersistenceAdapter.updateJourneyRequest(journeyRequestInputOutput);
		// then
		then(journeyRequestRepository).should(never()).save(any(JourneyRequestJpaEntity.class));

	}

	@Test
	void givenExistentDeparturePlaceDepartment_andArrivalPlaceDepartment_andEngineTypeNotFoundById_whentUpdateJourneyRequest_thenNeverUpdate() {

		// given

		JourneyRequestInputOutput journeyRequestInputOutput = JourneyRequestTestData
				.defaultJourneyRequestInputOutputBuilder().build();

		JourneyRequestJpaEntity journeyRequestJpaEntity = JourneyRequestJpaTestData
				.defaultExistentJourneyRequestJpaEntity();

		given(journeyRequestRepository.findById(any(Long.class))).willReturn(Optional.of(journeyRequestJpaEntity));

		// DeparturePlace data
		DepartmentJpaEntity departureDepartmentJpaEntity = DepartmentJpaTestData
				.defaultExistentDepartureDepartmentJpaEntity();
		given(departmentResolver.resolveDepartment(journeyRequestInputOutput.getDeparturePlace().getId(),
				journeyRequestInputOutput.getDeparturePlace().getType()))
						.willReturn(Optional.of(departureDepartmentJpaEntity));

		// ArrivalPlace data
		DepartmentJpaEntity arrivalDepartmentJpaEntity = DepartmentJpaTestData
				.defaultExistentArrivalDepartmentJpaEntity();
		given(departmentResolver.resolveDepartment(journeyRequestInputOutput.getArrivalPlace().getId(),
				journeyRequestInputOutput.getArrivalPlace().getType()))
						.willReturn(Optional.of(arrivalDepartmentJpaEntity));
		// EngineType data
		given(engineTypeRepository.findById(any(Long.class))).willReturn(Optional.empty());

		// when
		journeyRequestsPersistenceAdapter.updateJourneyRequest(journeyRequestInputOutput);
		// then
		then(journeyRequestRepository).should(never()).save(any(JourneyRequestJpaEntity.class));

	}

	@Test
	void givenDeparturePlaceAndArrivalPlaceNotFoundById_whenUpdateJourneyRequest_thenCreateDeparturePlace_andArrivalPlace_andUpdateJourneyREquest() {

		// given

		JourneyRequestInputOutput journeyRequestInputOutput = JourneyRequestTestData
				.defaultJourneyRequestInputOutputBuilder().build();

		JourneyRequestJpaEntity journeyRequestJpaEntity = JourneyRequestJpaTestData
				.defaultExistentJourneyRequestJpaEntity();

		given(journeyRequestRepository.findById(any(Long.class))).willReturn(Optional.of(journeyRequestJpaEntity));

		// DeparturePlace data

		DepartmentJpaEntity departureDepartmentJpaEntity = DepartmentJpaTestData
				.defaultExistentDepartureDepartmentJpaEntity();
		given(departmentResolver.resolveDepartment(journeyRequestInputOutput.getDeparturePlace().getId(),
				journeyRequestInputOutput.getDeparturePlace().getType()))
						.willReturn(Optional.of(departureDepartmentJpaEntity));

		PlaceJpaEntity departurePlaceJpaEntity = PlaceJpaTestData.defaultPlaceJpaEntity();
		given(placeRepository.findById(new PlaceId(journeyRequestInputOutput.getDeparturePlace().getId(),
				journeyRequestInputOutput.getDeparturePlace().getType()))).willReturn(Optional.empty());

		given(placeMapper.mapToJpaEntity(journeyRequestInputOutput.getDeparturePlace(), departureDepartmentJpaEntity))
				.willReturn(departurePlaceJpaEntity);

		given(localizedPlaceResolver.resolveLocalizedPlaces(journeyRequestInputOutput.getDeparturePlace().getId(),
				journeyRequestInputOutput.getDeparturePlace().getType())).willReturn(
						departurePlaceJpaEntity.getLocalizations().values().stream().collect(Collectors.toList()));

		given(placeRepository.save(departurePlaceJpaEntity)).willReturn(departurePlaceJpaEntity);

		// ArrivalPlace data

		DepartmentJpaEntity arrivalDepartmentJpaEntity = DepartmentJpaTestData
				.defaultExistentArrivalDepartmentJpaEntity();
		given(departmentResolver.resolveDepartment(journeyRequestInputOutput.getArrivalPlace().getId(),
				journeyRequestInputOutput.getArrivalPlace().getType()))
						.willReturn(Optional.of(arrivalDepartmentJpaEntity));

		PlaceJpaEntity arrivalPlaceJpaEntity = PlaceJpaTestData.defaultPlaceJpaEntity();
		given(placeRepository.findById(new PlaceId(journeyRequestInputOutput.getArrivalPlace().getId(),
				journeyRequestInputOutput.getArrivalPlace().getType()))).willReturn(Optional.empty());

		given(placeMapper.mapToJpaEntity(journeyRequestInputOutput.getArrivalPlace(), arrivalDepartmentJpaEntity))
				.willReturn(arrivalPlaceJpaEntity);
		given(localizedPlaceResolver.resolveLocalizedPlaces(journeyRequestInputOutput.getArrivalPlace().getId(),
				journeyRequestInputOutput.getArrivalPlace().getType())).willReturn(
						arrivalPlaceJpaEntity.getLocalizations().values().stream().collect(Collectors.toList()));

		given(placeRepository.save(arrivalPlaceJpaEntity)).willReturn(arrivalPlaceJpaEntity);

		// EngineType data
		EngineTypeJpaEntity engineTypeJpaEntity = EngineTypeJpaTestData.defaultEngineTypeJpaEntity();
		given(engineTypeRepository.findById(journeyRequestInputOutput.getEngineType().getId()))
				.willReturn(Optional.of(engineTypeJpaEntity));

		// when
		journeyRequestsPersistenceAdapter.updateJourneyRequest(journeyRequestInputOutput);
		// then
		ArgumentCaptor<JourneyRequestJpaEntity> captor = ArgumentCaptor.forClass(JourneyRequestJpaEntity.class);
		then(journeyRequestRepository).should(times(1)).save(captor.capture());

		assertEquals(departurePlaceJpaEntity, captor.getValue().getDeparturePlace());
		assertEquals(arrivalPlaceJpaEntity, captor.getValue().getArrivalPlace());
		assertEquals(engineTypeJpaEntity, captor.getValue().getEngineType());
		assertEquals(journeyRequestInputOutput.getWorkers(), captor.getValue().getWorkers());
		assertEquals(journeyRequestInputOutput.getDescription(), captor.getValue().getDescription());
		assertEquals(journeyRequestInputOutput.getDateTime(), captor.getValue().getDateTime());
		assertEquals(journeyRequestInputOutput.getHours(), captor.getValue().getHours());
		assertEquals(journeyRequestInputOutput.getMinutes(), captor.getValue().getMinutes());
		assertEquals(journeyRequestInputOutput.getDistance(), captor.getValue().getDistance());

	}

	@Test
	public void testCancelJourneyRequest() {

		// given
		JourneyRequestStatusJpaEntity status = canceledJourneyRequestStatusJpaEntity();

		given(journeyRequestStatusRepository.findByCode(JourneyRequestStatusCode.CANCELED)).willReturn(status);

		// when
		journeyRequestsPersistenceAdapter.cancelJourneyRequest(1L);

		// then
		then(journeyRequestRepository).should(times(1)).updateStatus(1L, status);
	}

	@Test
	void testLoadJourneyRequestIdsByStatusCodeAndLimit() {

		// given
		givenJourneyRequestsPage();

		// When
		Set<Long> result = journeyRequestsPersistenceAdapter
				.loadJourneyRequestIdsByStatusCodeAndLimit(JourneyRequestStatusCode.FULFILLED, 10);
		// then
		assertEquals(1, result.size());
		assertEquals(Set.of(defaultExistentJourneyRequestJpaEntity().getId()), result);
	}

	@Test
	void testUpdateJourneyStatus() {
		// given
		JourneyRequestStatusJpaEntity journeyRequestStatusJpaEntity = defaultJourneyRequestStatusJpaEntityBuilder()
				.code(JourneyRequestStatusCode.ARCHIVED).build();
		given(journeyRequestStatusRepository.findByCode(JourneyRequestStatusCode.ARCHIVED))
				.willReturn(journeyRequestStatusJpaEntity);
		// when
		journeyRequestsPersistenceAdapter.updateJourneyStatus(Set.of(1L, 2L), JourneyRequestStatusCode.ARCHIVED);
		// then
		then(journeyRequestRepository).should(times(1)).updateInBatch(Set.of(1L, 2L), journeyRequestStatusJpaEntity);
	}

	private Page<JourneyRequestSearchOutput> givenNotNullJourneyRequestsPageByDeparturePlace_RegionIdAndArrivalPlace_RegionIdInAndEngineType_IdInAndDateBetween() {
		Page<JourneyRequestSearchOutput> result = createPageFromJourneyRequestSearchOutputList(
				defaultJourneyRequestSearchOutputList());
		given(journeyRequestRepository
				.findByDeparturePlace_DepartmentIdAndArrivalPlace_DepartmentIdInAndEngineType_IdInAndDateBetween(
						any(Long.class), any(Set.class), any(Set.class), any(Instant.class), any(Instant.class),
						any(String.class), any(Pageable.class))).willReturn(result);

		return result;

	}

	private Page<JourneyRequestJpaEntity> givenJourneyRequestsPage() {
		Page<JourneyRequestJpaEntity> result = createPageFromJourneyRequestJpaEntityList(
				List.of(defaultExistentJourneyRequestJpaEntity()));

		given(journeyRequestRepository.findByStatus_Code(any(JourneyRequestStatusCode.class), any(Pageable.class)))
				.willReturn(result);

		return result;

	}

	private Page<JourneyRequestSearchDto> givenNullJourneyRequestsPageByDeparturePlace_RegionIdAndEngineType_IdInAndDateBetween() {

		given(journeyRequestRepository.findByDeparturePlace_DepartmentIdAndEngineType_IdInAndDateBetween(
				any(Long.class), any(Set.class), any(Instant.class), any(Instant.class), any(String.class),
				any(Pageable.class))).willReturn(null);

		return null;
	}

	private Page<JourneyRequestSearchOutput> givenNotNullJourneyRequestsPageByDeparturePlace_RegionIdAndEngineType_IdInAndDateBetween() {
		Page<JourneyRequestSearchOutput> result = createPageFromJourneyRequestSearchOutputList(
				defaultJourneyRequestSearchOutputList());
		given(journeyRequestRepository.findByDeparturePlace_DepartmentIdAndEngineType_IdInAndDateBetween(
				any(Long.class), any(Set.class), any(Instant.class), any(Instant.class), any(String.class),
				any(Pageable.class))).willReturn(result);

		return result;

	}

	private Page<JourneyRequestSearchOutput> createPageFromJourneyRequestSearchOutputList(
			List<JourneyRequestSearchOutput> journeyRequestSearchOutputList) {
		return new Page<JourneyRequestSearchOutput>() {

			@Override
			public int getNumber() {

				return 0;
			}

			@Override
			public int getSize() {

				return 2;
			}

			@Override
			public int getNumberOfElements() {

				return 2;
			}

			@Override
			public List<JourneyRequestSearchOutput> getContent() {

				return journeyRequestSearchOutputList;
			}

			@Override
			public boolean hasContent() {

				return true;
			}

			@Override
			public Sort getSort() {

				return Sort.by(List.of(new Order(Direction.DESC, "min-price")));
			}

			@Override
			public boolean isFirst() {

				return true;
			}

			@Override
			public boolean isLast() {

				return false;
			}

			@Override
			public boolean hasNext() {

				return true;
			}

			@Override
			public boolean hasPrevious() {

				return false;
			}

			@Override
			public Pageable nextPageable() {

				return null;
			}

			@Override
			public Pageable previousPageable() {

				return null;
			}

			@Override
			public Iterator<JourneyRequestSearchOutput> iterator() {

				return journeyRequestSearchOutputList.iterator();
			}

			@Override
			public int getTotalPages() {

				return 5;
			}

			@Override
			public long getTotalElements() {

				return 10;
			}

			@Override
			public <U> Page<U> map(Function<? super JourneyRequestSearchOutput, ? extends U> converter) {

				return null;
			}
		};
	}

	private Page<JourneyRequestJpaEntity> createPageFromJourneyRequestJpaEntityList(
			List<JourneyRequestJpaEntity> journeyRequestJpaEntityList) {
		return new Page<JourneyRequestJpaEntity>() {

			@Override
			public int getNumber() {

				return 0;
			}

			@Override
			public int getSize() {

				return 1;
			}

			@Override
			public int getNumberOfElements() {

				return 1;
			}

			@Override
			public List<JourneyRequestJpaEntity> getContent() {

				return journeyRequestJpaEntityList;
			}

			@Override
			public boolean hasContent() {

				return true;
			}

			@Override
			public Sort getSort() {

				return null;
			}

			@Override
			public boolean isFirst() {

				return true;
			}

			@Override
			public boolean isLast() {

				return true;
			}

			@Override
			public boolean hasNext() {

				return false;
			}

			@Override
			public boolean hasPrevious() {

				return false;
			}

			@Override
			public Pageable nextPageable() {

				return null;
			}

			@Override
			public Pageable previousPageable() {

				return null;
			}

			@Override
			public Iterator<JourneyRequestJpaEntity> iterator() {

				return journeyRequestJpaEntityList.iterator();
			}

			@Override
			public int getTotalPages() {

				return 1;
			}

			@Override
			public long getTotalElements() {

				return 1;
			}

			@Override
			public <U> Page<U> map(Function<? super JourneyRequestJpaEntity, ? extends U> converter) {

				return null;
			}
		};
	}

	private Page<ClientJourneyRequestDtoOutput> createPageFromClientJourneyRequestDto(
			List<ClientJourneyRequestDtoOutput> clientJourneyRequestDto) {
		return new Page<ClientJourneyRequestDtoOutput>() {

			@Override
			public int getNumber() {

				return 0;
			}

			@Override
			public int getSize() {

				return 25;
			}

			@Override
			public int getNumberOfElements() {

				return 2;
			}

			@Override
			public List<ClientJourneyRequestDtoOutput> getContent() {

				return clientJourneyRequestDto;
			}

			@Override
			public boolean hasContent() {

				return true;
			}

			@Override
			public Sort getSort() {

				return Sort.by(List.of(new Order(Direction.ASC, "price")));
			}

			@Override
			public boolean isFirst() {

				return true;
			}

			@Override
			public boolean isLast() {

				return false;
			}

			@Override
			public boolean hasNext() {

				return true;
			}

			@Override
			public boolean hasPrevious() {

				return false;
			}

			@Override
			public Pageable nextPageable() {

				return null;
			}

			@Override
			public Pageable previousPageable() {

				return null;
			}

			@Override
			public Iterator<ClientJourneyRequestDtoOutput> iterator() {

				return clientJourneyRequestDto.iterator();
			}

			@Override
			public int getTotalPages() {

				return 1;
			}

			@Override
			public long getTotalElements() {

				return 2;
			}

			@Override
			public <U> Page<U> map(Function<? super ClientJourneyRequestDtoOutput, ? extends U> converter) {
				// TODO Auto-generated method stub
				return null;
			}

		};
	}
}
