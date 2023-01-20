package com.excentria_it.wamya.adapter.web.adapter;

import com.c4_soft.springaddons.security.oauth2.test.mockmvc.MockMvcSupport;
import com.excentria_it.wamya.adapter.web.WebConfiguration;
import com.excentria_it.wamya.adapter.web.WebSecurityConfiguration;
import com.excentria_it.wamya.application.port.in.UpdateMessageReadStatusUseCase;
import com.excentria_it.wamya.application.port.in.UpdateMessageReadStatusUseCase.UpdateMessageReadStatusCommand;
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

import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.times;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles(profiles = {"web-local"})
@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {WebSecurityConfiguration.class, WebConfiguration.class})
@Import(value = {UpdateDiscussionMessageController.class, RestApiExceptionHandler.class, MockMvcSupport.class})
@WebMvcTest(controllers = UpdateDiscussionMessageController.class)
public class UpdateDiscussionMessageControllerTests {
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
    private UpdateMessageReadStatusUseCase updateMessageReadStatusUseCase;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void givenRoleClient_whenUpdateDiscussionMessageReadStatus_thenSucceed() throws Exception {
        UpdateMessageReadStatusCommand command = UpdateMessageReadStatusCommand.builder().isRead("true").build();

        String commandJson = objectMapper.writeValueAsString(command);

        mvc.perform(patch("/users/me/discussions/{discussionId}/messages/{messageId}", 1L, 2L).with(jwt().jwt(builder -> builder.claim("sub", TestConstants.DEFAULT_EMAIL)).authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_CLIENT"))))
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(commandJson))
                .andExpect(status().isNoContent()).andReturn();

        then(updateMessageReadStatusUseCase).should(times(1)).updateMessageReadStatus(1L, 2L,
                TestConstants.DEFAULT_EMAIL, command);
    }

    @Test
    void givenRoleTransporter_whenUpdateDiscussionMessageReadStatus_thenSucceed() throws Exception {
        UpdateMessageReadStatusCommand command = UpdateMessageReadStatusCommand.builder().isRead("true").build();

        String commandJson = objectMapper.writeValueAsString(command);

        mvc.perform(patch("/users/me/discussions/{discussionId}/messages/{messageId}", 1L, 2L).with(jwt().jwt(builder -> builder.claim("sub", TestConstants.DEFAULT_EMAIL)).authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_TRANSPORTER"))))

                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(commandJson))
                .andExpect(status().isNoContent()).andReturn();

        then(updateMessageReadStatusUseCase).should(times(1)).updateMessageReadStatus(1L, 2L,
                TestConstants.DEFAULT_EMAIL, command);
    }
}
