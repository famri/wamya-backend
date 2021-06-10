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
import com.excentria_it.wamya.application.port.in.UpdateEmailSectionUseCase;
import com.excentria_it.wamya.application.port.in.UpdateEmailSectionUseCase.UpdateEmailSectionCommand;
import com.excentria_it.wamya.common.exception.handlers.RestApiExceptionHandler;
import com.excentria_it.wamya.test.data.common.TestConstants;
import com.excentria_it.wamya.test.data.common.UserProfileTestData;
import com.fasterxml.jackson.databind.ObjectMapper;

@ActiveProfiles(profiles = { "web-local" })
@Import(value = { UpdateProfileEmailSectionController.class, RestApiExceptionHandler.class, MockMvcSupport.class })
@WebMvcTest(controllers = UpdateProfileEmailSectionController.class)
public class UpdateProfileEmailSectionControllerTests {
	@Autowired
	private MockMvcSupport api;

	@MockBean
	private UpdateEmailSectionUseCase updateEmailSectionUseCase;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void testUpdateEmailSection() throws Exception {

		// given
		UpdateEmailSectionCommand command = UserProfileTestData.defaultUpdateEmailSectionCommandBuilder().build();

		String commandJson = objectMapper.writeValueAsString(command);

		// when

		api.with(mockAuthentication(JwtAuthenticationToken.class).name(TestConstants.DEFAULT_EMAIL)
				.authorities("SCOPE_profile:write"))
				.perform(patch("/profiles/me/email").contentType(MediaType.APPLICATION_JSON_VALUE).content(commandJson))
				.andExpect(status().isOk()).andReturn();

		// then

		then(updateEmailSectionUseCase).should(times(1)).updateEmailSection(command, TestConstants.DEFAULT_EMAIL);

	}
}
