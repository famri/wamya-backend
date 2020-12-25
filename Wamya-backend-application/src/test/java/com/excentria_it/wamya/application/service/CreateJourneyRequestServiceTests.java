package com.excentria_it.wamya.application.service;

import static com.excentria_it.wamya.test.data.common.JourneyRequestTestData.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import com.excentria_it.wamya.application.port.in.CreateJourneyRequestUseCase.CreateJourneyRequestCommand;
import com.excentria_it.wamya.application.port.in.CreateJourneyRequestUseCase.CreateJourneyRequestCommand.CreateJourneyRequestCommandBuilder;
import com.excentria_it.wamya.application.port.out.CreateJourneyRequestPort;
import com.excentria_it.wamya.domain.JourneyRequest;
import com.excentria_it.wamya.test.data.common.TestConstants;

@ExtendWith(MockitoExtension.class)
public class CreateJourneyRequestServiceTests {

	@Mock
	private CreateJourneyRequestPort createJourneyRequestPort;

	
	@Spy
	@InjectMocks
	private CreateJourneyRequestService createJourneyRequestsService;

	@Test
	void givenCreateJourneyRequestCommand_WhenCreateJourneyRequest_ThenSucceed() {
		// given
		CreateJourneyRequestCommandBuilder commandBuilder = defaultCreateJourneyRequestCommandBuilder();
		CreateJourneyRequestCommand command = commandBuilder.build();

		ArgumentCaptor<JourneyRequest> journeyRequestCaptor = ArgumentCaptor.forClass(JourneyRequest.class);
		
		// when
		createJourneyRequestsService.createJourneyRequest(command, TestConstants.DEFAULT_EMAIL);

		// then
		then(createJourneyRequestPort).should(times(1)).createJourneyRequest(journeyRequestCaptor.capture(),eq(TestConstants.DEFAULT_EMAIL));

		assertThat(journeyRequestCaptor.getValue().getDeparturePlace().getPlaceId())
				.isEqualTo(command.getDeparturePlaceId());

		assertThat(journeyRequestCaptor.getValue().getDeparturePlace().getPlaceRegionId())
				.isEqualTo(command.getDeparturePlaceRegionId());

		assertThat(journeyRequestCaptor.getValue().getDeparturePlace().getPlaceName())
				.isEqualTo(command.getDeparturePlaceName());

		assertThat(journeyRequestCaptor.getValue().getArrivalPlace().getPlaceId())
				.isEqualTo(command.getArrivalPlaceId());

		assertThat(journeyRequestCaptor.getValue().getArrivalPlace().getPlaceRegionId())
				.isEqualTo(command.getArrivalPlaceRegionId());

		assertThat(journeyRequestCaptor.getValue().getArrivalPlace().getPlaceName())
				.isEqualTo(command.getArrivalPlaceName());

		assertThat(journeyRequestCaptor.getValue().getDateTime()).isEqualTo(command.getDateTime());

		assertThat(journeyRequestCaptor.getValue().getEndDateTime()).isEqualTo(command.getEndDateTime());

		assertThat(journeyRequestCaptor.getValue().getEngineType().getId()).isEqualTo(command.getEngineTypeId());

		assertThat(journeyRequestCaptor.getValue().getEngineType().getId()).isEqualTo(command.getEngineTypeId());

		assertThat(journeyRequestCaptor.getValue().getDistance()).isEqualTo(command.getDistance());

		assertThat(journeyRequestCaptor.getValue().getWorkers()).isEqualTo(command.getWorkers());

		assertThat(journeyRequestCaptor.getValue().getDescription()).isEqualTo(command.getDescription());

	

	}

}