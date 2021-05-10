package com.excentria_it.wamya.adapter.web.adapter;

import static com.c4_soft.springaddons.security.oauth2.test.mockmvc.MockAuthenticationRequestPostProcessor.*;
import static com.excentria_it.wamya.adapter.web.helper.ResponseBodyMatchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MvcResult;

import com.c4_soft.springaddons.security.oauth2.test.mockmvc.MockMvcSupport;
import com.excentria_it.wamya.application.port.in.MakeProposalUseCase;
import com.excentria_it.wamya.application.port.in.MakeProposalUseCase.MakeProposalCommand;
import com.excentria_it.wamya.common.exception.handlers.RestApiExceptionHandler;
import com.excentria_it.wamya.domain.MakeProposalDto;
import com.excentria_it.wamya.test.data.common.JourneyProposalTestData;
import com.excentria_it.wamya.test.data.common.TestConstants;
import com.fasterxml.jackson.databind.ObjectMapper;

@ActiveProfiles(profiles = { "web-local" })
@Import(value = { MakeProposalController.class, RestApiExceptionHandler.class, MockMvcSupport.class })
@WebMvcTest(controllers = MakeProposalController.class)
public class MakeProposalControllerTests {
	@Autowired
	private MockMvcSupport api;

	@MockBean
	private MakeProposalUseCase makeProposalUseCase;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void givenValidInputAndEmailUsername_WhenMakeProposal_ThenReturnCreateProposal() throws Exception {
		// given
		MakeProposalCommand command = JourneyProposalTestData.defaultMakeProposalCommandBuilder().build();

		String makeProposalJson = objectMapper.writeValueAsString(command);

		MakeProposalDto makeProposalDto = new MakeProposalDto(1L, command.getPrice(), null);

		given(makeProposalUseCase.makeProposal(any(MakeProposalCommand.class), any(Long.class), any(String.class),
				any(String.class))).willReturn(makeProposalDto);
		// when

		MvcResult mvcResult = api
				.with(mockAuthentication(JwtAuthenticationToken.class).name(TestConstants.DEFAULT_EMAIL)
						.authorities("SCOPE_offer:write"))
				.perform(post("/journey-requests/{id}/proposals", 1L).contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(makeProposalJson))
				.andExpect(status().isCreated()).andReturn();

		MakeProposalDto makeProposalDtoResult = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
				MakeProposalDto.class);
		// then

		then(makeProposalUseCase).should(times(1)).makeProposal(eq(command), eq(1L), eq(TestConstants.DEFAULT_EMAIL),
				eq("en_US"));
		then(makeProposalDto.getId().equals(makeProposalDtoResult.getId()));
		then(makeProposalDto.getPrice().equals(makeProposalDtoResult.getPrice()));

	}

	@Test
	void givenInvalidInputAndEmailUsername_WhenMakeProposal_ThenReturnBadRequest() throws Exception {
		// given
		MakeProposalCommand command = JourneyProposalTestData.defaultMakeProposalCommandBuilder().price(-250.0).build();

		String makeProposalJson = objectMapper.writeValueAsString(command);

		// when

		api.with(mockAuthentication(JwtAuthenticationToken.class).name(TestConstants.DEFAULT_EMAIL)
				.authorities("SCOPE_offer:write"))
				.perform(post("/journey-requests/{id}/proposals", 1L).contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(makeProposalJson))
				.andExpect(status().isBadRequest())
				.andExpect(responseBody().containsApiErrors(List.of("price: must be greater than or equal to 0")))
				.andReturn();

		// then

		then(makeProposalUseCase).should(never()).makeProposal(any(MakeProposalCommand.class), any(Long.class),
				any(String.class), any(String.class));

	}

}
