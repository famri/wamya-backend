package com.excentria_it.wamya.adapter.web.adapter;

import com.excentria_it.wamya.adapter.web.WebConfiguration;
import com.excentria_it.wamya.adapter.web.WebSecurityConfiguration;
import com.excentria_it.wamya.application.port.in.CreateJourneyRequestUseCase.CreateJourneyRequestCommand;
import com.excentria_it.wamya.application.port.in.UpdateJourneyRequestUseCase;
import com.excentria_it.wamya.common.exception.handlers.RestApiExceptionHandler;
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
@Import(value = {UpdateJourneyRequestController.class, RestApiExceptionHandler.class})
@WebMvcTest(controllers = UpdateJourneyRequestController.class)
public class UpdateJourneyRequestControllerTests {

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
    private UpdateJourneyRequestUseCase updateJourneyRequestUseCase;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void givenRoleClient_whenUpdateJourneyRequest_thenSucceed() throws Exception {
        CreateJourneyRequestCommand command = JourneyRequestTestData.defaultCreateJourneyRequestCommandBuilder()
                .build();

        String commandJson = objectMapper.writeValueAsString(command);

        mvc.perform(patch("/journey-requests/{id}", 1L).with(jwt().jwt(builder -> builder.claim("sub", TestConstants.DEFAULT_EMAIL)).authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_CLIENT")))).param("lang", "fr_FR").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(commandJson))
                .andExpect(status().isNoContent()).andReturn();

        then(updateJourneyRequestUseCase).should(times(1)).updateJourneyRequest(1L, command, TestConstants.DEFAULT_EMAIL, "fr_FR");
    }

    @Test
    void givenRoleTransporter_whenUpdateJourneyRequest_thenReturnForbidden() throws Exception {
        CreateJourneyRequestCommand command = JourneyRequestTestData.defaultCreateJourneyRequestCommandBuilder()
                .build();

        String commandJson = objectMapper.writeValueAsString(command);

        mvc.perform(patch("/journey-requests/{id}", 1L).with(jwt().jwt(builder -> builder.claim("sub", TestConstants.DEFAULT_EMAIL)).authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_TRANSPORTER")))).param("lang", "fr_FR").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(commandJson))
                .andExpect(status().isForbidden());

        then(updateJourneyRequestUseCase).should(never()).updateJourneyRequest(any(Long.class), any(CreateJourneyRequestCommand.class), any(String.class), any(String.class));
    }
}
