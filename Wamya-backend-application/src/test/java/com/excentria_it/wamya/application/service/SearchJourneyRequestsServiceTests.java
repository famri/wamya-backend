package com.excentria_it.wamya.application.service;

import static com.excentria_it.wamya.test.data.common.JourneyRequestTestData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import com.excentria_it.wamya.application.port.in.SearchJourneyRequestsUseCase.SearchJourneyRequestsCommand;
import com.excentria_it.wamya.application.port.in.SearchJourneyRequestsUseCase.SearchJourneyRequestsCommand.SearchJourneyRequestsCommandBuilder;
import com.excentria_it.wamya.application.port.out.SearchJourneyRequestsPort;
import com.excentria_it.wamya.common.SortingCriterion;
import com.excentria_it.wamya.domain.JourneyRequestsSearchResult;

@ExtendWith(MockitoExtension.class)
public class SearchJourneyRequestsServiceTests {

	@Mock
	private SearchJourneyRequestsPort searchJourneyRequestsPort;

	@Spy
	@InjectMocks
	private SearchJourneyRequestsService searchJourneyRequestsService;

	@Test
	void givenArrivalPlaceRegionAgnosticCommand_WhenSearchJourneyRequests_ThenSearchJourneyRequestsByDeparturePlaceRegionIdAndEngineTypesAndDateBetween() {

		// given

		SearchJourneyRequestsCommandBuilder commandBuilder = arrivalPlaceRegionAgnosticSearchJourneyRequestsCommandBuilder();
		SearchJourneyRequestsCommand command = commandBuilder.build();

		JourneyRequestsSearchResult expectedResult = givenSearchJourneyRequestsByDeparturePlaceRegionIdAndEngineTypesAndDateBetween_WillReturnJourneyRequestsSearchResult();
		// when
		JourneyRequestsSearchResult result = searchJourneyRequestsService.searchJourneyRequests(command, "en");
		// then

		ArgumentCaptor<String> departurePlaceRegionIdCaptor = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<Set<Long>> engineTypesCaptor = ArgumentCaptor.forClass(Set.class);
		ArgumentCaptor<LocalDateTime> startDateCaptor = ArgumentCaptor.forClass(LocalDateTime.class);
		ArgumentCaptor<LocalDateTime> endDateCaptor = ArgumentCaptor.forClass(LocalDateTime.class);
		ArgumentCaptor<Integer> pageNumberCaptor = ArgumentCaptor.forClass(Integer.class);
		ArgumentCaptor<Integer> pageSizeCaptor = ArgumentCaptor.forClass(Integer.class);
		ArgumentCaptor<SortingCriterion> sortingCriteriaCaptor = ArgumentCaptor.forClass(SortingCriterion.class);
		ArgumentCaptor<String> localeCaptor = ArgumentCaptor.forClass(String.class);

		then(searchJourneyRequestsPort).should(times(1))
				.searchJourneyRequestsByDeparturePlaceRegionIdAndEngineTypesAndDateBetween(
						departurePlaceRegionIdCaptor.capture(), engineTypesCaptor.capture(), startDateCaptor.capture(),
						endDateCaptor.capture(), localeCaptor.capture(), pageNumberCaptor.capture(),
						pageSizeCaptor.capture(), sortingCriteriaCaptor.capture());

		assertEquals(command.getDeparturePlaceRegionId(), departurePlaceRegionIdCaptor.getValue());

		assertEquals(command.getEngineTypes(), engineTypesCaptor.getValue());
		assertEquals(command.getStartDateTime(), startDateCaptor.getValue());
		assertEquals(command.getEndDateTime(), endDateCaptor.getValue());
		assertEquals(command.getPageNumber(), pageNumberCaptor.getValue());
		assertEquals(command.getPageSize(), pageSizeCaptor.getValue());
		assertEquals(command.getSortingCriterion(), sortingCriteriaCaptor.getValue());
		assertEquals("en", localeCaptor.getValue());

		assertEquals(expectedResult.getTotalPages(), result.getTotalPages());
		assertEquals(expectedResult.getTotalElements(), result.getTotalElements());
		assertEquals(expectedResult.getPageNumber(), result.getPageNumber());
		assertEquals(expectedResult.getPageSize(), result.getPageSize());
		assertEquals(expectedResult.isHasNext(), result.isHasNext());
		assertEquals(expectedResult.getContent(), result.getContent());

	}

