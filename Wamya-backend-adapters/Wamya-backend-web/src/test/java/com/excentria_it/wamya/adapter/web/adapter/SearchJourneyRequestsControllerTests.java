package com.excentria_it.wamya.adapter.web.adapter;

import com.c4_soft.springaddons.security.oauth2.test.mockmvc.MockMvcSupport;
import com.excentria_it.wamya.adapter.web.WebConfiguration;
import com.excentria_it.wamya.adapter.web.WebSecurityConfiguration;
import com.excentria_it.wamya.adapter.web.utils.ValidationHelper;
import com.excentria_it.wamya.application.port.in.SearchJourneyRequestsUseCase;
import com.excentria_it.wamya.application.port.in.SearchJourneyRequestsUseCase.SearchJourneyRequestsCommand;
import com.excentria_it.wamya.common.SortCriterion;
import com.excentria_it.wamya.common.exception.handlers.RestApiExceptionHandler;
import com.excentria_it.wamya.domain.JourneyRequestsSearchResult;
import com.excentria_it.wamya.test.data.common.JourneyRequestTestData;
import com.excentria_it.wamya.test.data.common.TestConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.excentria_it.wamya.adapter.web.helper.ResponseBodyMatchers.responseBody;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles(value = {"web-local"})
@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {WebSecurityConfiguration.class, WebConfiguration.class})
@Import(value = {SearchJourneyRequestsController.class, RestApiExceptionHandler.class, MockMvcSupport.class,
        ValidationHelper.class})
@WebMvcTest(controllers = SearchJourneyRequestsController.class)
public class SearchJourneyRequestsControllerTests {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter
            .ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

    @Autowired
    private WebApplicationContext context;
    private static MockMvc mvc;

