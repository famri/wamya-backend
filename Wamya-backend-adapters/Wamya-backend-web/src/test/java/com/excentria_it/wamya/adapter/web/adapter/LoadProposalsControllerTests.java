package com.excentria_it.wamya.adapter.web.adapter;

import com.c4_soft.springaddons.security.oauth2.test.mockmvc.MockMvcSupport;
import com.excentria_it.wamya.adapter.web.WebConfiguration;
import com.excentria_it.wamya.adapter.web.WebSecurityConfiguration;
import com.excentria_it.wamya.adapter.web.utils.ValidationHelper;
import com.excentria_it.wamya.application.port.in.LoadProposalsUseCase;
import com.excentria_it.wamya.application.port.in.LoadProposalsUseCase.LoadProposalsCommand;
import com.excentria_it.wamya.common.exception.handlers.RestApiExceptionHandler;
import com.excentria_it.wamya.domain.JourneyRequestProposals;
import com.excentria_it.wamya.test.data.common.JourneyProposalTestData;
import com.excentria_it.wamya.test.data.common.TestConstants;
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
@Import(value = {LoadProposalsController.class, RestApiExceptionHandler.class, MockMvcSupport.class,
        ValidationHelper.class})
@WebMvcTest(controllers = LoadProposalsController.class)
public class LoadProposalsControllerTests {
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

        MvcResult mvcResult = mvc
                .perform(get("/journey-requests/{journeyRequestId}/proposals", command.getJourneyRequestId()).with(jwt().jwt(builder -> builder.claim("sub", TestConstants.DEFAULT_EMAIL)).authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_CLIENT"))))
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

        mvc.perform(get("/journey-requests/{journeyRequestId}/proposals", command.getJourneyRequestId()).with(jwt().jwt(builder -> builder.claim("sub", TestConstants.DEFAULT_EMAIL)).authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_CLIENT"))))
                        .param("page", command.getPageNumber().toString())
                        .param("size", command.getPageSize().toString()).param("sort", "not_valid_sorting_field,asc"))
                .andExpect(responseBody().containsApiErrors(List.of(
                        "sortingCriterion: Wrong sort criterion: 'SortCriterion(field=not_valid_sorting_field, direction=asc)'. Valid sort fields are:[price]. Valid sort directions are:[asc, desc].")))
                .andExpect(status().isBadRequest()).andReturn();

        // then

        then(loadProposalsUseCase).should(never()).loadProposals(any(LoadProposalsCommand.class), any(String.class));

    }
}
