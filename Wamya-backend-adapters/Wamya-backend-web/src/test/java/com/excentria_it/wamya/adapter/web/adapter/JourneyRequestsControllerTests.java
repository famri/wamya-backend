package com.excentria_it.wamya.adapter.web.adapter;

import static com.c4_soft.springaddons.security.oauth2.test.mockmvc.MockAuthenticationRequestPostProcessor.*;
import static com.excentria_it.wamya.adapter.web.helper.ResponseBodyMatchers.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

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
import com.excentria_it.wamya.application.port.in.CreateJourneyRequestUseCase;
import com.excentria_it.wamya.application.port.in.CreateJourneyRequestUseCase.CreateJourneyRequestCommand;
import com.excentria_it.wamya.application.port.in.SearchJourneyRequestsUseCase;
import com.excentria_it.wamya.application.port.in.SearchJourneyRequestsUseCase.SearchJourneyRequestsCommand;
import com.excentria_it.wamya.common.SortingCriterion;
import com.excentria_it.wamya.common.exception.RestApiExceptionHandler;
import com.excentria_it.wamya.domain.JourneyRequest;
import com.excentria_it.wamya.domain.JourneyRequestsSearchResult;
import com.excentria_it.wamya.test.data.common.JourneyRequestTestData;
import com.excentria_it.wamya.test.data.common.TestConstants;
import com.fasterxml.jackson.databind.ObjectMapper;

@ActiveProfiles(value = { "web-local" })
@Import(value = { JourneyRequestsController.class, RestApiExceptionHandler.class, MockMvcSupport.class })
@WebMvcTest(controllers = JourneyRequestsController.class)
public class JourneyRequestsControllerTests {

	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

	@Autowired
	private MockMvcSupport api;

	@MockBean
	private SearchJourneyRequestsUseCase searchJourneyRequestsUseCase;

