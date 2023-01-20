package com.excentria_it.wamya.adapter.web.adapter;

import com.c4_soft.springaddons.security.oauth2.test.mockmvc.MockMvcSupport;
import com.excentria_it.wamya.adapter.web.WebConfiguration;
import com.excentria_it.wamya.adapter.web.WebSecurityConfiguration;
import com.excentria_it.wamya.application.port.in.SendMessageUseCase;
import com.excentria_it.wamya.application.port.in.SendMessageUseCase.SendMessageCommand;
import com.excentria_it.wamya.application.port.in.SendMessageUseCase.SendMessageCommand.SendMessageCommandBuilder;
import com.excentria_it.wamya.common.exception.handlers.RestApiExceptionHandler;
import com.excentria_it.wamya.domain.LoadDiscussionsDto.MessageDto;
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
import static com.excentria_it.wamya.test.data.common.MessageTestData.defaultMessageDto;
import static com.excentria_it.wamya.test.data.common.MessageTestData.defaultSendMessageCommandBuilder;
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
@Import(value = {SendMessageController.class, RestApiExceptionHandler.class, MockMvcSupport.class})
@WebMvcTest(controllers = SendMessageController.class)
public class SendMessageControllerTests {
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
    private SendMessageUseCase sendMessageUseCase;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void givenValidInput_WhenSendMessage_ThenReturnMessageDto() throws Exception {

        // given
        SendMessageCommandBuilder commandBuilder = defaultSendMessageCommandBuilder();
        SendMessageCommand command = commandBuilder.build();

        String sendMessageCommandJson = objectMapper.writeValueAsString(command);

        MessageDto messageDto = defaultMessageDto();
        given(sendMessageUseCase.sendMessage(any(SendMessageCommand.class), any(Long.class), any(String.class)))
                .willReturn(messageDto);
        // When
        mvc.perform(post("/users/me/discussions/{discussionId}/messages", 1L).with(jwt().jwt(builder -> builder.claim("sub", TestConstants.DEFAULT_EMAIL)).authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_CLIENT"))))

                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(sendMessageCommandJson))
                .andExpect(status().isCreated())
                .andExpect(responseBody().containsObjectAsJson(messageDto, MessageDto.class));

        ArgumentCaptor<SendMessageCommand> captor = ArgumentCaptor.forClass(SendMessageCommand.class);
        // then
        then(sendMessageUseCase).should(times(1)).sendMessage(captor.capture(), eq(1L),
                eq(TestConstants.DEFAULT_EMAIL));

        assertThat(captor.getValue().getContent()).isEqualTo(command.getContent());

    }
}
