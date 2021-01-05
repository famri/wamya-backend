package com.excentria_it.wamya.adapter.web.adapter;

import static com.c4_soft.springaddons.security.oauth2.test.mockmvc.MockAuthenticationRequestPostProcessor.*;
import static org.mockito.ArgumentMatchers.*;
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
import com.excentria_it.wamya.application.port.in.AcceptProposalUseCase;
import com.excentria_it.wamya.application.port.in.AcceptProposalUseCase.AcceptProposalCommand;
import com.excentria_it.wamya.common.exception.RestApiExceptionHandler;
import com.excentria_it.wamya.test.data.common.TestConstants;
import com.fasterxml.jackson.databind.ObjectMapper;

@ActiveProfiles(profiles = { "web-local" })
@Import(value = { AcceptProposalController.class, RestApiExceptionHandler.class, MockMvcSupport.class })
@WebMvcTest(controllers = AcceptProposalController.class)
public class AcceptProposalControllerTests {
	@Autowired
	private MockMvcSupport api;

	@MockBean
	private AcceptProposalUseCase acceptProposalUseCase;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void givenValidInput_WhenAcceptProposal_ThenSucceed() throws Exception {

		AcceptProposalCommand command = new AcceptProposalCommand("accepted");

		String commandJson = objectMapper.writeValueAsString(command);

		api.with(mockAuthentication(JwtAuthenticationToken.class).name(TestConstants.DEFAULT_EMAIL)
				.authorities("SCOPE_journey:write"))
				.perform(patch("/journey-requests/{journeyRequestId}/proposals/{proposalId}", 1L, 1L)
						.contentType(MediaType.APPLICATION_JSON_VALUE).content(commandJson))
				.andExpect(status().isNoContent()).andReturn();

		then(acceptProposalUseCase).should(times(1)).acceptProposal(eq(1L), eq(1L), eq(TestConstants.DEFAULT_EMAIL),
				eq("en_US"));

	}
}
