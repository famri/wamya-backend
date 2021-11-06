package com.excentria_it.wamya.application.service;

import static com.excentria_it.wamya.test.data.common.JourneyRequestTestData.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import com.excentria_it.wamya.application.port.in.LoadClientJourneyRequestsUseCase.LoadJourneyRequestCommand;
import com.excentria_it.wamya.application.port.in.LoadClientJourneyRequestsUseCase.LoadJourneyRequestsCommand;
import com.excentria_it.wamya.application.port.out.LoadClientJourneyRequestsPort;
import com.excentria_it.wamya.application.port.out.LoadJourneyRequestPort;
import com.excentria_it.wamya.application.utils.DateTimeHelper;
import com.excentria_it.wamya.common.exception.JourneyRequestNotFoundException;
import com.excentria_it.wamya.domain.ClientJourneyRequestDto;
import com.excentria_it.wamya.domain.ClientJourneyRequestDtoOutput;
import com.excentria_it.wamya.domain.ClientJourneyRequestsOutput;
import com.excentria_it.wamya.domain.LoadClientJourneyRequestsCriteria;

@ExtendWith(MockitoExtension.class)
public class LoadJourneyRequestsServiceTests {

	@Mock
	private LoadClientJourneyRequestsPort loadClientJourneyRequestsPort;

	@Mock
	private LoadJourneyRequestPort loadJourneyRequestPort;

	@Mock
	private DateTimeHelper dateTimeHelper;

	@Spy
	@InjectMocks
	private LoadJourneyRequestsService loadJourneyRequestsService;

	@Test
	void givenLoadJourneyRequestsCommand_WhenLoadJourneyRequests_Then_Succeed() {
		// given
		LoadJourneyRequestsCommand command = defaultLoadJourneyRequestsCommandBuilder().build();
		ClientJourneyRequestsOutput clientJourneyRequests = defaultClientJourneyRequestsOutput();
		// when

		ArgumentCaptor<LoadClientJourneyRequestsCriteria> criteriaCaptor = ArgumentCaptor
				.forClass(LoadClientJourneyRequestsCriteria.class);

		given(loadClientJourneyRequestsPort.loadClientJourneyRequests(any(LoadClientJourneyRequestsCriteria.class)))
				.willReturn(clientJourneyRequests);
		given(dateTimeHelper.findUserZoneId(command.getClientUsername())).willReturn(ZoneId.of("Africa/Tunis"));
		// when
		loadJourneyRequestsService.loadJourneyRequests(command, "en_US");

		// then
		then(loadClientJourneyRequestsPort).should(times(1)).loadClientJourneyRequests(criteriaCaptor.capture());

		assertThat(criteriaCaptor.getValue().getClientUsername()).isEqualTo(command.getClientUsername());
		assertThat(criteriaCaptor.getValue().getPeriodCriterion()).isEqualTo(command.getPeriodCriterion());
		assertThat(criteriaCaptor.getValue().getSortingCriterion()).isEqualTo(command.getSortingCriterion());
		assertThat(criteriaCaptor.getValue().getPageNumber()).isEqualTo(command.getPageNumber());
		assertThat(criteriaCaptor.getValue().getPageSize()).isEqualTo(command.getPageSize());
		assertThat(criteriaCaptor.getValue().getLocale()).isEqualTo("en_US");
	}

