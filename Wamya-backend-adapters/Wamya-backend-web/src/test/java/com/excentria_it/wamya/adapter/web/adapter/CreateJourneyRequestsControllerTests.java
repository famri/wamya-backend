package com.excentria_it.wamya.adapter.web.adapter;

import static com.c4_soft.springaddons.security.oauth2.test.mockmvc.MockAuthenticationRequestPostProcessor.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.test.context.ActiveProfiles;

import com.c4_soft.springaddons.security.oauth2.test.mockmvc.MockMvcSupport;
import com.excentria_it.wamya.application.port.in.CreateJourneyRequestUseCase;
import com.excentria_it.wamya.application.port.in.CreateJourneyRequestUseCase.CreateJourneyRequestCommand;
import com.excentria_it.wamya.common.exception.RestApiExceptionHandler;
import com.excentria_it.wamya.domain.CreateJourneyRequestDto;
import com.excentria_it.wamya.test.data.common.JourneyRequestTestData;
import com.excentria_it.wamya.test.data.common.TestConstants;
import com.fasterxml.jackson.databind.ObjectMapper;

@ActiveProfiles(value = { "web-local" })
@Import(value = { CreateJourneyRequestsController.class, RestApiExceptionHandler.class, MockMvcSupport.class })
@WebMvcTest(controllers = CreateJourneyRequestsController.class)
public class CreateJourneyRequestsControllerTests {

	@Autowired
	private MockMvcSupport api;

	@MockBean
	private CreateJourneyRequestUseCase createJourneyRequestUseCase;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void givenValidInput_WhenCreateJourneyRequest_ThenReturnCreatedJourneyRequest() throws Exception {
		CreateJourneyRequestCommand command = JourneyRequestTestData.defaultCreateJourneyRequestCommandBuilder()
				.build();
		CreateJourneyRequestDto journeyRequest = JourneyRequestTestData.defaultCreateJourneyRequestDto();

		given(createJourneyRequestUseCase.createJourneyRequest(any(CreateJourneyRequestCommand.class),
				any(String.class), any(String.class))).willReturn(journeyRequest);

		String createJourneyRequestJson = objectMapper.writeValueAsString(command);

		api.with(mockAuthentication(JwtAuthenticationToken.class).name(TestConstants.DEFAULT_EMAIL)
				.authorities("SCOPE_journey:write"))
				.perform(post("/journey-requests").contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(createJourneyRequestJson))
				.andExpect(status().isCreated()).andReturn();
		ArgumentCaptor<CreateJourneyRequestCommand> captor = ArgumentCaptor.forClass(CreateJourneyRequestCommand.class);
		then(createJourneyRequestUseCase).should(times(1)).createJourneyRequest(captor.capture(),
				eq(TestConstants.DEFAULT_EMAIL), eq("en_US"));

		assertThat(captor.getValue().getDateTime()).isEqualTo(command.getDateTime());
		assertThat(captor.getValue().getEndDateTime()).isEqualTo(command.getEndDateTime());
		assertThat(captor.getValue().getDeparturePlaceId()).isEqualTo(command.getDeparturePlaceId());
		assertThat(captor.getValue().getDeparturePlaceRegionId()).isEqualTo(command.getDeparturePlaceRegionId());
		assertThat(captor.getValue().getDeparturePlaceName()).isEqualTo(command.getDeparturePlaceName());
		assertThat(captor.getValue().getArrivalPlaceId()).isEqualTo(command.getArrivalPlaceId());
		assertThat(captor.getValue().getArrivalPlaceRegionId()).isEqualTo(command.getArrivalPlaceRegionId());
		assertThat(captor.getValue().getArrivalPlaceName()).isEqualTo(command.getArrivalPlaceName());

		assertThat(captor.getValue().getEngineTypeId()).isEqualTo(command.getEngineTypeId());
		assertThat(captor.getValue().getDistance()).isEqualTo(command.getDistance());
		assertThat(captor.getValue().getWorkers()).isEqualTo(command.getWorkers());
		assertThat(captor.getValue().getDescription()).isEqualTo(command.getDescription());

	}

	@Test
	void givenValidInputAndBadAuthority_WhenCreateJourneyRequest_ThenReturnUnauthorized() throws Exception {
		CreateJourneyRequestCommand command = JourneyRequestTestData.defaultCreateJourneyRequestCommandBuilder()
				.build();
		CreateJourneyRequestDto journeyRequest = JourneyRequestTestData.defaultCreateJourneyRequestDto();

		given(createJourneyRequestUseCase.createJourneyRequest(any(CreateJourneyRequestCommand.class),
				any(String.class), any(String.class))).willReturn(journeyRequest);

		String createJourneyRequestJson = objectMapper.writeValueAsString(command);

		api.with(mockAuthentication(JwtAuthenticationToken.class).name(TestConstants.DEFAULT_EMAIL)
				.authorities("SCOPE_journey:read"))
				.perform(post("/journey-requests").contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(createJourneyRequestJson))
				.andExpect(status().isForbidden()).andReturn();

		then(createJourneyRequestUseCase).should(never()).createJourneyRequest(eq(command),
				eq(TestConstants.DEFAULT_EMAIL), eq("en_US"));

	}

}
