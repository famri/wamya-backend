package com.excentria_it.wamya.adapter.web.adapter;

import static com.c4_soft.springaddons.security.oauth2.test.mockmvc.MockAuthenticationRequestPostProcessor.*;
import static com.excentria_it.wamya.adapter.web.helper.ResponseBodyMatchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.test.context.ActiveProfiles;

import com.c4_soft.springaddons.security.oauth2.test.mockmvc.MockMvcSupport;
import com.excentria_it.wamya.adapter.web.utils.ValidationHelper;
import com.excentria_it.wamya.application.port.in.CountMessagesUseCase;
import com.excentria_it.wamya.application.port.in.CountMessagesUseCase.CountMessagesCommand;
import com.excentria_it.wamya.common.exception.handlers.RestApiExceptionHandler;
import com.excentria_it.wamya.domain.CountMissedMessagesResult;
import com.excentria_it.wamya.test.data.common.TestConstants;

@ActiveProfiles(profiles = { "web-local" })
@Import(value = { CountMissedMessagesController.class, RestApiExceptionHandler.class, MockMvcSupport.class,
		ValidationHelper.class })
@WebMvcTest(controllers = CountMissedMessagesController.class)
public class CountMissedMessagesControllerTests {

	@Autowired
	private MockMvcSupport api;

	@MockBean
	private CountMessagesUseCase counMessagesUseCase;

	@Test
	void testCountMissedMessages() throws Exception {
		// given
		CountMessagesCommand command = CountMessagesCommand.builder().read("false")
				.username(TestConstants.DEFAULT_EMAIL).build();

		given(counMessagesUseCase.countMessages(any(CountMessagesCommand.class))).willReturn(5L);

		CountMissedMessagesResult result = CountMissedMessagesResult.builder().count(5L).build();

		// when //then
		api.with(mockAuthentication(JwtAuthenticationToken.class).name(TestConstants.DEFAULT_EMAIL)
				.authorities("SCOPE_journey:write")).perform(get("/users/me/messages/count").param("read", "false"))
				.andExpect(status().isOk())
				.andExpect(responseBody().containsObjectAsJson(result, CountMissedMessagesResult.class));

		then(counMessagesUseCase).should(times(1)).countMessages(command);
	}

}
