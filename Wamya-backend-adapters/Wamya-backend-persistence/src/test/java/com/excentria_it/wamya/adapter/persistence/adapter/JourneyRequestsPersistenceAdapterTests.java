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

import com.excentria_it.wamya.adapter.persistence.entity.EngineTypeJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.JourneyRequestJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.PlaceJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.UserAccountJpaEntity;
import com.excentria_it.wamya.adapter.persistence.mapper.JourneyRequestMapper;
import com.excentria_it.wamya.adapter.persistence.mapper.PlaceMapper;
import com.excentria_it.wamya.adapter.persistence.repository.EngineTypeRepository;
import com.excentria_it.wamya.adapter.persistence.repository.JourneyRequestRepository;
import com.excentria_it.wamya.adapter.persistence.repository.PlaceRepository;
import com.excentria_it.wamya.adapter.persistence.repository.UserAccountRepository;
import com.excentria_it.wamya.common.SortingCriterion;
import com.excentria_it.wamya.domain.JourneyRequest;
import com.excentria_it.wamya.domain.JourneyRequestSearchDto;
import com.excentria_it.wamya.domain.JourneyRequestsSearchResult;
import com.excentria_it.wamya.domain.SearchJourneyRequestsCriteria;
import com.excentria_it.wamya.test.data.common.TestConstants;

