package com.excentria_it.wamya.adapter.web.adapter;

import com.c4_soft.springaddons.security.oauth2.test.mockmvc.MockMvcSupport;
import com.excentria_it.wamya.adapter.web.WebConfiguration;
import com.excentria_it.wamya.adapter.web.WebSecurityConfiguration;
import com.excentria_it.wamya.adapter.web.utils.ValidationHelper;
import com.excentria_it.wamya.application.port.in.LoadTransporterProposalsUseCase;
import com.excentria_it.wamya.application.port.in.LoadTransporterProposalsUseCase.LoadTransporterProposalsCommand;
import com.excentria_it.wamya.common.exception.handlers.RestApiExceptionHandler;
import com.excentria_it.wamya.domain.JourneyProposalStatusCode;
import com.excentria_it.wamya.domain.TransporterProposals;
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
import java.util.stream.Collectors;

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
@Import(value = {LoadTransporterProposalsController.class, RestApiExceptionHandler.class, MockMvcSupport.class,
        ValidationHelper.class})
@WebMvcTest(controllers = LoadTransporterProposalsController.class)
public class LoadTransporterProposalsControllerTests {
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
    private LoadTransporterProposalsUseCase loadTransporterProposalsUseCase;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void givenValidInput_WhenLoadTransporterProposals_ThenReturnTransporterProposals() throws Exception {

        // given
        LoadTransporterProposalsCommand command = JourneyProposalTestData
                .defaultLoadTransporterProposalsCommandBuilder().build();

        TransporterProposals transporterProposals = JourneyProposalTestData.defaultTransporterProposals();

        given(loadTransporterProposalsUseCase.loadProposals(any(LoadTransporterProposalsCommand.class),
                any(String.class))).willReturn(transporterProposals);

        ArgumentCaptor<LoadTransporterProposalsCommand> commandCaptor = ArgumentCaptor
                .forClass(LoadTransporterProposalsCommand.class);
        // when
        MvcResult mvcResult = mvc.perform(get("/users/me/proposals").with(jwt().jwt(builder -> builder.claim("sub", TestConstants.DEFAULT_EMAIL)).authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_TRANSPORTER"))))
                        .param("lang", "en_US")
                        .param("page", command.getPageNumber().toString())
                        .param("size", command.getPageSize().toString())
                        .param("sort",
                                command.getSortingCriterion().getField().toLowerCase().concat(",")
                                        .concat(command.getSortingCriterion().getDirection().toLowerCase()))
                        .param("period", "m1").param("statuses",
                                Arrays.stream(JourneyProposalStatusCode.values()).map(s -> s.toString())
                                        .collect(Collectors.toSet()).toArray(String[]::new)))

                .andExpect(status().isOk()).andReturn();

        TransporterProposals response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                TransporterProposals.class);
        // then

        then(loadTransporterProposalsUseCase).should(times(1)).loadProposals(commandCaptor.capture(), eq("en_US"));

        assertThat(commandCaptor.getValue().getTransporterUsername()).isEqualTo(command.getTransporterUsername());
        assertThat(commandCaptor.getValue().getPageNumber()).isEqualTo(command.getPageNumber());
        assertThat(commandCaptor.getValue().getPageSize()).isEqualTo(command.getPageSize());
        assertThat(commandCaptor.getValue().getSortingCriterion().getField())
                .isEqualTo(command.getSortingCriterion().getField());
        assertThat(commandCaptor.getValue().getSortingCriterion().getDirection())
                .isEqualTo(command.getSortingCriterion().getDirection());
        assertThat(commandCaptor.getValue().getStatusCodes()).containsAll(command.getStatusCodes());
        assertThat(commandCaptor.getValue().getStatusCodes().size()).isEqualTo(command.getStatusCodes().size());

        assertEquals(transporterProposals, response);

    }

}