    @BeforeEach
    void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @MockBean
    private SearchJourneyRequestsUseCase searchJourneyRequestsUseCase;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void givenValidInput_WhenTransporterSearchesJourneyRequests_ThenReturnSearchResult() throws Exception {

        SearchJourneyRequestsCommand command = JourneyRequestTestData.defaultSearchJourneyRequestsCommandBuilder()
                .build();

        JourneyRequestsSearchResult expectedResult = JourneyRequestTestData
                .defaultJourneyRequestsSearchResult(ZoneId.of("Africa/Tunis"));

        given(searchJourneyRequestsUseCase.searchJourneyRequests(any(SearchJourneyRequestsCommand.class),
                any(String.class), any(String.class))).willReturn(expectedResult);

        MvcResult mvcResult = mvc
                .perform(get("/journey-requests").with(jwt().jwt(builder -> builder.claim("sub", TestConstants.DEFAULT_EMAIL)).authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_TRANSPORTER"))))
                        .param("departure", command.getDeparturePlaceDepartmentId().toString())
                        .param("arrival", command.getArrivalPlaceDepartmentIds().stream().map(id -> id.toString())
                                .toArray(String[]::new)

                        ).param("fromDate", command.getStartDateTime().format(DATE_TIME_FORMATTER))
                        .param("toDate", command.getEndDateTime().format(DATE_TIME_FORMATTER)).param("lang", "fr_FR")
                        .param("engine",
                                command.getEngineTypes().stream().map(e -> e.toString()).collect(Collectors.toSet())
                                        .toArray(new String[command.getEngineTypes().size()]))
                        .param("statuses", command.getStatusCodes().stream().map(s -> s.name()).toArray(String[]::new))
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
    void givenValidInputAndBadAuthority_WhenClientSearchesJourneyRequest_ThenReturnForbidden() throws Exception {

        SearchJourneyRequestsCommand command = JourneyRequestTestData.defaultSearchJourneyRequestsCommandBuilder()
                .build();

        JourneyRequestsSearchResult expectedResult = JourneyRequestTestData
                .defaultJourneyRequestsSearchResult(ZoneId.of("Africa/Tunis"));

        given(searchJourneyRequestsUseCase.searchJourneyRequests(any(SearchJourneyRequestsCommand.class),
                any(String.class), any(String.class))).willReturn(expectedResult);

        mvc.perform(get("/journey-requests").with(jwt().jwt(builder -> builder.claim("sub", TestConstants.DEFAULT_EMAIL)).authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_CLIENT"))))
                        .param("departure", command.getDeparturePlaceDepartmentId().toString())
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
    void givenValidInputWithNoSortCriterion_WhenTransporterSearchesJourneyRequests_ThenReturnSearchResult() throws Exception {

        SearchJourneyRequestsCommand command = JourneyRequestTestData.defaultSearchJourneyRequestsCommandBuilder()
                .sortingCriterion(null).build();

        JourneyRequestsSearchResult expectedResult = JourneyRequestTestData
                .defaultJourneyRequestsSearchResult(ZoneId.of("Africa/Tunis"));

        given(searchJourneyRequestsUseCase.searchJourneyRequests(any(SearchJourneyRequestsCommand.class),
                any(String.class), any(String.class))).willReturn(expectedResult);

        MvcResult mvcResult = mvc
                .perform(get("/journey-requests").with(jwt().jwt(builder -> builder.claim("sub", TestConstants.DEFAULT_EMAIL)).authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_TRANSPORTER"))))
                        .param("departure", command.getDeparturePlaceDepartmentId().toString())
                        .param("arrival", command.getArrivalPlaceDepartmentIds().stream().map(id -> id.toString())
                                .toArray(String[]::new)

                        ).param("fromDate", command.getStartDateTime().format(DATE_TIME_FORMATTER))
                        .param("toDate", command.getEndDateTime().format(DATE_TIME_FORMATTER))
                        .param("statuses", command.getStatusCodes().stream().map(s -> s.name()).toArray(String[]::new))
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
    void givenValidInputWithNoSortCriterionDirection_WhenTransporterSearchesJourneyRequests_ThenReturnSearchResult() throws Exception {

        SearchJourneyRequestsCommand command = JourneyRequestTestData.defaultSearchJourneyRequestsCommandBuilder()
                .build();

        JourneyRequestsSearchResult expectedResult = JourneyRequestTestData
                .defaultJourneyRequestsSearchResult(ZoneId.of("Africa/Tunis"));

        given(searchJourneyRequestsUseCase.searchJourneyRequests(any(SearchJourneyRequestsCommand.class),
                any(String.class), any(String.class))).willReturn(expectedResult);

        MvcResult mvcResult = mvc
                .perform(get("/journey-requests").with(jwt().jwt(builder -> builder.claim("sub", TestConstants.DEFAULT_EMAIL)).authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_TRANSPORTER"))))
                        .param("departure", command.getDeparturePlaceDepartmentId().toString())
                        .param("arrival", command.getArrivalPlaceDepartmentIds().stream().map(id -> id.toString())
                                .toArray(String[]::new)

                        ).param("fromDate", command.getStartDateTime().format(DATE_TIME_FORMATTER))
                        .param("toDate", command.getEndDateTime().format(DATE_TIME_FORMATTER))
                        .param("engine",
                                command.getEngineTypes().stream().map(e -> e.toString()).collect(Collectors.toSet())
                                        .toArray(new String[command.getEngineTypes().size()]))
                        .param("statuses", command.getStatusCodes().stream().map(s -> s.name()).toArray(String[]::new))
                        .param("page", command.getPageNumber().toString())
                        .param("size", command.getPageSize().toString())
                        .param("sort", command.getSortingCriterion().getField()))

                .andExpect(status().isOk())

                .andReturn();
        String actualResponseBody = mvcResult.getResponse().getContentAsString();

        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(expectedResult));

    }

    @Test
    void givenInvalidInput_WhenTransporterSearchesJourneyRequests_ThenReturnBadRequest() throws Exception {

        SearchJourneyRequestsCommand command = JourneyRequestTestData.defaultSearchJourneyRequestsCommandBuilder()
                .sortingCriterion(new SortCriterion("dummy-field", "up")).build();

        JourneyRequestsSearchResult expectedResult = JourneyRequestTestData
                .defaultJourneyRequestsSearchResult(ZoneId.of("Africa/Tunis"));

        given(searchJourneyRequestsUseCase.searchJourneyRequests(any(SearchJourneyRequestsCommand.class),
                any(String.class), any(String.class))).willReturn(expectedResult);

        mvc.perform(get("/journey-requests").with(jwt().jwt(builder -> builder.claim("sub", TestConstants.DEFAULT_EMAIL)).authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_TRANSPORTER"))))
                        .param("departure", command.getDeparturePlaceDepartmentId().toString())
                        .param("arrival", command.getArrivalPlaceDepartmentIds().stream().map(id -> id.toString())
                                .toArray(String[]::new)

                        ).param("fromDate", command.getStartDateTime().format(DATE_TIME_FORMATTER))
                        .param("toDate", command.getEndDateTime().format(DATE_TIME_FORMATTER)).param("lang", "en")
                        .param("engine",
                                command.getEngineTypes().stream().map(e -> e.toString()).collect(Collectors.toSet())
                                        .toArray(new String[command.getEngineTypes().size()]))
                        .param("statuses", command.getStatusCodes().stream().map(s -> s.name()).toArray(String[]::new))
                        .param("page", command.getPageNumber().toString())
                        .param("size", command.getPageSize().toString()).param("sort",
                                command.getSortingCriterion().getField() + ","
                                        + command.getSortingCriterion().getDirection()))

                .andExpect(status().isBadRequest())
                .andExpect(responseBody().containsApiErrors(List.of(
                        "sortingCriterion: Wrong sort criterion: 'SortCriterion(field=dummy-field, direction=up)'. Valid sort fields are:[distance, date-time]. Valid sort directions are:[asc, desc].")))

                .andReturn();

        then(searchJourneyRequestsUseCase).should(never())
                .searchJourneyRequests(any(SearchJourneyRequestsCommand.class), any(String.class), any(String.class));

    }

}
