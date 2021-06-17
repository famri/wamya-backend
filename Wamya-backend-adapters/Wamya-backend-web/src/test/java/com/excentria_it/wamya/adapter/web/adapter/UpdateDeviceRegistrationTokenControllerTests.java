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
import com.excentria_it.wamya.application.port.in.UpdateDeviceRegistrationTokenUseCase;
import com.excentria_it.wamya.application.port.in.UpdateDeviceRegistrationTokenUseCase.UpdateDeviceRegistrationTokenCommand;
import com.excentria_it.wamya.common.exception.handlers.RestApiExceptionHandler;
import com.excentria_it.wamya.test.data.common.TestConstants;
import com.fasterxml.jackson.databind.ObjectMapper;

@ActiveProfiles(profiles = { "web-local" })
@Import(value = { UpdateDeviceRegistrationTokenController.class, RestApiExceptionHandler.class, MockMvcSupport.class })
@WebMvcTest(controllers = UpdateDeviceRegistrationTokenController.class)
public class UpdateDeviceRegistrationTokenControllerTests {

	@Autowired
	private MockMvcSupport api;

	@MockBean
	private UpdateDeviceRegistrationTokenUseCase updateDeviceRegistrationTokenUseCase;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void testUpdateDeviceRegistrationToken() throws Exception {
		UpdateDeviceRegistrationTokenCommand command = new UpdateDeviceRegistrationTokenCommand(
				"some-device-registration-token");

		String commandJson = objectMapper.writeValueAsString(command);

		api.with(mockAuthentication(JwtAuthenticationToken.class).name(TestConstants.DEFAULT_EMAIL)
				.authorities("SCOPE_profile:write"))
				.perform(patch("/accounts/me/device-token").contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(commandJson))
				.andExpect(status().isNoContent()).andReturn();

		then(updateDeviceRegistrationTokenUseCase).should(times(1)).updateToken("some-device-registration-token",
				TestConstants.DEFAULT_EMAIL);
	}
}
