package com.excentria_it.wamya.adapter.web.adapter;

import com.excentria_it.wamya.adapter.web.WebConfiguration;
import com.excentria_it.wamya.adapter.web.WebSecurityConfiguration;
import com.excentria_it.wamya.application.port.in.MakeProposalUseCase;
import com.excentria_it.wamya.application.port.in.MakeProposalUseCase.MakeProposalCommand;
import com.excentria_it.wamya.common.exception.handlers.RestApiExceptionHandler;
import com.excentria_it.wamya.domain.MakeProposalDto;
import com.excentria_it.wamya.test.data.common.JourneyProposalTestData;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;

import static com.excentria_it.wamya.adapter.web.helper.ResponseBodyMatchers.responseBody;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles(profiles = {"web-local"})
@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {WebSecurityConfiguration.class, WebConfiguration.class})
@Import(value = {MakeProposalController.class, RestApiExceptionHandler.class})
@WebMvcTest(controllers = MakeProposalController.class)
public class MakeProposalControllerTests {
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
    private MakeProposalUseCase makeProposalUseCase;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void givenValidInputAndEmailUsername_WhenMakeProposal_ThenReturnCreateProposal() throws Exception {
        // given
        MakeProposalCommand command = JourneyProposalTestData.defaultMakeProposalCommandBuilder().build();

        String makeProposalJson = objectMapper.writeValueAsString(command);

        MakeProposalDto makeProposalDto = new MakeProposalDto(1L, command.getPrice(), null);

        given(makeProposalUseCase.makeProposal(any(MakeProposalCommand.class), any(Long.class), any(String.class),
                any(String.class))).willReturn(makeProposalDto);
        // when

        MvcResult mvcResult = mvc
                .perform(post("/journey-requests/{id}/proposals", 1L).with(jwt().jwt(builder -> builder.claim("sub", TestConstants.DEFAULT_EMAIL)).authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_TRANSPORTER"))))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(makeProposalJson))
                .andExpect(status().isCreated()).andReturn();

        MakeProposalDto makeProposalDtoResult = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                MakeProposalDto.class);
        // then

        then(makeProposalUseCase).should(times(1)).makeProposal(eq(command), eq(1L), eq(TestConstants.DEFAULT_EMAIL),
                eq("en_US"));
        then(makeProposalDto.getId().equals(makeProposalDtoResult.getId()));
        then(makeProposalDto.getPrice().equals(makeProposalDtoResult.getPrice()));

    }

    @Test
    void givenInvalidInputAndEmailUsername_WhenMakeProposal_ThenReturnBadRequest() throws Exception {
        // given
        MakeProposalCommand command = JourneyProposalTestData.defaultMakeProposalCommandBuilder().price(-250.0).build();

        String makeProposalJson = objectMapper.writeValueAsString(command);

        // when

        mvc.perform(post("/journey-requests/{id}/proposals", 1L).with(jwt().jwt(builder -> builder.claim("sub", TestConstants.DEFAULT_EMAIL)).authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_TRANSPORTER"))))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(makeProposalJson))
                .andExpect(status().isBadRequest())
                .andExpect(responseBody().containsApiErrors(List.of("price: must be greater than or equal to 0")))
                .andReturn();

        // then

        then(makeProposalUseCase).should(never()).makeProposal(any(MakeProposalCommand.class), any(Long.class),
                any(String.class), any(String.class));

    }

}
