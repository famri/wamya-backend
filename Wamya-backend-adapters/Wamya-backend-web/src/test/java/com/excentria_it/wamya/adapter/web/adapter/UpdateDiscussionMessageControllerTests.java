package com.excentria_it.wamya.adapter.web.adapter;

import static com.c4_soft.springaddons.security.oauth2.test.mockmvc.MockAuthenticationRequestPostProcessor.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.test.context.ActiveProfiles;

import com.c4_soft.springaddons.security.oauth2.test.mockmvc.MockMvcSupport;
import com.excentria_it.wamya.application.port.in.UpdateMessageReadStatusUseCase;
import com.excentria_it.wamya.application.port.in.UpdateMessageReadStatusUseCase.UpdateMessageReadStatusCommand;
import com.excentria_it.wamya.common.exception.handlers.RestApiExceptionHandler;
import com.excentria_it.wamya.test.data.common.TestConstants;
import com.fasterxml.jackson.databind.ObjectMapper;

@ActiveProfiles(profiles = { "web-local" })
@Import(value = { UpdateDiscussionMessageController.class, RestApiExceptionHandler.class, MockMvcSupport.class })
@WebMvcTest(controllers = UpdateDiscussionMessageController.class)
public class UpdateDiscussionMessageControllerTests {
	@Autowired
	private MockMvcSupport api;

	@MockBean
	private UpdateMessageReadStatusUseCase updateMessageReadStatusUseCase;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void testUpdateDiscussionMessageReadStatus() throws Exception {
		UpdateMessageReadStatusCommand command = UpdateMessageReadStatusCommand.builder().isRead("true").build();

		String commandJson = objectMapper.writeValueAsString(command);

		api.with(mockAuthentication(JwtAuthenticationToken.class).name(TestConstants.DEFAULT_EMAIL)
				.authorities("SCOPE_journey:write"))
				.perform(patch("/users/me/discussions/{discussionId}/messages/{messageId}", 1L, 2L)
						.contentType(MediaType.APPLICATION_JSON_VALUE).content(commandJson))
				.andExpect(status().isNoContent()).andReturn();

		then(updateMessageReadStatusUseCase).should(times(1)).updateMessageReadStatus(1L, 2L,
				TestConstants.DEFAULT_EMAIL, command);
	}
}
