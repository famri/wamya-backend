package com.excentria_it.wamya.adapter.web.adapter;

import static com.c4_soft.springaddons.security.oauth2.test.mockmvc.MockAuthenticationRequestPostProcessor.*;
import static com.excentria_it.wamya.adapter.web.helper.ResponseBodyMatchers.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MvcResult;

import com.c4_soft.springaddons.security.oauth2.test.mockmvc.MockMvcSupport;
import com.excentria_it.wamya.adapter.web.utils.ValidationHelper;
import com.excentria_it.wamya.application.port.in.LoadProposalsUseCase;
import com.excentria_it.wamya.application.port.in.LoadProposalsUseCase.LoadProposalsCommand;
import com.excentria_it.wamya.common.exception.handlers.RestApiExceptionHandler;
import com.excentria_it.wamya.domain.JourneyRequestProposals;
import com.excentria_it.wamya.test.data.common.JourneyProposalTestData;
import com.excentria_it.wamya.test.data.common.TestConstants;
import com.fasterxml.jackson.databind.ObjectMapper;

@ActiveProfiles(profiles = { "web-local" })
@Import(value = { LoadProposalsController.class, RestApiExceptionHandler.class, MockMvcSupport.class,
		ValidationHelper.class })
@WebMvcTest(controllers = LoadProposalsController.class)
public class LoadProposalsControllerTests {
	@Autowired
	private MockMvcSupport api;

	@MockBean
	private LoadProposalsUseCase loadProposalsUseCase;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void givenValidInputAndEmailUsername_WhenLoadJourneyRequestProposals_ThenReturnJourneyRequestProposals()
			throws Exception {

		// given
		LoadProposalsCommand command = JourneyProposalTestData.defaultLoadProposalsCommandBuilder().build();
		JourneyRequestProposals journeyRequestProposals = JourneyProposalTestData.defaultJourneyRequestProposals();
		given(loadProposalsUseCase.loadProposals(any(LoadProposalsCommand.class), any(String.class)))
				.willReturn(journeyRequestProposals);

		ArgumentCaptor<LoadProposalsCommand> commandCaptor = ArgumentCaptor.forClass(LoadProposalsCommand.class);
		// when

		MvcResult mvcResult = api
				.with(mockAuthentication(JwtAuthenticationToken.class).name(TestConstants.DEFAULT_EMAIL)
						.authorities("SCOPE_offer:read"))
				.perform(get("/journey-requests/{journeyRequestId}/proposals", command.getJourneyRequestId())
						.param("page", command.getPageNumber().toString())
						.param("size", command.getPageSize().toString()).param("sort", "price,asc"))
				.andExpect(status().isOk()).andReturn();

		JourneyRequestProposals response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
				JourneyRequestProposals.class);
		// then

		then(loadProposalsUseCase).should(times(1)).loadProposals(commandCaptor.capture(), eq("en_US"));

		assertThat(commandCaptor.getValue().getClientUsername()).isEqualTo(TestConstants.DEFAULT_EMAIL);
		assertThat(commandCaptor.getValue().getPageNumber()).isEqualTo(command.getPageNumber());
		assertThat(commandCaptor.getValue().getPageSize()).isEqualTo(command.getPageSize());
		assertThat(commandCaptor.getValue().getSortingCriterion().getField())
				.isEqualTo(command.getSortingCriterion().getField());
		assertThat(commandCaptor.getValue().getSortingCriterion().getDirection())
				.isEqualTo(command.getSortingCriterion().getDirection());

		assertEquals(journeyRequestProposals, response);

	}

	@Test
	void givenInalidInput_WhenLoadJourneyRequestProposals_ThenReturnBadRequest() throws Exception {

		// given
		LoadProposalsCommand command = JourneyProposalTestData.defaultLoadProposalsCommandBuilder().build();

		// when

		api.with(mockAuthentication(JwtAuthenticationToken.class).name(TestConstants.DEFAULT_EMAIL)
				.authorities("SCOPE_offer:read"))
				.perform(get("/journey-requests/{journeyRequestId}/proposals", command.getJourneyRequestId())
						.param("page", command.getPageNumber().toString())
						.param("size", command.getPageSize().toString()).param("sort", "not_valid_sorting_field,asc"))
				.andExpect(responseBody().containsApiErrors(List.of(
						"sortingCriterion: Wrong sort criterion: 'SortCriterion(field=not_valid_sorting_field, direction=asc)'. Valid sort fields are:[price]. Valid sort directions are:[asc, desc].")))
				.andExpect(status().isBadRequest()).andReturn();

		// then

		then(loadProposalsUseCase).should(never()).loadProposals(any(LoadProposalsCommand.class), any(String.class));

	}
}