	@Test
	void givenSearchJourneyRequestsCommandWithArrivalPlaceRegionId_WhenSearchJourneyRequests_ThenSearchJourneyRequestsByDeparturePlaceRegionIdAndArrivalPlaceRegionIdAndEngineTypesAndDateBetween() {

		// given

		SearchJourneyRequestsCommandBuilder commandBuilder = defaultSearchJourneyRequestsCommandBuilder();
		SearchJourneyRequestsCommand command = commandBuilder.build();
		JourneyRequestsSearchResult expectedResult = givenSearchJourneyRequestsByDeparturePlaceRegionIdAndArrivalPlaceRegionIdAndEngineTypesAndDateBetween_WillReturnJourneyRequestsSearchResult();
		// when
		JourneyRequestsSearchResult result = searchJourneyRequestsService.searchJourneyRequests(command, "en");
		// then

		ArgumentCaptor<String> departurePlaceRegionIdCaptor = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<Set<Long>> engineTypesCaptor = ArgumentCaptor.forClass(Set.class);
		ArgumentCaptor<LocalDateTime> startDateCaptor = ArgumentCaptor.forClass(LocalDateTime.class);
		ArgumentCaptor<LocalDateTime> endDateCaptor = ArgumentCaptor.forClass(LocalDateTime.class);
		ArgumentCaptor<Integer> pageNumberCaptor = ArgumentCaptor.forClass(Integer.class);
		ArgumentCaptor<Integer> pageSizeCaptor = ArgumentCaptor.forClass(Integer.class);
		ArgumentCaptor<SortingCriterion> sortingCriteriaCaptor = ArgumentCaptor.forClass(SortingCriterion.class);
		ArgumentCaptor<Set<String>> arrivalPlaceRegionIdCaptor = ArgumentCaptor.forClass(Set.class);
		ArgumentCaptor<String> localeCaptor = ArgumentCaptor.forClass(String.class);

		then(searchJourneyRequestsPort).should(times(1))
				.searchJourneyRequestsByDeparturePlaceRegionIdAndArrivalPlaceRegionIdAndEngineTypesAndDateBetween(
						departurePlaceRegionIdCaptor.capture(), arrivalPlaceRegionIdCaptor.capture(),
						engineTypesCaptor.capture(), startDateCaptor.capture(), endDateCaptor.capture(),
						localeCaptor.capture(), pageNumberCaptor.capture(), pageSizeCaptor.capture(),
						sortingCriteriaCaptor.capture());

		assertEquals(command.getDeparturePlaceRegionId(), departurePlaceRegionIdCaptor.getValue());
		assertEquals(command.getArrivalPlaceRegionIds(), arrivalPlaceRegionIdCaptor.getValue());
		assertEquals(command.getEngineTypes(), engineTypesCaptor.getValue());
		assertEquals(command.getStartDateTime(), startDateCaptor.getValue());
		assertEquals(command.getEndDateTime(), endDateCaptor.getValue());
		assertEquals(command.getPageNumber(), pageNumberCaptor.getValue());
		assertEquals(command.getPageSize(), pageSizeCaptor.getValue());
		assertEquals(command.getSortingCriterion(), sortingCriteriaCaptor.getValue());
		assertEquals("en", localeCaptor.getValue());

		assertEquals(expectedResult.getTotalPages(), result.getTotalPages());
		assertEquals(expectedResult.getTotalElements(), result.getTotalElements());
		assertEquals(expectedResult.getPageNumber(), result.getPageNumber());
		assertEquals(expectedResult.getPageSize(), result.getPageSize());
		assertEquals(expectedResult.isHasNext(), result.isHasNext());
		assertEquals(expectedResult.getContent(), result.getContent());

	}

	private JourneyRequestsSearchResult givenSearchJourneyRequestsByDeparturePlaceRegionIdAndArrivalPlaceRegionIdAndEngineTypesAndDateBetween_WillReturnJourneyRequestsSearchResult() {
		JourneyRequestsSearchResult result = defaultJourneyRequestsSearchResult();

		given(searchJourneyRequestsPort
				.searchJourneyRequestsByDeparturePlaceRegionIdAndArrivalPlaceRegionIdAndEngineTypesAndDateBetween(
						any(String.class), any(Set.class), any(Set.class), any(LocalDateTime.class),
						any(LocalDateTime.class), any(String.class), any(Integer.class), any(Integer.class),
						any(SortingCriterion.class))).willReturn(result);
		return result;
	}

	private JourneyRequestsSearchResult givenSearchJourneyRequestsByDeparturePlaceRegionIdAndEngineTypesAndDateBetween_WillReturnJourneyRequestsSearchResult() {
		JourneyRequestsSearchResult result = defaultJourneyRequestsSearchResult();

		given(searchJourneyRequestsPort.searchJourneyRequestsByDeparturePlaceRegionIdAndEngineTypesAndDateBetween(
				any(String.class), any(Set.class), any(LocalDateTime.class), any(LocalDateTime.class),
				any(String.class), any(Integer.class), any(Integer.class), any(SortingCriterion.class)))
						.willReturn(result);
		return result;

	}

}
