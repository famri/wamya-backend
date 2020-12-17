package com.excentria_it.wamya.adapter.persistence.adapter;

import static com.excentria_it.wamya.test.data.common.SearchJourneyRequestsTestData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.excentria_it.wamya.adapter.persistence.repository.JourneyRequestRepository;
import com.excentria_it.wamya.application.port.in.SearchJourneyRequestsUseCase.SearchJourneyRequestsCommand;
import com.excentria_it.wamya.common.SortingCriterion;
import com.excentria_it.wamya.domain.JourneyRequestSearchDto;
import com.excentria_it.wamya.domain.JourneyRequestsSearchResult;

@ExtendWith(MockitoExtension.class)
public class JourneyRequestsPersistenceAdapterTests {
	@Mock
	private JourneyRequestRepository journeyRequestRepository;

	@InjectMocks
	private JourneyRequestsPersistenceAdapter journeyRequestsPersistenceAdapter;

	@Test
	void givenNotNullJourneyRequestsPage_WhenSearchJourneyRequestsByDeparturePlaceRegionIdAndArrivalPlaceRegionIdAndEngineTypesAndDateBetween_ThenReturnJourneyRequestsSearchResult() {

		// given
		SearchJourneyRequestsCommand command = defaultSearchJourneyRequestsCommandBuilder().build();

		Page<JourneyRequestSearchDto> expectedResult = givenNotNullJourneyRequestsPageByDeparturePlace_RegionIdAndArrivalPlace_RegionIdInAndEngineType_IdInAndDateBetween();

		// when

		JourneyRequestsSearchResult result = journeyRequestsPersistenceAdapter
				.searchJourneyRequestsByDeparturePlaceRegionIdAndArrivalPlaceRegionIdAndEngineTypesAndDateBetween(
						command.getDeparturePlaceRegionId(), command.getArrivalPlaceRegionIds(),
						command.getEngineTypes(), command.getStartDateTime(), command.getEndDateTime(),
						command.getPageNumber(), command.getPageSize(), command.getSortingCriterion());
		// then

		assertEquals(expectedResult.getTotalPages(), result.getTotalPages());
		assertEquals(expectedResult.getTotalElements(), result.getTotalElements());
		assertEquals(expectedResult.getNumber(), result.getPageNumber());
		assertEquals(expectedResult.getSize(), result.getPageSize());
		assertEquals(expectedResult.hasNext(), result.isHasNext());
		assertEquals(expectedResult.getContent(), result.getContent());

	}

	@Test
	void givenNullJourneyRequestsPage_WhenSearchJourneyRequestsByDeparturePlaceRegionIdAndArrivalPlaceRegionIdAndEngineTypesAndDateBetween_ThenReturnEmptyJourneyRequestsSearchResult() {

		// given
		SearchJourneyRequestsCommand command = defaultSearchJourneyRequestsCommandBuilder().build();

		givenNullJourneyRequestsPageByDeparturePlace_RegionIdAndArrivalPlace_RegionIdInAndEngineType_IdInAndDateBetween();

		// when

		JourneyRequestsSearchResult result = journeyRequestsPersistenceAdapter
				.searchJourneyRequestsByDeparturePlaceRegionIdAndArrivalPlaceRegionIdAndEngineTypesAndDateBetween(
						command.getDeparturePlaceRegionId(), command.getArrivalPlaceRegionIds(),
						command.getEngineTypes(), command.getStartDateTime(), command.getEndDateTime(),
						command.getPageNumber(), command.getPageSize(), command.getSortingCriterion());
		// then

		assertEquals(0, result.getTotalPages());
		assertEquals(0, result.getTotalElements());
		assertEquals(0, result.getPageNumber());
		assertEquals(0, result.getPageSize());
		assertEquals(false, result.isHasNext());
		assertEquals(Collections.<JourneyRequestSearchDto>emptyList(), result.getContent());

	}

