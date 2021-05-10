package com.excentria_it.wamya.adapter.web.adapter;

import static com.c4_soft.springaddons.security.oauth2.test.mockmvc.MockAuthenticationRequestPostProcessor.*;
import static com.excentria_it.wamya.adapter.web.helper.ResponseBodyMatchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.test.context.ActiveProfiles;

import com.c4_soft.springaddons.security.oauth2.test.mockmvc.MockMvcSupport;
import com.excentria_it.wamya.application.port.in.SaveUserPreferenceUseCase;
import com.excentria_it.wamya.application.port.in.SaveUserPreferenceUseCase.SaveUserPreferenceCommand;
import com.excentria_it.wamya.common.exception.handlers.RestApiExceptionHandler;
import com.excentria_it.wamya.test.data.common.TestConstants;
import com.fasterxml.jackson.databind.ObjectMapper;

@ActiveProfiles(profiles = { "web-local" })
@Import(value = { UserPreferencesController.class, RestApiExceptionHandler.class, MockMvcSupport.class })
@WebMvcTest(controllers = UserPreferencesController.class)
public class UserPreferencesControllerTests {
	@Autowired
	private MockMvcSupport api;

	@MockBean
	private SaveUserPreferenceUseCase saveUserPreferenceUseCase;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void givenValidInput_WhenCreateUserPreference_ThenSucceed() throws Exception {

		SaveUserPreferenceCommand command = new SaveUserPreferenceCommand("timezone", "Africa/Tunis");

		String commandJson = objectMapper.writeValueAsString(command);

		api.with(mockAuthentication(JwtAuthenticationToken.class).name(TestConstants.DEFAULT_EMAIL)
				.authorities("SCOPE_offer:write"))
				.perform(post("/user-preferences").contentType(MediaType.APPLICATION_JSON_VALUE).content(commandJson))
				.andExpect(status().isCreated()).andReturn();

		then(saveUserPreferenceUseCase).should(times(1)).saveUserPreference(command.getKey(), command.getValue(),
				TestConstants.DEFAULT_EMAIL);

	}

	@Test
	void givenInvalidInput_WhenCreateUserPreference_ThenBadRequest() throws Exception {

		SaveUserPreferenceCommand command = new SaveUserPreferenceCommand("bad key", "Africa/Tunis");

		String commandJson = objectMapper.writeValueAsString(command);

		api.with(mockAuthentication(JwtAuthenticationToken.class).name(TestConstants.DEFAULT_EMAIL)
				.authorities("SCOPE_offer:write"))
				.perform(post("/user-preferences").contentType(MediaType.APPLICATION_JSON_VALUE).content(commandJson))
				.andExpect(status().isBadRequest()).andExpect(responseBody()
						.containsApiErrors(List.of("key: Wrong value: 'bad key'. Valid values are: [timezone].")));

		then(saveUserPreferenceUseCase).should(never()).saveUserPreference(any(String.class), any(String.class),
				any(String.class));

	}

}
