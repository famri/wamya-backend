package com.excentria_it.wamya.adapter.persistence.adapter;

import static com.excentria_it.wamya.test.data.common.JourneyRequestJpaTestData.*;
import static com.excentria_it.wamya.test.data.common.JourneyRequestTestData.*;
import static com.excentria_it.wamya.test.data.common.UserAccountJpaEntityTestData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

import com.excentria_it.wamya.adapter.persistence.entity.ClientJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.EngineTypeJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.JourneyRequestJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.PlaceJpaEntity;
import com.excentria_it.wamya.adapter.persistence.mapper.JourneyRequestMapper;
import com.excentria_it.wamya.adapter.persistence.mapper.PlaceMapper;
import com.excentria_it.wamya.adapter.persistence.repository.ClientRepository;
import com.excentria_it.wamya.adapter.persistence.repository.EngineTypeRepository;
import com.excentria_it.wamya.adapter.persistence.repository.JourneyRequestRepository;
import com.excentria_it.wamya.adapter.persistence.repository.PlaceRepository;
import com.excentria_it.wamya.common.SortCriterion;
import com.excentria_it.wamya.domain.ClientJourneyRequestDto;
import com.excentria_it.wamya.domain.ClientJourneyRequests;
import com.excentria_it.wamya.domain.CreateJourneyRequestDto;
import com.excentria_it.wamya.domain.JourneyRequestSearchDto;
import com.excentria_it.wamya.domain.JourneyRequestsSearchResult;
import com.excentria_it.wamya.domain.LoadClientJourneyRequestsCriteria;
import com.excentria_it.wamya.domain.SearchJourneyRequestsCriteria;
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
	private PlaceMapper placeMapper;

	@InjectMocks
	private JourneyRequestsPersistenceAdapter journeyRequestsPersistenceAdapter;

	@Test
	void givenNotNullJourneyRequestsPage_WhenSearchJourneyRequests_ThenReturnJourneyRequestsSearchResult() {

		// given
		SearchJourneyRequestsCriteria command = defaultSearchJourneyRequestsCriteriaBuilder().build();

		Page<JourneyRequestSearchDto> expectedResult = givenNotNullJourneyRequestsPageByDeparturePlace_RegionIdAndArrivalPlace_RegionIdInAndEngineType_IdInAndDateBetween();

		// when

		JourneyRequestsSearchResult result = journeyRequestsPersistenceAdapter.searchJourneyRequests(command);
		// then

		assertEquals(expectedResult.getTotalPages(), result.getTotalPages());
		assertEquals(expectedResult.getTotalElements(), result.getTotalElements());
		assertEquals(expectedResult.getNumber(), result.getPageNumber());
		assertEquals(expectedResult.getSize(), result.getPageSize());
		assertEquals(expectedResult.hasNext(), result.isHasNext());
		assertEquals(expectedResult.getContent(), result.getContent());

	}

	@Test
	void givenNullJourneyRequestsPage_WhenSearchJourneyRequests_ThenReturnEmptyJourneyRequestsSearchResult() {

		// given
		SearchJourneyRequestsCriteria command = defaultSearchJourneyRequestsCriteriaBuilder().build();

		givenNullJourneyRequestsPageByDeparturePlace_RegionIdAndArrivalPlace_RegionIdInAndEngineType_IdInAndDateBetween();

		// when

		JourneyRequestsSearchResult result = journeyRequestsPersistenceAdapter.searchJourneyRequests(command);

		assertEquals(0, result.getTotalPages());
		assertEquals(0, result.getTotalElements());
		assertEquals(command.getPageNumber(), result.getPageNumber());
		assertEquals(command.getPageSize(), result.getPageSize());
		assertEquals(false, result.isHasNext());
		assertEquals(Collections.<JourneyRequestSearchDto>emptyList(), result.getContent());

	}

	@Test
	void givenNotNullJourneyRequestsPage_WhenSearchJourneyRequestsByAnyArrivalPlace_ThenReturnJourneyRequestsSearchResult() {

		// given
		SearchJourneyRequestsCriteria command = defaultSearchJourneyRequestsCriteriaBuilder().build();

		command.setArrivalPlaceRegionIds(Set.of("ANY"));

		Page<JourneyRequestSearchDto> expectedResult = givenNotNullJourneyRequestsPageByDeparturePlace_RegionIdAndEngineType_IdInAndDateBetween();

		// when

		JourneyRequestsSearchResult result = journeyRequestsPersistenceAdapter.searchJourneyRequests(command);
		// then

		assertEquals(expectedResult.getTotalPages(), result.getTotalPages());
		assertEquals(expectedResult.getTotalElements(), result.getTotalElements());
		assertEquals(expectedResult.getNumber(), result.getPageNumber());
		assertEquals(expectedResult.getSize(), result.getPageSize());
		assertEquals(expectedResult.hasNext(), result.isHasNext());
		assertEquals(expectedResult.getContent(), result.getContent());

	}

	@Test
	void givenNullJourneyRequestsPage_WhenSearchJourneyRequestsByAnyArrivalPlace_ThenReturnEmptyJourneyRequestsSearchResult() {

		// given
		SearchJourneyRequestsCriteria command = defaultSearchJourneyRequestsCriteriaBuilder().build();
		command.setArrivalPlaceRegionIds(Set.of("ANY"));

		givenNullJourneyRequestsPageByDeparturePlace_RegionIdAndEngineType_IdInAndDateBetween();

		// when

		JourneyRequestsSearchResult result = journeyRequestsPersistenceAdapter.searchJourneyRequests(command);
		// then

		assertEquals(0, result.getTotalPages());
		assertEquals(0, result.getTotalElements());
		assertEquals(command.getPageNumber(), result.getPageNumber());
		assertEquals(command.getPageSize(), result.getPageSize());
		assertEquals(false, result.isHasNext());
		assertEquals(Collections.<JourneyRequestSearchDto>emptyList(), result.getContent());

	}

	@Test
	void givenExistentDeparturePlaceAndExistentArrivalPlace_WhenCreateJourneyRequest_ThenSaveJourneyRequestJpaEntity() {

		// given
		CreateJourneyRequestDto journeyRequest = defaultCreateJourneyRequestDto();

		EngineTypeJpaEntity engineTypeJpaEntity = defaultEngineTypeJpaEntity();
		given(engineTypeRepository.findById(journeyRequest.getEngineType().getId()))
				.willReturn(Optional.of(engineTypeJpaEntity));

		ClientJpaEntity clientJpaEntity = defaultExistentClientJpaEntity();
		given(clientRepository.findByEmail(TestConstants.DEFAULT_EMAIL)).willReturn(Optional.of(clientJpaEntity));

		PlaceJpaEntity departurePlaceJpaEntity = defaultDeparturePlaceJpaEntity();
		given(placeRepository.findById(journeyRequest.getDeparturePlace().getPlaceId()))
				.willReturn(Optional.of(departurePlaceJpaEntity));

		PlaceJpaEntity arrivalPlaceJpaEntity = defaultArrivalPlaceJpaEntity();
		given(placeRepository.findById(journeyRequest.getArrivalPlace().getPlaceId()))
				.willReturn(Optional.of(arrivalPlaceJpaEntity));

		JourneyRequestJpaEntity journeyRequestJpaEntity = defaultExistentJourneyRequestJpaEntity();
		given(journeyRequestMapper.mapToJpaEntity(journeyRequest, departurePlaceJpaEntity, arrivalPlaceJpaEntity,
				engineTypeJpaEntity, clientJpaEntity)).willReturn(journeyRequestJpaEntity);

		given(journeyRequestRepository.save(journeyRequestJpaEntity)).willReturn(journeyRequestJpaEntity);

		// when
		journeyRequestsPersistenceAdapter.createJourneyRequest(journeyRequest, TestConstants.DEFAULT_EMAIL);

		// then
		then(journeyRequestRepository).should(times(1)).save(journeyRequestJpaEntity);
	}

	@Test
	void givenNonExistentDeparturePlaceAndNonExistentArrivalPlace_WhenCreateJourneyRequest_ThenSaveJourneyRequestJpaEntity() {

		// given
		CreateJourneyRequestDto journeyRequest = defaultCreateJourneyRequestDto();

		EngineTypeJpaEntity engineTypeJpaEntity = defaultEngineTypeJpaEntity();
		given(engineTypeRepository.findById(journeyRequest.getEngineType().getId()))
				.willReturn(Optional.of(engineTypeJpaEntity));

		ClientJpaEntity clientJpaEntity = defaultExistentClientJpaEntity();
		given(clientRepository.findByEmail(TestConstants.DEFAULT_EMAIL)).willReturn(Optional.of(clientJpaEntity));

		PlaceJpaEntity departurePlaceJpaEntity = defaultDeparturePlaceJpaEntity();
		given(placeRepository.findById(journeyRequest.getDeparturePlace().getPlaceId()))
				.willReturn(Optional.ofNullable(null));
		given(placeMapper.mapToJpaEntity(journeyRequest.getDeparturePlace())).willReturn(departurePlaceJpaEntity);
		given(placeRepository.save(departurePlaceJpaEntity)).willReturn(departurePlaceJpaEntity);

		PlaceJpaEntity arrivalPlaceJpaEntity = defaultArrivalPlaceJpaEntity();
		given(placeRepository.findById(journeyRequest.getArrivalPlace().getPlaceId()))
				.willReturn(Optional.ofNullable(null));
		given(placeMapper.mapToJpaEntity(journeyRequest.getArrivalPlace())).willReturn(arrivalPlaceJpaEntity);
		given(placeRepository.save(arrivalPlaceJpaEntity)).willReturn(arrivalPlaceJpaEntity);

		JourneyRequestJpaEntity journeyRequestJpaEntity = defaultExistentJourneyRequestJpaEntity();
		given(journeyRequestMapper.mapToJpaEntity(journeyRequest, departurePlaceJpaEntity, arrivalPlaceJpaEntity,
				engineTypeJpaEntity, clientJpaEntity)).willReturn(journeyRequestJpaEntity);

		given(journeyRequestRepository.save(journeyRequestJpaEntity)).willReturn(journeyRequestJpaEntity);

		// when
		journeyRequestsPersistenceAdapter.createJourneyRequest(journeyRequest, TestConstants.DEFAULT_EMAIL);

		// then
		then(journeyRequestRepository).should(times(1)).save(journeyRequestJpaEntity);
	}

	@Test
	void givenExistentUserAccountByPhoneNumber_WhencreateJourneyRequest_ThenSaveJourneyRequestJpaEntity() {

		// given
		CreateJourneyRequestDto journeyRequest = defaultCreateJourneyRequestDto();

		EngineTypeJpaEntity engineTypeJpaEntity = defaultEngineTypeJpaEntity();
		given(engineTypeRepository.findById(journeyRequest.getEngineType().getId()))
				.willReturn(Optional.of(engineTypeJpaEntity));

		ClientJpaEntity clientJpaEntity = defaultExistentClientJpaEntity();
		given(clientRepository.findByEmail(TestConstants.DEFAULT_MOBILE_NUMBER_USERNAME))
				.willReturn(Optional.ofNullable(null));

		given(clientRepository.findByMobilePhoneNumber(any(String.class), any(String.class)))
				.willReturn(Optional.of(clientJpaEntity));

		PlaceJpaEntity departurePlaceJpaEntity = defaultDeparturePlaceJpaEntity();
		given(placeRepository.findById(journeyRequest.getDeparturePlace().getPlaceId()))
				.willReturn(Optional.ofNullable(null));
		given(placeMapper.mapToJpaEntity(journeyRequest.getDeparturePlace())).willReturn(departurePlaceJpaEntity);
		given(placeRepository.save(departurePlaceJpaEntity)).willReturn(departurePlaceJpaEntity);

		PlaceJpaEntity arrivalPlaceJpaEntity = defaultArrivalPlaceJpaEntity();
		given(placeRepository.findById(journeyRequest.getArrivalPlace().getPlaceId()))
				.willReturn(Optional.ofNullable(null));
		given(placeMapper.mapToJpaEntity(journeyRequest.getArrivalPlace())).willReturn(arrivalPlaceJpaEntity);
		given(placeRepository.save(arrivalPlaceJpaEntity)).willReturn(arrivalPlaceJpaEntity);

		JourneyRequestJpaEntity journeyRequestJpaEntity = defaultExistentJourneyRequestJpaEntity();
		given(journeyRequestMapper.mapToJpaEntity(journeyRequest, departurePlaceJpaEntity, arrivalPlaceJpaEntity,
				engineTypeJpaEntity, clientJpaEntity)).willReturn(journeyRequestJpaEntity);

		given(journeyRequestRepository.save(journeyRequestJpaEntity)).willReturn(journeyRequestJpaEntity);

		// when
		journeyRequestsPersistenceAdapter.createJourneyRequest(journeyRequest,
				TestConstants.DEFAULT_MOBILE_NUMBER_USERNAME);

		// then
		then(journeyRequestRepository).should(times(1)).save(journeyRequestJpaEntity);
	}

	@Test
	void testLoadJourneyRequestById() {

		// given
		JourneyRequestJpaEntity journeyRequestJpaEntity = defaultExistentJourneyRequestJpaEntity();
		given(journeyRequestRepository.findById(any(Long.class))).willReturn(Optional.of(journeyRequestJpaEntity));

		CreateJourneyRequestDto createJourneyRequestDto = defaultCreateJourneyRequestDto();
		given(journeyRequestMapper.mapToDomainEntity(any(JourneyRequestJpaEntity.class), any(String.class)))
				.willReturn(createJourneyRequestDto);
		// when
		Optional<CreateJourneyRequestDto> createJourneyRequestDtoOptional = journeyRequestsPersistenceAdapter
				.loadJourneyRequestById(1L);
		// then

		assertEquals(createJourneyRequestDto, createJourneyRequestDtoOptional.get());

	}

	@Test
	void givenNotNullJourneyRequestsPageAnClientEmail_WhenLoadClientJourneyRequests_ThenReturnClientJourneyRequests() {

		// given

		Page<ClientJourneyRequestDto> page = createPageFromClientJourneyRequestDto(
				defaultClientJourneyRequestDtoList());
		given(journeyRequestRepository.findByCreationDateTimeBetweenAndClient_Email(any(LocalDateTime.class),
				any(LocalDateTime.class), any(String.class), any(String.class), any(Pageable.class))).willReturn(page);
		LoadClientJourneyRequestsCriteria criteria = defaultLoadClientJourneyRequestsCriteriaBuilder().build();
		// when
		ClientJourneyRequests result = journeyRequestsPersistenceAdapter.loadClientJourneyRequests(criteria);
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

		given(journeyRequestRepository.findByCreationDateTimeBetweenAndClient_Email(any(LocalDateTime.class),
				any(LocalDateTime.class), any(String.class), any(String.class), any(Pageable.class))).willReturn(null);
		LoadClientJourneyRequestsCriteria criteria = defaultLoadClientJourneyRequestsCriteriaBuilder().build();
		// when
		ClientJourneyRequests result = journeyRequestsPersistenceAdapter.loadClientJourneyRequests(criteria);
		// then
		assertEquals(0, result.getTotalPages());
		assertEquals(criteria.getPageSize(), result.getPageSize());
		assertEquals(0, result.getTotalElements());
		assertEquals(criteria.getPageNumber(), result.getPageNumber());
		assertEquals(false, result.isHasNext());
		assertEquals(Collections.<ClientJourneyRequestDto>emptyList(), result.getContent());
	}

	@Test
	void givenNotNullJourneyRequestsPageAnClientMobileNumber_WhenLoadClientJourneyRequests_ThenReturnClientJourneyRequests() {

		// given

		Page<ClientJourneyRequestDto> page = createPageFromClientJourneyRequestDto(
				defaultClientJourneyRequestDtoList());
		given(journeyRequestRepository.findByCreationDateTimeBetweenAndClient_MobileNumberAndClient_IccValue(
				any(LocalDateTime.class), any(LocalDateTime.class), any(String.class), any(String.class),
				any(String.class), any(Pageable.class))).willReturn(page);

		LoadClientJourneyRequestsCriteria criteria = defaultLoadClientJourneyRequestsCriteriaBuilder()
				.clientUsername(TestConstants.DEFAULT_MOBILE_NUMBER_USERNAME).build();
		// when
		ClientJourneyRequests result = journeyRequestsPersistenceAdapter.loadClientJourneyRequests(criteria);
		// then
		assertEquals(page.getTotalPages(), result.getTotalPages());
		assertEquals(page.hasNext(), result.isHasNext());
		assertEquals(page.getSize(), result.getPageSize());
		assertEquals(page.getTotalElements(), result.getTotalElements());
		assertEquals(page.getNumber(), result.getPageNumber());
		assertEquals(page.getContent(), result.getContent());
	}

	@Test
	void givenNullJourneyRequestsPageAndClientMobileNumber_WhenLoadClientJourneyRequests_ThenReturnClientJourneyRequests() {

		// given

		given(journeyRequestRepository.findByCreationDateTimeBetweenAndClient_MobileNumberAndClient_IccValue(
				any(LocalDateTime.class), any(LocalDateTime.class), any(String.class), any(String.class),
				any(String.class), any(Pageable.class))).willReturn(null);
		LoadClientJourneyRequestsCriteria criteria = defaultLoadClientJourneyRequestsCriteriaBuilder()
				.clientUsername(TestConstants.DEFAULT_MOBILE_NUMBER_USERNAME).build();
		// when
		ClientJourneyRequests result = journeyRequestsPersistenceAdapter.loadClientJourneyRequests(criteria);
		// then

		assertEquals(criteria.getPageSize(), result.getPageSize());
		assertEquals(criteria.getPageNumber(), result.getPageNumber());
		assertEquals(0, result.getTotalPages());
		assertEquals(0, result.getTotalElements());

		assertEquals(false, result.isHasNext());
		assertEquals(Collections.<ClientJourneyRequestDto>emptyList(), result.getContent());
	}

	@Test
	void testLoadJourneyRequestByIdAndClientEmail() {

		// given

		ClientJourneyRequestDto clientJourneyRequestDto = defaultClientJourneyRequestDto();
		given(journeyRequestRepository.findByIdAndClient_Email(eq(1L), eq(TestConstants.DEFAULT_EMAIL)))
				.willReturn(Optional.of(clientJourneyRequestDto));
		// when
		Optional<ClientJourneyRequestDto> result = journeyRequestsPersistenceAdapter
				.loadJourneyRequestByIdAndClientEmail(1L, TestConstants.DEFAULT_EMAIL);

		// then
		then(journeyRequestRepository).should(times(1)).findByIdAndClient_Email(eq(1L),
				eq(TestConstants.DEFAULT_EMAIL));
		assertEquals(clientJourneyRequestDto.getId(), result.get().getId());
	}

	@Test
	void testLoadJourneyRequestByIdAndClientMobileNumberAndIcc() {
		// given

		String[] clientMobileNumber = TestConstants.DEFAULT_MOBILE_NUMBER_USERNAME.split("_");
		ClientJourneyRequestDto clientJourneyRequestDto = defaultClientJourneyRequestDto();
		given(journeyRequestRepository.findByIdAndClient_MobileNumberAndClient_IccValue(eq(1L),
				eq(clientMobileNumber[1]), eq(clientMobileNumber[0]))).willReturn(Optional.of(clientJourneyRequestDto));
		// when
		Optional<ClientJourneyRequestDto> result = journeyRequestsPersistenceAdapter
				.loadJourneyRequestByIdAndClientMobileNumberAndIcc(1L, clientMobileNumber[1], clientMobileNumber[0]);

		// then
		then(journeyRequestRepository).should(times(1)).findByIdAndClient_MobileNumberAndClient_IccValue(eq(1L),
				eq(clientMobileNumber[1]), eq(clientMobileNumber[0]));
		assertEquals(clientJourneyRequestDto.getId(), result.get().getId());

	}

	@Test
	void testLoadInexistentJourneyRequestById() {

		// given

		given(journeyRequestRepository.findById(any(Long.class))).willReturn(Optional.ofNullable(null));

		// when
		Optional<CreateJourneyRequestDto> createJourneyRequestDtoOptional = journeyRequestsPersistenceAdapter
				.loadJourneyRequestById(1L);
		// then

		assertTrue(createJourneyRequestDtoOptional.isEmpty());

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

	private Page<JourneyRequestSearchDto> givenNullJourneyRequestsPageByDeparturePlace_RegionIdAndArrivalPlace_RegionIdInAndEngineType_IdInAndDateBetween() {

		given(journeyRequestRepository
				.findByDeparturePlace_RegionIdAndArrivalPlace_RegionIdInAndEngineType_IdInAndDateBetween(
						any(String.class), any(Set.class), any(Set.class), any(LocalDateTime.class),
						any(LocalDateTime.class), any(String.class), any(Pageable.class))).willReturn(null);

		return null;
	}

	private Page<JourneyRequestSearchDto> givenNotNullJourneyRequestsPageByDeparturePlace_RegionIdAndArrivalPlace_RegionIdInAndEngineType_IdInAndDateBetween() {
		Page<JourneyRequestSearchDto> result = createPageFromJourneyRequestSearchDto(
				defaultJourneyRequestSearchDtoList());
		given(journeyRequestRepository
				.findByDeparturePlace_RegionIdAndArrivalPlace_RegionIdInAndEngineType_IdInAndDateBetween(
						any(String.class), any(Set.class), any(Set.class), any(LocalDateTime.class),
						any(LocalDateTime.class), any(String.class), any(Pageable.class))).willReturn(result);

		return result;

	}

	private Page<JourneyRequestSearchDto> givenNullJourneyRequestsPageByDeparturePlace_RegionIdAndEngineType_IdInAndDateBetween() {

		given(journeyRequestRepository.findByDeparturePlace_RegionIdAndEngineType_IdInAndDateBetween(any(String.class),
				any(Set.class), any(LocalDateTime.class), any(LocalDateTime.class), any(String.class),
				any(Pageable.class))).willReturn(null);

		return null;
	}

	private Page<JourneyRequestSearchDto> givenNotNullJourneyRequestsPageByDeparturePlace_RegionIdAndEngineType_IdInAndDateBetween() {
		Page<JourneyRequestSearchDto> result = createPageFromJourneyRequestSearchDto(
				defaultJourneyRequestSearchDtoList());
		given(journeyRequestRepository.findByDeparturePlace_RegionIdAndEngineType_IdInAndDateBetween(any(String.class),
				any(Set.class), any(LocalDateTime.class), any(LocalDateTime.class), any(String.class),
				any(Pageable.class))).willReturn(result);

		return result;

	}

	private Page<JourneyRequestSearchDto> createPageFromJourneyRequestSearchDto(
			List<JourneyRequestSearchDto> journeyRequestSearchDtos) {
		return new Page<JourneyRequestSearchDto>() {

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
			public List<JourneyRequestSearchDto> getContent() {

				return journeyRequestSearchDtos;
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
			public Iterator<JourneyRequestSearchDto> iterator() {

				return journeyRequestSearchDtos.iterator();
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
			public <U> Page<U> map(Function<? super JourneyRequestSearchDto, ? extends U> converter) {

				return null;
			}
		};
	}

	private Page<ClientJourneyRequestDto> createPageFromClientJourneyRequestDto(
			List<ClientJourneyRequestDto> clientJourneyRequestDto) {
		return new Page<ClientJourneyRequestDto>() {

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
			public List<ClientJourneyRequestDto> getContent() {

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
			public Iterator<ClientJourneyRequestDto> iterator() {

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
			public <U> Page<U> map(Function<? super ClientJourneyRequestDto, ? extends U> converter) {
				// TODO Auto-generated method stub
				return null;
			}

		};
	}
}
