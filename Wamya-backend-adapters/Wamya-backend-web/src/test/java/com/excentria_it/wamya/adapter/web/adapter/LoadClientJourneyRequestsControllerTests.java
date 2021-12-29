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
import com.excentria_it.wamya.application.port.in.LoadClientJourneyRequestsUseCase;
import com.excentria_it.wamya.application.port.in.LoadClientJourneyRequestsUseCase.LoadJourneyRequestCommand;
import com.excentria_it.wamya.application.port.in.LoadClientJourneyRequestsUseCase.LoadJourneyRequestsCommand;
import com.excentria_it.wamya.common.exception.JourneyRequestNotFoundException;
import com.excentria_it.wamya.common.exception.handlers.RestApiExceptionHandler;
import com.excentria_it.wamya.domain.ClientJourneyRequestDto;
import com.excentria_it.wamya.domain.ClientJourneyRequests;
import com.excentria_it.wamya.test.data.common.JourneyRequestTestData;
import com.excentria_it.wamya.test.data.common.TestConstants;
import com.fasterxml.jackson.databind.ObjectMapper;

@ActiveProfiles(profiles = { "web-local" })
@Import(value = { LoadClientJourneyRequestsController.class, RestApiExceptionHandler.class, MockMvcSupport.class,
		ValidationHelper.class })
@WebMvcTest(controllers = LoadClientJourneyRequestsController.class)
public class LoadClientJourneyRequestsControllerTests {
	@Autowired
	private MockMvcSupport api;

