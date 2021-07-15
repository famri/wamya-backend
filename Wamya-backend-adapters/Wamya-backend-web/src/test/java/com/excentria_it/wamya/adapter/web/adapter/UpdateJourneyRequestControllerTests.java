package com.excentria_it.wamya.adapter.web.adapter;

import static com.c4_soft.springaddons.security.oauth2.test.mockmvc.MockAuthenticationRequestPostProcessor.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.test.context.ActiveProfiles;

import com.c4_soft.springaddons.security.oauth2.test.mockmvc.MockMvcSupport;
import com.excentria_it.wamya.application.port.in.CreateJourneyRequestUseCase.CreateJourneyRequestCommand;
import com.excentria_it.wamya.application.port.in.UpdateJourneyRequestUseCase;
import com.excentria_it.wamya.common.exception.handlers.RestApiExceptionHandler;
import com.excentria_it.wamya.test.data.common.JourneyRequestTestData;
import com.excentria_it.wamya.test.data.common.TestConstants;
import com.fasterxml.jackson.databind.ObjectMapper;

@ActiveProfiles(profiles = { "web-local" })
@Import(value = { UpdateJourneyRequestController.class, RestApiExceptionHandler.class, MockMvcSupport.class })
@WebMvcTest(controllers = UpdateJourneyRequestController.class)
public class UpdateJourneyRequestControllerTests {

	@Autowired
	private MockMvcSupport api;

	@MockBean
	private UpdateJourneyRequestUseCase updateJourneyRequestUseCase;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void testUpdateJourneyRequest() throws Exception {
		CreateJourneyRequestCommand command = JourneyRequestTestData.defaultCreateJourneyRequestCommandBuilder()
				.build();

		String commandJson = objectMapper.writeValueAsString(command);

		api.with(mockAuthentication(JwtAuthenticationToken.class).name(TestConstants.DEFAULT_EMAIL)
				.authorities("SCOPE_journey:write"))
				.perform(patch("/journey-requests/{id}", 1L).param("lang", "fr_FR").contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(commandJson))
				.andExpect(status().isNoContent()).andReturn();

		then(updateJourneyRequestUseCase).should(times(1)).updateJourneyRequest(1L, command, TestConstants.DEFAULT_EMAIL, "fr_FR");
	}
}