@ExtendWith(MockitoExtension.class)
public class JourneyRequestsPersistenceAdapterTests {
	@Mock
	private JourneyRequestRepository journeyRequestRepository;
	@Mock
	private EngineTypeRepository engineTypeRepository;
	@Mock
	private UserAccountRepository userAccountRepository;
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
		assertEquals(0, result.getPageNumber());
		assertEquals(0, result.getPageSize());
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
		assertEquals(0, result.getPageNumber());
		assertEquals(0, result.getPageSize());
		assertEquals(false, result.isHasNext());
		assertEquals(Collections.<JourneyRequestSearchDto>emptyList(), result.getContent());

	}

	@Test
	void givenExistentDeparturePlaceAndExistentArrivalPlace_WhencreateJourneyRequest_ThenSaveJourneyRequestJpaEntity() {

		// given
		JourneyRequest journeyRequest = defaultJourneyRequest();

		EngineTypeJpaEntity engineTypeJpaEntity = defaultEngineTypeJpaEntity();
		given(engineTypeRepository.findById(journeyRequest.getEngineType().getId()))
				.willReturn(Optional.of(engineTypeJpaEntity));

		UserAccountJpaEntity userAccountJpaEntity = defaultExistingNotTransporterUserAccountJpaEntity();
		given(userAccountRepository.findByEmail(TestConstants.DEFAULT_EMAIL))
				.willReturn(Optional.of(userAccountJpaEntity));

		PlaceJpaEntity departurePlaceJpaEntity = defaultDeparturePlaceJpaEntity();
		given(placeRepository.findById(journeyRequest.getDeparturePlace().getPlaceId()))
				.willReturn(Optional.of(departurePlaceJpaEntity));

		PlaceJpaEntity arrivalPlaceJpaEntity = defaultArrivalPlaceJpaEntity();
		given(placeRepository.findById(journeyRequest.getArrivalPlace().getPlaceId()))
				.willReturn(Optional.of(arrivalPlaceJpaEntity));

		JourneyRequestJpaEntity journeyRequestJpaEntity = defaultNewJourneyRequestJpaEntity();
		given(journeyRequestMapper.mapToJpaEntity(journeyRequest, departurePlaceJpaEntity, arrivalPlaceJpaEntity,
				engineTypeJpaEntity, userAccountJpaEntity, null)).willReturn(journeyRequestJpaEntity);

		given(journeyRequestRepository.save(journeyRequestJpaEntity)).willReturn(journeyRequestJpaEntity);

		// when
		journeyRequestsPersistenceAdapter.createJourneyRequest(journeyRequest, TestConstants.DEFAULT_EMAIL);

		// then
		then(journeyRequestRepository).should(times(1)).save(journeyRequestJpaEntity);
	}

	@Test
	void givenNonExistentDeparturePlaceAndNonExistentArrivalPlace_WhencreateJourneyRequest_ThenSaveJourneyRequestJpaEntity() {

		// given
		JourneyRequest journeyRequest = defaultJourneyRequest();

		EngineTypeJpaEntity engineTypeJpaEntity = defaultEngineTypeJpaEntity();
		given(engineTypeRepository.findById(journeyRequest.getEngineType().getId()))
				.willReturn(Optional.of(engineTypeJpaEntity));

		UserAccountJpaEntity userAccountJpaEntity = defaultExistingNotTransporterUserAccountJpaEntity();
		given(userAccountRepository.findByEmail(TestConstants.DEFAULT_EMAIL))
				.willReturn(Optional.of(userAccountJpaEntity));

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

		JourneyRequestJpaEntity journeyRequestJpaEntity = defaultNewJourneyRequestJpaEntity();
		given(journeyRequestMapper.mapToJpaEntity(journeyRequest, departurePlaceJpaEntity, arrivalPlaceJpaEntity,
				engineTypeJpaEntity, userAccountJpaEntity, null)).willReturn(journeyRequestJpaEntity);

		given(journeyRequestRepository.save(journeyRequestJpaEntity)).willReturn(journeyRequestJpaEntity);

		// when
		journeyRequestsPersistenceAdapter.createJourneyRequest(journeyRequest, TestConstants.DEFAULT_EMAIL);

		// then
		then(journeyRequestRepository).should(times(1)).save(journeyRequestJpaEntity);
	}

	@Test
	void givenExistentUserAccountByPhoneNumber_WhencreateJourneyRequest_ThenSaveJourneyRequestJpaEntity() {

		// given
		JourneyRequest journeyRequest = defaultJourneyRequest();
		

		EngineTypeJpaEntity engineTypeJpaEntity = defaultEngineTypeJpaEntity();
		given(engineTypeRepository.findById(journeyRequest.getEngineType().getId()))
				.willReturn(Optional.of(engineTypeJpaEntity));

		UserAccountJpaEntity userAccountJpaEntity = defaultExistingNotTransporterUserAccountJpaEntity();
		given(userAccountRepository.findByEmail(TestConstants.DEFAULT_MOBILE_NUMBER_USERNAME))
				.willReturn(Optional.ofNullable(null));

		given(userAccountRepository.findByMobilePhoneNumber(any(String.class), any(String.class)))
				.willReturn(Optional.of(userAccountJpaEntity));

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

		JourneyRequestJpaEntity journeyRequestJpaEntity = defaultNewJourneyRequestJpaEntity();
		given(journeyRequestMapper.mapToJpaEntity(journeyRequest, departurePlaceJpaEntity, arrivalPlaceJpaEntity,
				engineTypeJpaEntity, userAccountJpaEntity, null)).willReturn(journeyRequestJpaEntity);

		given(journeyRequestRepository.save(journeyRequestJpaEntity)).willReturn(journeyRequestJpaEntity);

		// when
		journeyRequestsPersistenceAdapter.createJourneyRequest(journeyRequest,
				TestConstants.DEFAULT_MOBILE_NUMBER_USERNAME);

		// then
		then(journeyRequestRepository).should(times(1)).save(journeyRequestJpaEntity);
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

	@Test
	void testStripDashes() {
		String str1 = "-minprice";
		String res1 = journeyRequestsPersistenceAdapter.stripDashes(str1);
		assertEquals("Minprice", res1);

		String str2 = "minprice-";
		String res2 = journeyRequestsPersistenceAdapter.stripDashes(str2);
		assertEquals("minprice", res2);

		String str3 = "min-price";
		String res3 = journeyRequestsPersistenceAdapter.stripDashes(str3);
		assertEquals("minPrice", res3);

		String str4 = "-min-p-ric-e-";
		String res4 = journeyRequestsPersistenceAdapter.stripDashes(str4);
		assertEquals("MinPRicE", res4);

	}

	@Test
	void testConvertToSort() {

		Sort sort1 = journeyRequestsPersistenceAdapter.convertToSort(new SortingCriterion("min-price", "desc"));
		Sort sort2 = journeyRequestsPersistenceAdapter.convertToSort(new SortingCriterion("date-time", "asc"));
		Sort sort3 = journeyRequestsPersistenceAdapter.convertToSort(new SortingCriterion("distance", "asc"));

		assertEquals(sort1.get().toArray().length, 1);
		assertTrue(sort1.getOrderFor("(minPrice)") != null
				&& sort1.getOrderFor("(minPrice)").getDirection().isDescending());

		assertEquals(sort2.get().toArray().length, 1);
		assertTrue(sort2.getOrderFor("dateTime") != null && sort2.getOrderFor("dateTime").getDirection().isAscending());

		assertEquals(sort3.get().toArray().length, 1);
		assertTrue(sort3.getOrderFor("distance") != null && sort3.getOrderFor("distance").getDirection().isAscending());
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
}
