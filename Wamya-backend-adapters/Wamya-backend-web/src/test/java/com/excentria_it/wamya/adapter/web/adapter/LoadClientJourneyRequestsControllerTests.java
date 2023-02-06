package com.excentria_it.wamya.adapter.web.adapter;

import com.excentria_it.wamya.adapter.web.WebConfiguration;
import com.excentria_it.wamya.adapter.web.WebSecurityConfiguration;
import com.excentria_it.wamya.adapter.web.utils.ValidationHelper;
import com.excentria_it.wamya.application.port.in.LoadClientJourneyRequestsUseCase;
import com.excentria_it.wamya.application.port.in.LoadClientJourneyRequestsUseCase.LoadJourneyRequestCommand;
import com.excentria_it.wamya.application.port.in.LoadClientJourneyRequestsUseCase.LoadJourneyRequestsCommand;
import com.excentria_it.wamya.common.exception.JourneyRequestNotFoundException;
import com.excentria_it.wamya.common.exception.handlers.RestApiExceptionHandler;
import com.excentria_it.wamya.domain.ClientJourneyRequestDto;
import com.excentria_it.wamya.domain.ClientJourneyRequests;
import com.excentria_it.wamya.test.data.common.JourneyRequestTestData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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

import java.util.Arrays;
import java.util.List;

import static com.excentria_it.wamya.adapter.web.helper.ResponseBodyMatchers.responseBody;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles(profiles = {"web-local"})
@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {WebSecurityConfiguration.class, WebConfiguration.class})
@Import(value = {LoadClientJourneyRequestsController.class, RestApiExceptionHandler.class,
        ValidationHelper.class})
@WebMvcTest(controllers = LoadClientJourneyRequestsController.class)
public class LoadClientJourneyRequestsControllerTests {
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

        MvcResult mvcResult =
                mvc.perform(get("/users/me/journey-requests").with(jwt().jwt(builder -> builder.claim("sub", command.getClientUsername())).authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_CLIENT"))))
                                .param("period", command.getPeriodCriterion().getValue())
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

        mvc.perform(get("/users/me/journey-requests").with(jwt().authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_CLIENT")))).param("period", "not_valid_period").param("page", "0")
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

        mvc.perform(get("/users/me/journey-requests/{journeyRequestId}", 1L).with(jwt().jwt(builder -> builder.claim("sub", command.getClientUsername())).authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_CLIENT")))).param("lang", "fr_FR"))
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
        MvcResult mvcResult = mvc.perform(get("/users/me/journey-requests/{journeyRequestId}", 1L).with(jwt().jwt(builder -> builder.claim("sub", command.getClientUsername())).authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_CLIENT")))).param("lang", "fr_FR"))

                .andExpect(status().isOk()).andReturn();
        // then
        then(loadClientJourneyRequestsUseCase).should(times(1)).loadJourneyRequest(eq(command), eq("fr_FR"));
        String actualResponseBody = mvcResult.getResponse().getContentAsString();

        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(expectedResult));
    }
}
