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
import com.excentria_it.wamya.application.port.in.UpdateAboutSectionUseCase;
import com.excentria_it.wamya.application.port.in.UpdateAboutSectionUseCase.UpdateAboutSectionCommand;
import com.excentria_it.wamya.common.exception.handlers.RestApiExceptionHandler;
import com.excentria_it.wamya.test.data.common.TestConstants;
import com.excentria_it.wamya.test.data.common.UserProfileTestData;
import com.fasterxml.jackson.databind.ObjectMapper;

@ActiveProfiles(profiles = { "web-local" })
@Import(value = { UpdateProfileAboutSectionController.class, RestApiExceptionHandler.class, MockMvcSupport.class })
@WebMvcTest(controllers = UpdateProfileAboutSectionController.class)
public class UpdateProfileAboutSectionControllerTests {
	@Autowired
	private MockMvcSupport api;

	@MockBean
	private UpdateAboutSectionUseCase updateAboutSectionUseCase;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void testUpdateEmailSection() throws Exception {

		// given
		UpdateAboutSectionCommand command = UserProfileTestData.defaultUpdateAboutSectionCommandBuilder().build();

		String commandJson = objectMapper.writeValueAsString(command);

		// when

		api.with(mockAuthentication(JwtAuthenticationToken.class).name(TestConstants.DEFAULT_EMAIL)
				.authorities("SCOPE_profile:write"))
				.perform(patch("/profiles/me/about").contentType(MediaType.APPLICATION_JSON_VALUE).content(commandJson))
				.andExpect(status().isNoContent()).andReturn();

		// then

		then(updateAboutSectionUseCase).should(times(1)).updateAboutSection(command, TestConstants.DEFAULT_EMAIL);

	}
}
