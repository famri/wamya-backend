package com.excentria_it.wamya.adapter.web.adapter;

import static com.c4_soft.springaddons.security.oauth2.test.mockmvc.MockAuthenticationRequestPostProcessor.*;
import static org.junit.jupiter.api.Assertions.*;
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
import org.springframework.test.web.servlet.MvcResult;

import com.c4_soft.springaddons.security.oauth2.test.mockmvc.MockMvcSupport;
import com.excentria_it.wamya.application.port.in.ValidateMobileValidationCodeUseCase;
import com.excentria_it.wamya.application.port.in.ValidateMobileValidationCodeUseCase.ValidateMobileValidationCodeCommand;
import com.excentria_it.wamya.common.exception.handlers.RestApiExceptionHandler;
import com.excentria_it.wamya.domain.CodeValidationResult;
import com.excentria_it.wamya.test.data.common.TestConstants;
import com.fasterxml.jackson.databind.ObjectMapper;

@ActiveProfiles(profiles = { "web-local" })
@Import(value = { ValidateMobileValidationCodeController.class, RestApiExceptionHandler.class, MockMvcSupport.class })
@WebMvcTest(controllers = ValidateMobileValidationCodeController.class)
public class ValidateMobileValidationCodeControllerTests {
	@Autowired
	private MockMvcSupport api;

	@MockBean
	private ValidateMobileValidationCodeUseCase validateMobileValidationCodeUseCase;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void givenValidCode_WhenValidateMobileValidationCode_ThenReturnTrueCodeValidationResult() throws Exception {
		// given
		ValidateMobileValidationCodeCommand command = new ValidateMobileValidationCodeCommand("1234");

		given(validateMobileValidationCodeUseCase.validateCode(command, TestConstants.DEFAULT_EMAIL)).willReturn(true);

		String commandJson = objectMapper.writeValueAsString(command);
		// when
		MvcResult mvcResult = api
				.with(mockAuthentication(JwtAuthenticationToken.class).name(TestConstants.DEFAULT_EMAIL)
						.authorities("SCOPE_profile:write"))
				.perform(post("/validation-codes/sms/validate", 1L).contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(commandJson))
				.andExpect(status().isOk()).andReturn();

		// then
		then(validateMobileValidationCodeUseCase).should(times(1)).validateCode(command, TestConstants.DEFAULT_EMAIL);

		CodeValidationResult response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
				CodeValidationResult.class);
		assertTrue(response.isValid());

	}
	@Test
	void givenInvalidCode_WhenValidateMobileValidationCode_ThenReturnTrueCodeValidationResult() throws Exception {
		// given
		ValidateMobileValidationCodeCommand command = new ValidateMobileValidationCodeCommand("1234");

		given(validateMobileValidationCodeUseCase.validateCode(command, TestConstants.DEFAULT_EMAIL)).willReturn(false);

		String commandJson = objectMapper.writeValueAsString(command);
		// when
		MvcResult mvcResult = api
				.with(mockAuthentication(JwtAuthenticationToken.class).name(TestConstants.DEFAULT_EMAIL)
						.authorities("SCOPE_profile:write"))
				.perform(post("/validation-codes/sms/validate", 1L).contentType(MediaType.APPLICATION_JSON_VALUE)
						.content(commandJson))
				.andExpect(status().isOk()).andReturn();

		// then
		then(validateMobileValidationCodeUseCase).should(times(1)).validateCode(command, TestConstants.DEFAULT_EMAIL);

		CodeValidationResult response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
				CodeValidationResult.class);
		assertFalse(response.isValid());

	}
}
