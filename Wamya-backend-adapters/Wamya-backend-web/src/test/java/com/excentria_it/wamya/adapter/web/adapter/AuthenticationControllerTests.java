package com.excentria_it.wamya.adapter.web.adapter;

import static com.excentria_it.wamya.adapter.web.helper.ResponseBodyMatchers.*;
import static org.assertj.core.api.Assertions.*;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.excentria_it.wamya.application.port.in.AuthenticateUserUseCase;
import com.excentria_it.wamya.application.port.in.AuthenticateUserUseCase.LoginUserCommand;
import com.excentria_it.wamya.common.exception.AuthorizationException;
import com.excentria_it.wamya.common.exception.handlers.RestApiExceptionHandler;
import com.excentria_it.wamya.domain.OpenIdAuthResponse;
import com.excentria_it.wamya.test.data.common.UserLoginTestData;
import com.fasterxml.jackson.databind.ObjectMapper;

@ActiveProfiles(profiles = { "web-local" })
@Import(value = { AuthenticationController.class, RestApiExceptionHandler.class })
@WebMvcTest(controllers = AuthenticationController.class)
public class AuthenticationControllerTests {

	private static final String ACCESS_TOKEN = "SOME_ACCESS_TOKEN";

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private AuthenticateUserUseCase authenticateUserUseCase;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void givenValidInput_WhenLoginUser_ThenReturnJwtToken() throws Exception {

		LoginUserCommand command = UserLoginTestData.defaultLoginUserCommand().build();

		OpenIdAuthResponse oAuth2AccessToken = new OpenIdAuthResponse();
		oAuth2AccessToken.setAccessToken(ACCESS_TOKEN);
		given(authenticateUserUseCase.loginUser(eq(command))).willReturn(oAuth2AccessToken);

		String loginUserCommandJson = objectMapper.writeValueAsString(command);

		MvcResult mvcResult = mockMvc
				.perform(post("/login").contentType(MediaType.APPLICATION_JSON_VALUE).content(loginUserCommandJson))
				.andExpect(status().isOk()).andReturn();

		then(authenticateUserUseCase).should(times(1)).loginUser(eq(command));

		String actualResponseBody = mvcResult.getResponse().getContentAsString();

		assertThat(actualResponseBody).isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(oAuth2AccessToken));

	}

	@Test
	void givenInvalidEmailUsername_WhenLoginUser_ThenReturnBadRequest() throws Exception {

		LoginUserCommand command = UserLoginTestData.defaultLoginUserCommand().username("toto @ tata").build();

		String loginUserCommandJson = objectMapper.writeValueAsString(command);

		mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON_VALUE).content(loginUserCommandJson))
				.andExpect(status().isBadRequest())
				.andExpect(responseBody().containsApiErrors(List.of("username: Login or password not found.")));

		then(authenticateUserUseCase).should(never()).loginUser(eq(command));

	}

	@Test
	void givenInvalidMobilePhoneNumberUsername_WhenLoginUser_ThenReturnBadRequest() throws Exception {

		LoginUserCommand command = UserLoginTestData.defaultLoginUserCommand().username("+216_991112222").build();

		String loginUserCommandJson = objectMapper.writeValueAsString(command);

		mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON_VALUE).content(loginUserCommandJson))
				.andExpect(status().isBadRequest())
				.andExpect(responseBody().containsApiErrors(List.of("username: Login or password not found.")));

		then(authenticateUserUseCase).should(never()).loginUser(eq(command));

	}

	@Test
	void givenNonExistentUsernameOrPassword_WhenLoginUser_ThenReturnLoginOrPasswordNotFoundError() throws Exception {

		LoginUserCommand command = UserLoginTestData.defaultLoginUserCommand().build();

		doThrow(new AuthorizationException("SOME ERROR DESCRIPTION")).when(authenticateUserUseCase)
				.loginUser(eq(command));

		String loginUserCommandJson = objectMapper.writeValueAsString(command);

		mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON_VALUE).content(loginUserCommandJson))
				.andExpect(status().isUnauthorized())
				.andExpect(responseBody().containsApiErrors(List.of("Bad credentials.")));

		then(authenticateUserUseCase).should(times(1)).loginUser(eq(command));

	}
}
