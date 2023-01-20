package com.excentria_it.wamya.adapter.web.adapter;

import com.c4_soft.springaddons.security.oauth2.test.mockmvc.MockMvcSupport;
import com.excentria_it.wamya.adapter.web.WebConfiguration;
import com.excentria_it.wamya.adapter.web.WebSecurityConfiguration;
import com.excentria_it.wamya.application.port.in.CreateDiscussionUseCase;
import com.excentria_it.wamya.application.port.in.CreateDiscussionUseCase.CreateDiscussionCommand;
import com.excentria_it.wamya.application.port.in.CreateDiscussionUseCase.CreateDiscussionCommand.CreateDiscussionCommandBuilder;
import com.excentria_it.wamya.common.exception.handlers.RestApiExceptionHandler;
import com.excentria_it.wamya.domain.LoadDiscussionsDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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

import static com.excentria_it.wamya.adapter.web.helper.ResponseBodyMatchers.responseBody;
import static com.excentria_it.wamya.test.data.common.DiscussionTestData.defaultCreateDiscussionCommandBuilder;
import static com.excentria_it.wamya.test.data.common.DiscussionTestData.defaultLoadDiscussionsDto;
import static org.assertj.core.api.Assertions.assertThat;
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
@Import(value = {CreateDiscussionController.class, RestApiExceptionHandler.class, MockMvcSupport.class})
@WebMvcTest(controllers = CreateDiscussionController.class)
public class CreateDiscussionControllerTests {

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
    private CreateDiscussionUseCase createDiscussionUseCase;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void givenValidInput_WhenCreateDiscussion_ThenReturnCreatedDiscussion() throws Exception {

        // given
        CreateDiscussionCommandBuilder commandBuilder = defaultCreateDiscussionCommandBuilder();
        CreateDiscussionCommand command = commandBuilder.build();

        String createDiscussionCommandJson = objectMapper.writeValueAsString(command);

        LoadDiscussionsDto loadDiscussionsDto = defaultLoadDiscussionsDto();
        given(createDiscussionUseCase.createDiscussion(any(CreateDiscussionCommand.class), any(String.class)))
                .willReturn(loadDiscussionsDto);
        // When
        mvc.perform(post("/users/me/discussions").with(jwt().authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_TRANSPORTER")))).contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(createDiscussionCommandJson))
                .andExpect(status().isCreated())
                .andExpect(responseBody().containsObjectAsJson(loadDiscussionsDto, LoadDiscussionsDto.class));

        ArgumentCaptor<CreateDiscussionCommand> captor = ArgumentCaptor.forClass(CreateDiscussionCommand.class);
        // then
        then(createDiscussionUseCase).should(times(1)).createDiscussion(captor.capture(),
                eq("user"));

        assertThat(captor.getValue().getClientId()).isEqualTo(command.getClientId());
        assertThat(captor.getValue().getTransporterId()).isEqualTo(command.getTransporterId());

    }
}