	@MockBean
	private CreateJourneyRequestUseCase createJourneyRequestUseCase;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void givenValidInput_WhenSearch_ThenReturnSearchResult() throws Exception {

		SearchJourneyRequestsCommand command = JourneyRequestTestData.defaultSearchJourneyRequestsCommandBuilder()
				.build();

		JourneyRequestsSearchResult expectedResult = JourneyRequestTestData.defaultJourneyRequestsSearchResult();

		given(searchJourneyRequestsUseCase.searchJourneyRequests(any(SearchJourneyRequestsCommand.class),
				any(String.class))).willReturn(expectedResult);

		MvcResult mvcResult = api
				.with(mockAuthentication(JwtAuthenticationToken.class).name(TestConstants.DEFAULT_EMAIL)
						.authorities("SCOPE_journey:read"))
				.perform(get("/journey-requests").param("departure", command.getDeparturePlaceRegionId())
						.param("arrival",
								command.getArrivalPlaceRegionIds()
										.toArray(new String[command.getArrivalPlaceRegionIds().size()]))
						.param("fromDate", command.getStartDateTime().format(DATE_TIME_FORMATTER))
						.param("toDate", command.getEndDateTime().format(DATE_TIME_FORMATTER))
						.param("lang", "fr_FR")
						.param("engine",
								command.getEngineTypes().stream().map(e -> e.toString()).collect(Collectors.toSet())
										.toArray(new String[command.getEngineTypes().size()]))
						.param("page", command.getPageNumber().toString())
						.param("size", command.getPageSize().toString()).param("sort",
								command.getSortingCriterion().getField() + ","
										+ command.getSortingCriterion().getDirection()))

				.andExpect(status().isOk())

				.andReturn();
		String actualResponseBody = mvcResult.getResponse().getContentAsString();

		assertThat(actualResponseBody).isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(expectedResult));

	}

	@Test
	void givenValidInputAndBadAuthority_WhenSearch_ThenReturnForbidden() throws Exception {

		SearchJourneyRequestsCommand command = JourneyRequestTestData.defaultSearchJourneyRequestsCommandBuilder()
				.build();

		JourneyRequestsSearchResult expectedResult = JourneyRequestTestData.defaultJourneyRequestsSearchResult();

		given(searchJourneyRequestsUseCase.searchJourneyRequests(any(SearchJourneyRequestsCommand.class),
				any(String.class))).willReturn(expectedResult);

		MvcResult mvcResult = api
				.with(mockAuthentication(JwtAuthenticationToken.class).name(TestConstants.DEFAULT_EMAIL)
						.authorities("SCOPE_journey:write"))
				.perform(get("/journey-requests").param("departure", command.getDeparturePlaceRegionId())
						.param("arrival",
								command.getArrivalPlaceRegionIds()
										.toArray(new String[command.getArrivalPlaceRegionIds().size()]))
						.param("fromDate", command.getStartDateTime().format(DATE_TIME_FORMATTER))
						.param("toDate", command.getEndDateTime().format(DATE_TIME_FORMATTER)).param("lang", "en")
						.param("engine",
								command.getEngineTypes().stream().map(e -> e.toString()).collect(Collectors.toSet())
										.toArray(new String[command.getEngineTypes().size()]))
						.param("page", command.getPageNumber().toString())
						.param("size", command.getPageSize().toString()).param("sort",
								command.getSortingCriterion().getField() + ","
										+ command.getSortingCriterion().getDirection()))

				.andExpect(status().isForbidden())

				.andReturn();
		then(searchJourneyRequestsUseCase).should(never()).searchJourneyRequests(eq(command), eq("en"));

	}

	@Test
	void givenValidInputWithNoSortCriterion_WhenSearch_ThenReturnSearchResult() throws Exception {

		SearchJourneyRequestsCommand command = JourneyRequestTestData.defaultSearchJourneyRequestsCommandBuilder()
				.sortingCriterion(null).build();

		JourneyRequestsSearchResult expectedResult = JourneyRequestTestData.defaultJourneyRequestsSearchResult();

		given(searchJourneyRequestsUseCase.searchJourneyRequests(any(SearchJourneyRequestsCommand.class),
				any(String.class))).willReturn(expectedResult);

		MvcResult mvcResult = api
				.with(mockAuthentication(JwtAuthenticationToken.class).name(TestConstants.DEFAULT_EMAIL)
						.authorities("SCOPE_journey:read"))
				.perform(get("/journey-requests").param("departure", command.getDeparturePlaceRegionId())
						.param("arrival",
								command.getArrivalPlaceRegionIds()
										.toArray(new String[command.getArrivalPlaceRegionIds().size()]))
						.param("fromDate", command.getStartDateTime().format(DATE_TIME_FORMATTER))
						.param("toDate", command.getEndDateTime().format(DATE_TIME_FORMATTER))
						.param("engine",
								command.getEngineTypes().stream().map(e -> e.toString()).collect(Collectors.toSet())
										.toArray(new String[command.getEngineTypes().size()]))
						.param("page", command.getPageNumber().toString())
						.param("size", command.getPageSize().toString()))

				.andExpect(status().isOk())

				.andReturn();
		String actualResponseBody = mvcResult.getResponse().getContentAsString();

		assertThat(actualResponseBody).isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(expectedResult));

	}

	@Test
	void givenValidInputWithNoSortCriterionDirection_WhenSearch_ThenReturnSearchResult() throws Exception {

		SearchJourneyRequestsCommand command = JourneyRequestTestData.defaultSearchJourneyRequestsCommandBuilder()
				.build();

		JourneyRequestsSearchResult expectedResult = JourneyRequestTestData.defaultJourneyRequestsSearchResult();

		given(searchJourneyRequestsUseCase.searchJourneyRequests(any(SearchJourneyRequestsCommand.class),
				any(String.class))).willReturn(expectedResult);

		MvcResult mvcResult = api
				.with(mockAuthentication(JwtAuthenticationToken.class).name(TestConstants.DEFAULT_EMAIL)
						.authorities("SCOPE_journey:read"))
				.perform(get("/journey-requests").param("departure", command.getDeparturePlaceRegionId())
						.param("arrival",
								command.getArrivalPlaceRegionIds()
										.toArray(new String[command.getArrivalPlaceRegionIds().size()]))
						.param("fromDate", command.getStartDateTime().format(DATE_TIME_FORMATTER))
						.param("toDate", command.getEndDateTime().format(DATE_TIME_FORMATTER))
						.param("engine",
								command.getEngineTypes().stream().map(e -> e.toString()).collect(Collectors.toSet())
										.toArray(new String[command.getEngineTypes().size()]))
						.param("page", command.getPageNumber().toString())
						.param("size", command.getPageSize().toString())
						.param("sort", command.getSortingCriterion().getField()))

				.andExpect(status().isOk())

				.andReturn();
		String actualResponseBody = mvcResult.getResponse().getContentAsString();

		assertThat(actualResponseBody).isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(expectedResult));

	}

	@Test
	void givenInvalidInput_WhenSearch_ThenReturnBadRequest() throws Exception {

		SearchJourneyRequestsCommand command = JourneyRequestTestData.defaultSearchJourneyRequestsCommandBuilder()
				.sortingCriterion(new SortingCriterion("dummy-field", "up")).build();

		JourneyRequestsSearchResult expectedResult = JourneyRequestTestData.defaultJourneyRequestsSearchResult();

		given(searchJourneyRequestsUseCase.searchJourneyRequests(any(SearchJourneyRequestsCommand.class),
				any(String.class))).willReturn(expectedResult);

		MvcResult mvcResult = api
				.with(mockAuthentication(JwtAuthenticationToken.class).name(TestConstants.DEFAULT_EMAIL)
						.authorities("SCOPE_journey:read"))
				.perform(get("/journey-requests").param("departure", command.getDeparturePlaceRegionId())
						.param("arrival",
								command.getArrivalPlaceRegionIds()
										.toArray(new String[command.getArrivalPlaceRegionIds().size()]))
						.param("fromDate", command.getStartDateTime().format(DATE_TIME_FORMATTER))
						.param("toDate", command.getEndDateTime().format(DATE_TIME_FORMATTER)).param("lang", "en")
						.param("engine",
								command.getEngineTypes().stream().map(e -> e.toString()).collect(Collectors.toSet())
										.toArray(new String[command.getEngineTypes().size()]))
						.param("page", command.getPageNumber().toString())
						.param("size", command.getPageSize().toString()).param("sort",
								command.getSortingCriterion().getField() + ","
										+ command.getSortingCriterion().getDirection()))

				.andExpect(status().isBadRequest())
				.andExpect(responseBody().containsApiErrors(List.of(
						"sortingCriterion: Wrong sort criterion: 'SortingCriterion(field=dummy-field, direction=up)'. Valid sort fields are:[min-price, distance, date-time]. Valid sort directions are:[asc, desc].")))

				.andReturn();

		then(searchJourneyRequestsUseCase).should(never()).searchJourneyRequests(eq(command), eq("en"));

	}

	@Test
	void givenValidInput_WhenCreateJourneyRequest_ThenReturnCreatedJourneyRequest() throws Exception {
		CreateJourneyRequestCommand command = JourneyRequestTestData.defaultCreateJourneyRequestCommandBuilder()
				.build();
		JourneyRequest journeyRequest = JourneyRequestTestData.defaultJourneyRequest();

		given(createJourneyRequestUseCase.createJourneyRequest(any(CreateJourneyRequestCommand.class),
				any(String.class))).willReturn(journeyRequest);

		String createJourneyRequestJson = objectMapper.writeValueAsString(command);

		api.with(mockAuthentication(JwtAuthenticationToken.class).name(TestConstants.DEFAULT_EMAIL)
				.authorities("SCOPE_journey:write"))
				.perform(post("/journey-requests").contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(createJourneyRequestJson))
				.andExpect(status().isCreated()).andReturn();

		then(createJourneyRequestUseCase).should(times(1)).createJourneyRequest(eq(command),
				eq(TestConstants.DEFAULT_EMAIL));

	}

	@Test
	void givenValidInputAndBadAuthority_WhenCreateJourneyRequest_ThenReturnUnauthorized() throws Exception {
		CreateJourneyRequestCommand command = JourneyRequestTestData.defaultCreateJourneyRequestCommandBuilder()
				.build();
		JourneyRequest journeyRequest = JourneyRequestTestData.defaultJourneyRequest();

		given(createJourneyRequestUseCase.createJourneyRequest(any(CreateJourneyRequestCommand.class),
				any(String.class))).willReturn(journeyRequest);

		String createJourneyRequestJson = objectMapper.writeValueAsString(command);

		api.with(mockAuthentication(JwtAuthenticationToken.class).name(TestConstants.DEFAULT_EMAIL)
				.authorities("SCOPE_journey:read"))
				.perform(post("/journey-requests").contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(createJourneyRequestJson))
				.andExpect(status().isForbidden()).andReturn();

		then(createJourneyRequestUseCase).should(never()).createJourneyRequest(eq(command),
				eq(TestConstants.DEFAULT_EMAIL));

	}

}