	@Test
	void givenLoadJourneyRequestCommand_WhenLoadJourneyRequest_Then_Succeed() {

		// given
		LoadJourneyRequestCommand command = defaultLoadJourneyRequestCommandBuilder().build();
		ClientJourneyRequestDtoOutput clientJourneyRequestDtoOutput = defaultClientJourneyRequestDtoOutput();
		// when

		given(loadJourneyRequestPort.loadJourneyRequestByIdAndClientEmail(command.getJourneyRequestId(),
				command.getClientUsername(), "en_US")).willReturn(Optional.of(clientJourneyRequestDtoOutput));

		ZonedDateTime userDateTimeZoned = clientJourneyRequestDtoOutput.getDateTime().atZone(ZoneId.of("Africa/Tunis"));

		given(dateTimeHelper.findUserZoneId(command.getClientUsername())).willReturn(ZoneId.of("Africa/Tunis"));

		given(dateTimeHelper.systemToUserLocalDateTime(clientJourneyRequestDtoOutput.getDateTime(),
				ZoneId.of("Africa/Tunis"))).willReturn(userDateTimeZoned.toLocalDateTime());

		// when
		ClientJourneyRequestDto result = loadJourneyRequestsService.loadJourneyRequest(command, "en_US");

		// then
		then(loadJourneyRequestPort).should(times(1)).loadJourneyRequestByIdAndClientEmail(
				eq(command.getJourneyRequestId()), eq(command.getClientUsername()), eq("en_US"));

		assertEquals(clientJourneyRequestDtoOutput.getId(), result.getId());
		assertEquals(clientJourneyRequestDtoOutput.getDeparturePlace().getId(), result.getDeparturePlace().getId());
		assertEquals(clientJourneyRequestDtoOutput.getDeparturePlace().getType(), result.getDeparturePlace().getType());
		assertEquals(clientJourneyRequestDtoOutput.getDeparturePlace().getName(), result.getDeparturePlace().getName());
		assertEquals(clientJourneyRequestDtoOutput.getDeparturePlace().getLatitude(),
				result.getDeparturePlace().getLatitude());
		assertEquals(clientJourneyRequestDtoOutput.getDeparturePlace().getLongitude(),
				result.getDeparturePlace().getLongitude());
		assertEquals(clientJourneyRequestDtoOutput.getDeparturePlace().getDepartmentId(),
				result.getDeparturePlace().getDepartmentId());

		assertEquals(clientJourneyRequestDtoOutput.getArrivalPlace().getId(), result.getArrivalPlace().getId());
		assertEquals(clientJourneyRequestDtoOutput.getArrivalPlace().getType(), result.getArrivalPlace().getType());
		assertEquals(clientJourneyRequestDtoOutput.getArrivalPlace().getName(), result.getArrivalPlace().getName());
		assertEquals(clientJourneyRequestDtoOutput.getArrivalPlace().getLatitude(),
				result.getArrivalPlace().getLatitude());
		assertEquals(clientJourneyRequestDtoOutput.getArrivalPlace().getLongitude(),
				result.getArrivalPlace().getLongitude());
		assertEquals(clientJourneyRequestDtoOutput.getArrivalPlace().getDepartmentId(),
				result.getArrivalPlace().getDepartmentId());

		assertEquals(clientJourneyRequestDtoOutput.getEngineType().getId(), result.getEngineType().getId());
		assertEquals(clientJourneyRequestDtoOutput.getEngineType().getName(), result.getEngineType().getName());
		assertEquals(clientJourneyRequestDtoOutput.getEngineType().getCode(), result.getEngineType().getCode());

		assertEquals(clientJourneyRequestDtoOutput.getDistance(), result.getDistance());
		assertEquals(clientJourneyRequestDtoOutput.getCreationDateTime(), result.getCreationDateTime());
		assertEquals(userDateTimeZoned.toLocalDateTime(), result.getDateTime());
		assertEquals(clientJourneyRequestDtoOutput.getWorkers(), result.getWorkers());
		assertEquals(clientJourneyRequestDtoOutput.getDescription(), result.getDescription());
		assertEquals(clientJourneyRequestDtoOutput.getProposalsCount(), result.getProposalsCount());

	}

	@Test
	void givenJourneyRequestNotFound_WhenLoadJourneyRequest_Then_ThrowJourneyRequestNotFoundException() {

		// given
		LoadJourneyRequestCommand command = defaultLoadJourneyRequestCommandBuilder().build();

		// when

		given(loadJourneyRequestPort.loadJourneyRequestByIdAndClientEmail(command.getJourneyRequestId(),
				command.getClientUsername(), "en_US")).willReturn(Optional.empty());

		// when // then
		assertThrows(JourneyRequestNotFoundException.class,
				() -> loadJourneyRequestsService.loadJourneyRequest(command, "en_US"));

	}
}
