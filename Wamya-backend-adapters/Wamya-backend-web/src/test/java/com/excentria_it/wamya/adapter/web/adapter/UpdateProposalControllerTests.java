package com.excentria_it.wamya.adapter.web.adapter;

import com.c4_soft.springaddons.security.oauth2.test.mockmvc.MockMvcSupport;
import com.excentria_it.wamya.adapter.web.WebConfiguration;
import com.excentria_it.wamya.adapter.web.WebSecurityConfiguration;
import com.excentria_it.wamya.application.port.in.UpdateProposalUseCase;
import com.excentria_it.wamya.application.port.in.UpdateProposalUseCase.UpdateProposalCommand;
import com.excentria_it.wamya.common.domain.StatusCode;
import com.excentria_it.wamya.common.exception.handlers.RestApiExceptionHandler;
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
@Import(value = {UpdateProposalController.class, RestApiExceptionHandler.class, MockMvcSupport.class})
@WebMvcTest(controllers = UpdateProposalController.class)
public class UpdateProposalControllerTests {
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
    private UpdateProposalUseCase updateProposalUseCase;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void givenValidInputAndClientRole_WhenAcceptProposal_ThenSucceed() throws Exception {

        UpdateProposalCommand command = new UpdateProposalCommand("accepted");

        String commandJson = objectMapper.writeValueAsString(command);

        mvc.perform(patch("/journey-requests/{journeyRequestId}/proposals/{proposalId}", 1L, 1L).with(jwt().jwt(builder -> builder.claim("sub", TestConstants.DEFAULT_EMAIL)).authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_CLIENT"))))
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(commandJson))
                .andExpect(status().isNoContent()).andReturn();

        then(updateProposalUseCase).should(times(1)).updateProposal(eq(1L), eq(1L), eq(StatusCode.ACCEPTED),
                eq(TestConstants.DEFAULT_EMAIL));

    }

    @Test
    void givenValidInputAndTransporterRole_WhenAcceptProposal_ThenReturnForbidden() throws Exception {

        UpdateProposalCommand command = new UpdateProposalCommand("accepted");

        String commandJson = objectMapper.writeValueAsString(command);

        mvc.perform(patch("/journey-requests/{journeyRequestId}/proposals/{proposalId}", 1L, 1L).with(jwt().jwt(builder -> builder.claim("sub", TestConstants.DEFAULT_EMAIL)).authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_TRANSPORTER"))))
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(commandJson))
                .andExpect(status().isForbidden()).andReturn();

        then(updateProposalUseCase).should(never()).updateProposal(any(Long.class), any(Long.class), any(StatusCode.class),
                any(String.class));

    }
}
