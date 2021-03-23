package com.excentria_it.wamya.adapter.web.adapter;

import static com.c4_soft.springaddons.security.oauth2.test.mockmvc.MockAuthenticationRequestPostProcessor.*;
import static com.excentria_it.wamya.adapter.web.helper.ResponseBodyMatchers.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MvcResult;

import com.c4_soft.springaddons.security.oauth2.test.mockmvc.MockMvcSupport;
import com.excentria_it.wamya.adapter.web.utils.ValidationHelper;
import com.excentria_it.wamya.application.port.in.SearchJourneyRequestsUseCase;
import com.excentria_it.wamya.application.port.in.SearchJourneyRequestsUseCase.SearchJourneyRequestsCommand;
import com.excentria_it.wamya.common.SortCriterion;
import com.excentria_it.wamya.common.exception.RestApiExceptionHandler;
import com.excentria_it.wamya.domain.JourneyRequestsSearchResult;
import com.excentria_it.wamya.test.data.common.JourneyRequestTestData;
import com.excentria_it.wamya.test.data.common.TestConstants;
import com.fasterxml.jackson.databind.ObjectMapper;

@ActiveProfiles(value = { "web-local" })
@Import(value = { SearchJourneyRequestsController.class, RestApiExceptionHandler.class, MockMvcSupport.class,
		ValidationHelper.class })
@WebMvcTest(controllers = SearchJourneyRequestsController.class)
public class SearchJourneyRequestsControllerTests {

	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter
			.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

	@Autowired
	private MockMvcSupport api;

	@MockBean
	private SearchJourneyRequestsUseCase searchJourneyRequestsUseCase;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void givenValidInput_WhenSearch_ThenReturnSearchResult() throws Exception {

		SearchJourneyRequestsCommand command = JourneyRequestTestData.defaultSearchJourneyRequestsCommandBuilder()
				.build();

		JourneyRequestsSearchResult expectedResult = JourneyRequestTestData
				.defaultJourneyRequestsSearchResult(ZoneId.of("Africa/Tunis"));

		given(searchJourneyRequestsUseCase.searchJourneyRequests(any(SearchJourneyRequestsCommand.class),
				any(String.class), any(String.class))).willReturn(expectedResult);

		MvcResult mvcResult = api
				.with(mockAuthentication(JwtAuthenticationToken.class).name(TestConstants.DEFAULT_EMAIL)
						.authorities("SCOPE_journey:read"))
				.perform(get("/journey-requests").param("departure", command.getDeparturePlaceDepartmentId().toString())
						.param("arrival", command.getArrivalPlaceDepartmentIds().stream().map(id -> id.toString())
								.toArray(String[]::new)

						).param("fromDate", command.getStartDateTime().format(DATE_TIME_FORMATTER))
						.param("toDate", command.getEndDateTime().format(DATE_TIME_FORMATTER)).param("lang", "fr_FR")
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

		JourneyRequestsSearchResult expectedResult = JourneyRequestTestData
				.defaultJourneyRequestsSearchResult(ZoneId.of("Africa/Tunis"));

		given(searchJourneyRequestsUseCase.searchJourneyRequests(any(SearchJourneyRequestsCommand.class),
				any(String.class), any(String.class))).willReturn(expectedResult);

		MvcResult mvcResult = api
				.with(mockAuthentication(JwtAuthenticationToken.class).name(TestConstants.DEFAULT_EMAIL)
						.authorities("SCOPE_journey:write"))
				.perform(get("/journey-requests").param("departure", command.getDeparturePlaceDepartmentId().toString())
						.param("arrival", command.getArrivalPlaceDepartmentIds().stream().map(id -> id.toString())
								.toArray(String[]::new)

						).param("fromDate", command.getStartDateTime().format(DATE_TIME_FORMATTER))
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
		then(searchJourneyRequestsUseCase).should(never())
				.searchJourneyRequests(any(SearchJourneyRequestsCommand.class), any(String.class), any(String.class));

	}

	@Test
	void givenValidInputWithNoSortCriterion_WhenSearch_ThenReturnSearchResult() throws Exception {

		SearchJourneyRequestsCommand command = JourneyRequestTestData.defaultSearchJourneyRequestsCommandBuilder()
				.sortingCriterion(null).build();

		JourneyRequestsSearchResult expectedResult = JourneyRequestTestData
				.defaultJourneyRequestsSearchResult(ZoneId.of("Africa/Tunis"));

		given(searchJourneyRequestsUseCase.searchJourneyRequests(any(SearchJourneyRequestsCommand.class),
				any(String.class), any(String.class))).willReturn(expectedResult);

		MvcResult mvcResult = api
				.with(mockAuthentication(JwtAuthenticationToken.class).name(TestConstants.DEFAULT_EMAIL)
						.authorities("SCOPE_journey:read"))
				.perform(get("/journey-requests").param("departure", command.getDeparturePlaceDepartmentId().toString())
						.param("arrival", command.getArrivalPlaceDepartmentIds().stream().map(id -> id.toString())
								.toArray(String[]::new)

						).param("fromDate", command.getStartDateTime().format(DATE_TIME_FORMATTER))
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

		JourneyRequestsSearchResult expectedResult = JourneyRequestTestData
				.defaultJourneyRequestsSearchResult(ZoneId.of("Africa/Tunis"));

		given(searchJourneyRequestsUseCase.searchJourneyRequests(any(SearchJourneyRequestsCommand.class),
				any(String.class), any(String.class))).willReturn(expectedResult);

		MvcResult mvcResult = api
				.with(mockAuthentication(JwtAuthenticationToken.class).name(TestConstants.DEFAULT_EMAIL)
						.authorities("SCOPE_journey:read"))
				.perform(get("/journey-requests").param("departure", command.getDeparturePlaceDepartmentId().toString())
						.param("arrival", command.getArrivalPlaceDepartmentIds().stream().map(id -> id.toString())
								.toArray(String[]::new)

						).param("fromDate", command.getStartDateTime().format(DATE_TIME_FORMATTER))
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
				.sortingCriterion(new SortCriterion("dummy-field", "up")).build();

		JourneyRequestsSearchResult expectedResult = JourneyRequestTestData
				.defaultJourneyRequestsSearchResult(ZoneId.of("Africa/Tunis"));

		given(searchJourneyRequestsUseCase.searchJourneyRequests(any(SearchJourneyRequestsCommand.class),
				any(String.class), any(String.class))).willReturn(expectedResult);

		api.with(mockAuthentication(JwtAuthenticationToken.class).name(TestConstants.DEFAULT_EMAIL)
				.authorities("SCOPE_journey:read"))
				.perform(get("/journey-requests").param("departure", command.getDeparturePlaceDepartmentId().toString())
						.param("arrival", command.getArrivalPlaceDepartmentIds().stream().map(id -> id.toString())
								.toArray(String[]::new)

						).param("fromDate", command.getStartDateTime().format(DATE_TIME_FORMATTER))
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
						"sortingCriterion: Wrong sort criterion: 'SortCriterion(field=dummy-field, direction=up)'. Valid sort fields are:[min-price, distance, date-time]. Valid sort directions are:[asc, desc].")))

				.andReturn();

		then(searchJourneyRequestsUseCase).should(never())
				.searchJourneyRequests(any(SearchJourneyRequestsCommand.class), any(String.class), any(String.class));

	}

}
