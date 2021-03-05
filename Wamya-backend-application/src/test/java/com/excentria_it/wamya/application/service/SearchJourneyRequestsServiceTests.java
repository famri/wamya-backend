package com.excentria_it.wamya.application.service;

import static com.excentria_it.wamya.test.data.common.JourneyRequestTestData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import java.time.ZoneId;
import java.util.stream.Collectors;

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
import com.excentria_it.wamya.application.utils.DateTimeHelper;
import com.excentria_it.wamya.domain.JourneyRequestsSearchOutputResult;
import com.excentria_it.wamya.domain.JourneyRequestsSearchResult;
import com.excentria_it.wamya.domain.SearchJourneyRequestsInput;
import com.excentria_it.wamya.test.data.common.TestConstants;

@ExtendWith(MockitoExtension.class)
public class SearchJourneyRequestsServiceTests {

	@Mock
	private SearchJourneyRequestsPort searchJourneyRequestsPort;

	@Mock
	private DateTimeHelper dateTimeHelper;

	@Spy
	@InjectMocks
	private SearchJourneyRequestsService searchJourneyRequestsService;

	@Test
	void givenSearchJourneyRequestsCommand_WhenSearchJourneyRequests_ThenSearchJourneyRequests() {

		// given

		SearchJourneyRequestsCommandBuilder commandBuilder = defaultSearchJourneyRequestsCommandBuilder();
		SearchJourneyRequestsCommand command = commandBuilder.build();

		ZoneId userZoneId = ZoneId.of("Africa/Tunis");

		JourneyRequestsSearchResult expectedResult = givenSearchJourneyRequestsByDeparturePlaceRegionIdAndArrivalPlaceRegionIdAndEngineTypesAndDateBetween_WillReturnJourneyRequestsSearchResult(
				userZoneId);

		given(dateTimeHelper.findUserZoneId(any(String.class))).willReturn(userZoneId);

		given(dateTimeHelper.userLocalToSystemDateTime(eq(command.getStartDateTime()), eq(userZoneId)))
				.willReturn(command.getStartDateTime().atZone(userZoneId).toInstant());

		given(dateTimeHelper.userLocalToSystemDateTime(eq(command.getEndDateTime()), eq(userZoneId)))
				.willReturn(command.getEndDateTime().atZone(userZoneId).toInstant());

		// when
		JourneyRequestsSearchResult result = searchJourneyRequestsService.searchJourneyRequests(command,
				TestConstants.DEFAULT_EMAIL, "en_US");

		// then
		ArgumentCaptor<SearchJourneyRequestsInput> SearchJourneyRequestsInputCaptor = ArgumentCaptor
				.forClass(SearchJourneyRequestsInput.class);

		then(searchJourneyRequestsPort).should(times(1))
				.searchJourneyRequests(SearchJourneyRequestsInputCaptor.capture());

		assertEquals(command.getDeparturePlaceDepartmentId(),
				SearchJourneyRequestsInputCaptor.getValue().getDeparturePlaceDepartmentId());
		assertEquals(command.getArrivalPlaceDepartmentIds(),
				SearchJourneyRequestsInputCaptor.getValue().getArrivalPlaceDepartmentIds());
		assertTrue(command.getStartDateTime().isEqual(
				SearchJourneyRequestsInputCaptor.getValue().getStartDateTime().atZone(userZoneId).toLocalDateTime()));

		assertTrue(command.getEndDateTime().isEqual(
				SearchJourneyRequestsInputCaptor.getValue().getEndDateTime().atZone(userZoneId).toLocalDateTime()));
		assertEquals(command.getEngineTypes(), SearchJourneyRequestsInputCaptor.getValue().getEngineTypes());
		assertEquals(command.getPageNumber(), SearchJourneyRequestsInputCaptor.getValue().getPageNumber());
		assertEquals(command.getSortingCriterion(), SearchJourneyRequestsInputCaptor.getValue().getSortingCriterion());
		assertEquals("en_US", SearchJourneyRequestsInputCaptor.getValue().getLocale());

		assertEquals(expectedResult.getTotalPages(), result.getTotalPages());
		assertEquals(expectedResult.getTotalElements(), result.getTotalElements());
		assertEquals(expectedResult.getPageNumber(), result.getPageNumber());
		assertEquals(expectedResult.getPageSize(), result.getPageSize());
		assertEquals(expectedResult.isHasNext(), result.isHasNext());
		assertEquals(expectedResult.getContent(), result.getContent());

	}

	private JourneyRequestsSearchResult givenSearchJourneyRequestsByDeparturePlaceRegionIdAndArrivalPlaceRegionIdAndEngineTypesAndDateBetween_WillReturnJourneyRequestsSearchResult(
			ZoneId userZoneId) {
		JourneyRequestsSearchOutputResult searchOutput = defaultJourneyRequestsSearchOutputResult();

		given(searchJourneyRequestsPort.searchJourneyRequests(any(SearchJourneyRequestsInput.class)))
				.willReturn(searchOutput);

		return new JourneyRequestsSearchResult(searchOutput.getTotalPages(), searchOutput.getTotalElements(),
				searchOutput.getPageNumber(), searchOutput.getPageSize(), searchOutput.isHasNext(),
				searchOutput.getContent().stream()
						.map(j -> searchJourneyRequestsService.mapToJourneyRequestSearchDto(j, userZoneId))
						.collect(Collectors.toList()));

	}

}