	@MockBean
	private LoadClientJourneyRequestsUseCase loadClientJourneyRequestsUseCase;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void givenValidInput_WhenLoadClientJourneyRequests_ThenReturnClientJourneyRequests() throws Exception {

		// given
		LoadJourneyRequestsCommand command = JourneyRequestTestData.defaultLoadJourneyRequestsCommandBuilder().build();

		ClientJourneyRequests expectedResult = JourneyRequestTestData.defaultClientJourneyRequests();

		given(loadClientJourneyRequestsUseCase.loadJourneyRequests(any(LoadJourneyRequestsCommand.class),
				any(String.class))).willReturn(expectedResult);
		// when

		MvcResult mvcResult = api
				.with(mockAuthentication(JwtAuthenticationToken.class).name(command.getClientUsername())
						.authorities("SCOPE_journey:write"))
				.perform(get("/users/me/journey-requests").param("period", command.getPeriodCriterion().getValue())
						.param("page", command.getPageNumber().toString())
						.param("size", command.getPageSize().toString()).param("sort", "creation-date-time,desc")
						.param("lang", "fr_FR"))

				.andExpect(status().isOk()).andReturn();

		// then
		ArgumentCaptor<LoadJourneyRequestsCommand> commandCaptor = ArgumentCaptor
				.forClass(LoadJourneyRequestsCommand.class);

		then(loadClientJourneyRequestsUseCase).should(times(1)).loadJourneyRequests(commandCaptor.capture(),
				eq("fr_FR"));
		assertEquals(command.getClientUsername(), commandCaptor.getValue().getClientUsername());
		assertEquals(command.getPageNumber(), commandCaptor.getValue().getPageNumber());
		assertEquals(command.getPageSize(), commandCaptor.getValue().getPageSize());
		assertEquals(command.getSortingCriterion().getDirection(),
				commandCaptor.getValue().getSortingCriterion().getDirection());
		assertEquals(command.getSortingCriterion().getField(),
				commandCaptor.getValue().getSortingCriterion().getField());
		assertEquals(command.getPeriodCriterion().getValue(), commandCaptor.getValue().getPeriodCriterion().getValue());
		assertTrue(command.getPeriodCriterion().getLowerEdge()
				.isBefore(commandCaptor.getValue().getPeriodCriterion().getLowerEdge())
				|| command.getPeriodCriterion().getLowerEdge()
						.equals(commandCaptor.getValue().getPeriodCriterion().getLowerEdge()));
		assertTrue(command.getPeriodCriterion().getHigherEdge()
				.isBefore(commandCaptor.getValue().getPeriodCriterion().getHigherEdge())
				|| command.getPeriodCriterion().getHigherEdge()
						.equals(commandCaptor.getValue().getPeriodCriterion().getHigherEdge()));

		String actualResponseBody = mvcResult.getResponse().getContentAsString();

		assertThat(actualResponseBody).isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(expectedResult));
	}

	@Test
	void givenInvalidInput_WhenLoadClientJourneyRequests_ThenReturnBadRequest() throws Exception {

		// given //when

		api.with(mockAuthentication(JwtAuthenticationToken.class).name(TestConstants.DEFAULT_EMAIL)
				.authorities("SCOPE_journey:write"))
				.perform(get("/users/me/journey-requests").param("period", "not_valid_period").param("page", "0")
						.param("size", "25").param("sort", "not_valid_sort_field,desc"))
				.andExpect(responseBody().containsApiErrors(List.of(
						"periodCriterion: Wrong period: 'PeriodCriterion(value=not_valid_period, lowerEdge=null, higherEdge=null)'. Valid period values are: [w1, m1, lm1, lm3].",
						"sortingCriterion: Wrong sort criterion: 'SortCriterion(field=not_valid_sort_field, direction=desc)'. Valid sort fields are:[creation-date-time, date-time]. Valid sort directions are:[asc, desc].")))
				.andExpect(status().isBadRequest());

		// then
		then(loadClientJourneyRequestsUseCase).should(never())
				.loadJourneyRequests(any(LoadJourneyRequestsCommand.class), any(String.class));

	}

	@Test
	void givenInexistentJourneyRequest_WhenLoadClientJourneyRequest_ThenReturnBadRequest() throws Exception {

		// given
		LoadJourneyRequestCommand command = JourneyRequestTestData.defaultLoadJourneyRequestCommandBuilder().build();

		doThrow(new JourneyRequestNotFoundException("SOME ERROR DESCRIPTION")).when(loadClientJourneyRequestsUseCase)
				.loadJourneyRequest(eq(command), any(String.class));
		// when //then
		api.with(mockAuthentication(JwtAuthenticationToken.class).name(TestConstants.DEFAULT_EMAIL)
				.authorities("SCOPE_journey:write"))
				.perform(get("/users/me/journey-requests/{journeyRequestId}", 1L).param("lang", "fr_FR"))
				.andExpect(responseBody().containsApiErrors(List.of("SOME ERROR DESCRIPTION")))
				.andExpect(status().isBadRequest()).andReturn();
		// then
		then(loadClientJourneyRequestsUseCase).should(times(1)).loadJourneyRequest(eq(command), eq("fr_FR"));
	}

	@Test
	void givenExistentJourneyRequest_WhenLoadClientJourneyRequest_ThenReturnJourneyRequest() throws Exception {

		// given
		LoadJourneyRequestCommand command = JourneyRequestTestData.defaultLoadJourneyRequestCommandBuilder().build();

		ClientJourneyRequestDto expectedResult = JourneyRequestTestData.defaultClientJourneyRequestDto();

		given(loadClientJourneyRequestsUseCase.loadJourneyRequest(eq(command), any(String.class)))
				.willReturn(expectedResult);

		// when
		MvcResult mvcResult = api
				.with(mockAuthentication(JwtAuthenticationToken.class).name(TestConstants.DEFAULT_EMAIL)
						.authorities("SCOPE_journey:write"))
				.perform(get("/users/me/journey-requests/{journeyRequestId}", 1L).param("lang", "fr_FR"))

				.andExpect(status().isOk()).andReturn();
		// then
		then(loadClientJourneyRequestsUseCase).should(times(1)).loadJourneyRequest(eq(command), eq("fr_FR"));
		String actualResponseBody = mvcResult.getResponse().getContentAsString();

		assertThat(actualResponseBody).isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(expectedResult));
	}
}
