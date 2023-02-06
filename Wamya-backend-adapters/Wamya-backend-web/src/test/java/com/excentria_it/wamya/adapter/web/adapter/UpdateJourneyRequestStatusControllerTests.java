package com.excentria_it.wamya.adapter.web.adapter;

import com.excentria_it.wamya.adapter.web.WebConfiguration;
import com.excentria_it.wamya.adapter.web.WebSecurityConfiguration;
import com.excentria_it.wamya.application.port.in.UpdateJourneyRequestStatusUseCase;
import com.excentria_it.wamya.application.port.in.UpdateJourneyRequestStatusUseCase.UpdateJourneyRequestStatusCommand;
import com.excentria_it.wamya.common.exception.handlers.RestApiExceptionHandler;
import com.excentria_it.wamya.domain.JourneyRequestStatusCode;
import com.excentria_it.wamya.test.data.common.TestConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.times;
import static org.mockito.Mockito.never;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles(profiles = {"web-local"})
@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {WebSecurityConfiguration.class, WebConfiguration.class})
@Import(value = {UpdateJourneyRequestStatusController.class, RestApiExceptionHandler.class})
@WebMvcTest(controllers = UpdateJourneyRequestStatusController.class)
public class UpdateJourneyRequestStatusControllerTests {
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
    private UpdateJourneyRequestStatusUseCase updateJourneyRequestStatusUseCase;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void givenValidInputAndClientRole_WhenUpdateJourneyStatus_ThenSucceed() throws Exception {

        UpdateJourneyRequestStatusCommand command = new UpdateJourneyRequestStatusCommand("canceled");

        String commandJson = objectMapper.writeValueAsString(command);

        mvc.perform(patch("/journey-requests/{id}/status", 1L).with(jwt().jwt(builder -> builder.claim("sub", TestConstants.DEFAULT_EMAIL)).authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_CLIENT")))).param("lang", "fr_FR").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param("lang", "fr_FR")
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(commandJson))
                .andExpect(status().isNoContent()).andReturn();

        then(updateJourneyRequestStatusUseCase).should(times(1)).updateStatus(eq(1L), eq(TestConstants.DEFAULT_EMAIL),
                eq(JourneyRequestStatusCode.CANCELED), eq("fr_FR"));

    }

    @Test
    void givenValidInputAndTransporterRole_WhenUpdateJourneyStatus_ThenReturnForbidden() throws Exception {

        UpdateJourneyRequestStatusCommand command = new UpdateJourneyRequestStatusCommand("canceled");

        String commandJson = objectMapper.writeValueAsString(command);

        mvc.perform(patch("/journey-requests/{id}/status", 1L).with(jwt().jwt(builder -> builder.claim("sub", TestConstants.DEFAULT_EMAIL)).authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_TRANSPORTER")))).param("lang", "fr_FR").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .param("lang", "fr_FR")
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(commandJson))
                .andExpect(status().isForbidden()).andReturn();

        then(updateJourneyRequestStatusUseCase).should(never()).updateStatus(any(Long.class), any(String.class),
                any(JourneyRequestStatusCode.class), any(String.class));

    }

}
