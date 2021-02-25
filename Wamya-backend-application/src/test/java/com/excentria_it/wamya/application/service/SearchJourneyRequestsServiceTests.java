package com.excentria_it.wamya.application.service;

import static com.excentria_it.wamya.test.data.common.JourneyRequestTestData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import java.time.ZoneOffset;

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
import com.excentria_it.wamya.domain.JourneyRequestsSearchResult;
import com.excentria_it.wamya.domain.SearchJourneyRequestsCriteria;

@ExtendWith(MockitoExtension.class)
public class SearchJourneyRequestsServiceTests {

	@Mock
	private SearchJourneyRequestsPort searchJourneyRequestsPort;

	@Spy
	@InjectMocks
	private SearchJourneyRequestsService searchJourneyRequestsService;

	@Test
	void givenSearchJourneyRequestsCommand_WhenSearchJourneyRequests_ThenSearchJourneyRequests() {

		// given

		SearchJourneyRequestsCommandBuilder commandBuilder = defaultSearchJourneyRequestsCommandBuilder();
		SearchJourneyRequestsCommand command = commandBuilder.build();
		JourneyRequestsSearchResult expectedResult = givenSearchJourneyRequestsByDeparturePlaceRegionIdAndArrivalPlaceRegionIdAndEngineTypesAndDateBetween_WillReturnJourneyRequestsSearchResult();

		// when
		JourneyRequestsSearchResult result = searchJourneyRequestsService.searchJourneyRequests(command, "en_US");

		// then
		ArgumentCaptor<SearchJourneyRequestsCriteria> searchJourneyRequestsCriteriaCaptor = ArgumentCaptor
				.forClass(SearchJourneyRequestsCriteria.class);

		then(searchJourneyRequestsPort).should(times(1))
				.searchJourneyRequests(searchJourneyRequestsCriteriaCaptor.capture());

		assertEquals(command.getDeparturePlaceDepartmentId(),
				searchJourneyRequestsCriteriaCaptor.getValue().getDeparturePlaceDepartmentId());
		assertEquals(command.getArrivalPlaceDepartmentIds(),
				searchJourneyRequestsCriteriaCaptor.getValue().getArrivalPlaceDepartmentIds());
		assertTrue(command.getStartDateTime()
				.isEqual(searchJourneyRequestsCriteriaCaptor.getValue().getStartDateTime().atZone(ZoneOffset.UTC)));

		assertTrue(command.getEndDateTime()
				.isEqual(searchJourneyRequestsCriteriaCaptor.getValue().getEndDateTime().atZone(ZoneOffset.UTC)));
		assertEquals(command.getEngineTypes(), searchJourneyRequestsCriteriaCaptor.getValue().getEngineTypes());
		assertEquals(command.getPageNumber(), searchJourneyRequestsCriteriaCaptor.getValue().getPageNumber());
		assertEquals(command.getSortingCriterion(),
				searchJourneyRequestsCriteriaCaptor.getValue().getSortingCriterion());
		assertEquals("en_US", searchJourneyRequestsCriteriaCaptor.getValue().getLocale());

		assertEquals(expectedResult.getTotalPages(), result.getTotalPages());
		assertEquals(expectedResult.getTotalElements(), result.getTotalElements());
		assertEquals(expectedResult.getPageNumber(), result.getPageNumber());
		assertEquals(expectedResult.getPageSize(), result.getPageSize());
		assertEquals(expectedResult.isHasNext(), result.isHasNext());
		assertEquals(expectedResult.getContent(), result.getContent());

	}

	private JourneyRequestsSearchResult givenSearchJourneyRequestsByDeparturePlaceRegionIdAndArrivalPlaceRegionIdAndEngineTypesAndDateBetween_WillReturnJourneyRequestsSearchResult() {
		JourneyRequestsSearchResult result = defaultJourneyRequestsSearchResult();

		given(searchJourneyRequestsPort.searchJourneyRequests(any(SearchJourneyRequestsCriteria.class)))
				.willReturn(result);
		return result;
	}

}