	@Test
	void givenNotNullJourneyRequestsPage_WhenSearchJourneyRequestsByDeparturePlaceRegionIdAndEngineTypesAndDateBetween_ThenReturnJourneyRequestsSearchResult() {

		// given
		SearchJourneyRequestsCommand command = defaultSearchJourneyRequestsCommandBuilder().build();

		Page<JourneyRequestSearchDto> expectedResult = givenNotNullJourneyRequestsPageByDeparturePlace_RegionIdAndEngineType_IdInAndDateBetween();

		// when

		JourneyRequestsSearchResult result = journeyRequestsPersistenceAdapter
				.searchJourneyRequestsByDeparturePlaceRegionIdAndEngineTypesAndDateBetween(
						command.getDeparturePlaceRegionId(), command.getEngineTypes(), command.getStartDateTime(),
						command.getEndDateTime(), command.getPageNumber(), command.getPageSize(),
						command.getSortingCriterion());
		// then

		assertEquals(expectedResult.getTotalPages(), result.getTotalPages());
		assertEquals(expectedResult.getTotalElements(), result.getTotalElements());
		assertEquals(expectedResult.getNumber(), result.getPageNumber());
		assertEquals(expectedResult.getSize(), result.getPageSize());
		assertEquals(expectedResult.hasNext(), result.isHasNext());
		assertEquals(expectedResult.getContent(), result.getContent());

	}

	@Test
	void givenNullJourneyRequestsPage_WhenSearchJourneyRequestsByDeparturePlaceRegionIdAndEngineTypesAndDateBetween_ThenReturnEmptyJourneyRequestsSearchResult() {

		// given
		SearchJourneyRequestsCommand command = defaultSearchJourneyRequestsCommandBuilder().build();

		givenNullJourneyRequestsPageByDeparturePlace_RegionIdAndEngineType_IdInAndDateBetween();

		// when

		JourneyRequestsSearchResult result = journeyRequestsPersistenceAdapter
				.searchJourneyRequestsByDeparturePlaceRegionIdAndEngineTypesAndDateBetween(
						command.getDeparturePlaceRegionId(), command.getEngineTypes(), command.getStartDateTime(),
						command.getEndDateTime(), command.getPageNumber(), command.getPageSize(),
						command.getSortingCriterion());
		// then

		assertEquals(0, result.getTotalPages());
		assertEquals(0, result.getTotalElements());
		assertEquals(0, result.getPageNumber());
		assertEquals(0, result.getPageSize());
		assertEquals(false, result.isHasNext());
		assertEquals(Collections.<JourneyRequestSearchDto>emptyList(), result.getContent());

	}

	private Page<JourneyRequestSearchDto> givenNullJourneyRequestsPageByDeparturePlace_RegionIdAndArrivalPlace_RegionIdInAndEngineType_IdInAndDateBetween() {

		given(journeyRequestRepository
				.findByDeparturePlace_RegionIdAndArrivalPlace_RegionIdInAndEngineType_IdInAndDateBetween(
						any(String.class), any(Set.class), any(Set.class), any(LocalDateTime.class),
						any(LocalDateTime.class), any(Pageable.class))).willReturn(null);

		return null;
	}

	private Page<JourneyRequestSearchDto> givenNotNullJourneyRequestsPageByDeparturePlace_RegionIdAndArrivalPlace_RegionIdInAndEngineType_IdInAndDateBetween() {
		Page<JourneyRequestSearchDto> result = defaultJourneyRequestSearchDtoPage();
		given(journeyRequestRepository
				.findByDeparturePlace_RegionIdAndArrivalPlace_RegionIdInAndEngineType_IdInAndDateBetween(
						any(String.class), any(Set.class), any(Set.class), any(LocalDateTime.class),
						any(LocalDateTime.class), any(Pageable.class)))
								.willReturn(defaultJourneyRequestSearchDtoPage());

		return result;

	}

	private Page<JourneyRequestSearchDto> givenNullJourneyRequestsPageByDeparturePlace_RegionIdAndEngineType_IdInAndDateBetween() {

		given(journeyRequestRepository.findByDeparturePlace_RegionIdAndEngineType_IdInAndDateBetween(any(String.class),
				any(Set.class), any(LocalDateTime.class), any(LocalDateTime.class), any(Pageable.class)))
						.willReturn(null);

		return null;
	}

	private Page<JourneyRequestSearchDto> givenNotNullJourneyRequestsPageByDeparturePlace_RegionIdAndEngineType_IdInAndDateBetween() {
		Page<JourneyRequestSearchDto> result = defaultJourneyRequestSearchDtoPage();
		given(journeyRequestRepository.findByDeparturePlace_RegionIdAndEngineType_IdInAndDateBetween(any(String.class),
				any(Set.class), any(LocalDateTime.class), any(LocalDateTime.class), any(Pageable.class)))
						.willReturn(defaultJourneyRequestSearchDtoPage());

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
}
