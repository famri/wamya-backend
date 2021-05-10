package com.excentria_it.wamya.adapter.web.adapter;

import static com.c4_soft.springaddons.security.oauth2.test.mockmvc.MockAuthenticationRequestPostProcessor.*;
import static com.excentria_it.wamya.adapter.web.helper.ResponseBodyMatchers.*;
import static com.excentria_it.wamya.test.data.common.MessageTestData.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.test.context.ActiveProfiles;

import com.c4_soft.springaddons.security.oauth2.test.mockmvc.MockMvcSupport;
import com.excentria_it.wamya.application.port.in.SendMessageUseCase;
import com.excentria_it.wamya.application.port.in.SendMessageUseCase.SendMessageCommand;
import com.excentria_it.wamya.application.port.in.SendMessageUseCase.SendMessageCommand.SendMessageCommandBuilder;
import com.excentria_it.wamya.common.exception.handlers.RestApiExceptionHandler;
import com.excentria_it.wamya.domain.LoadDiscussionsDto.MessageDto;
import com.excentria_it.wamya.test.data.common.TestConstants;
import com.fasterxml.jackson.databind.ObjectMapper;

@ActiveProfiles(profiles = { "web-local" })
@Import(value = { SendMessageController.class, RestApiExceptionHandler.class, MockMvcSupport.class })
@WebMvcTest(controllers = SendMessageController.class)
public class SendMessageControllerTests {
	@Autowired
	private MockMvcSupport api;

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
		api.with(mockAuthentication(JwtAuthenticationToken.class).name(TestConstants.DEFAULT_EMAIL)
				.authorities("SCOPE_journey:write"))
				.perform(post("/users/me/discussions/{discussionId}/messages", 1L)
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
