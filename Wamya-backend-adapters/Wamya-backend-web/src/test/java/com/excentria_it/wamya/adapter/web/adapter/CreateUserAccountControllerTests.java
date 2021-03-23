package com.excentria_it.wamya.adapter.web.adapter;

import static com.excentria_it.wamya.adapter.web.helper.ResponseBodyMatchers.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Locale;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.excentria_it.wamya.application.port.in.CreateUserAccountUseCase;
import com.excentria_it.wamya.application.port.in.CreateUserAccountUseCase.CreateUserAccountCommand;
import com.excentria_it.wamya.common.exception.RestApiExceptionHandler;
import com.excentria_it.wamya.common.exception.UnsupportedInternationalCallingCode;
import com.excentria_it.wamya.common.exception.UserAccountAlreadyExistsException;
import com.excentria_it.wamya.domain.JwtOAuth2AccessToken;
import com.excentria_it.wamya.test.data.common.UserAccountTestData;
import com.fasterxml.jackson.databind.ObjectMapper;

@ActiveProfiles(profiles = { "web-local" })
@Import(value = { CreateUserAccountController.class, RestApiExceptionHandler.class })
@WebMvcTest(controllers = CreateUserAccountController.class)
public class CreateUserAccountControllerTests {

	private static final String ACCESS_TOKEN = "SOME_ACCESS_TOKEN";

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CreateUserAccountUseCase createUserAccountUseCase;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void givenValidInput_WhenCreateUserAccount_ThenReturnJwtToken() throws Exception {

		CreateUserAccountCommand command = UserAccountTestData.defaultClientUserAccountCommandBuilder().build();

		JwtOAuth2AccessToken oAuth2AccessToken = new JwtOAuth2AccessToken();
		oAuth2AccessToken.setAccessToken(ACCESS_TOKEN);
		given(createUserAccountUseCase.registerUserAccountCreationDemand(eq(command), any(Locale.class)))
				.willReturn(oAuth2AccessToken);

		String createUserAccountJson = objectMapper.writeValueAsString(command);

		MvcResult mvcResult = mockMvc
				.perform(post("/accounts").contentType(MediaType.APPLICATION_JSON_VALUE).content(createUserAccountJson))
				.andExpect(status().isCreated()).andReturn();

		then(createUserAccountUseCase).should(times(1)).registerUserAccountCreationDemand(eq(command),
				any(Locale.class));

		String actualResponseBody = mvcResult.getResponse().getContentAsString();

		assertThat(actualResponseBody).isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(oAuth2AccessToken));

	}

	@Test
	void givenInvalidInput_WhenCreateUserAccount_ThenReturnBadRequest() throws Exception {

		CreateUserAccountCommand createUserAccountCommand = UserAccountTestData.defaultClientUserAccountCommandBuilder()
				.email("invalid email@test.com").build();

		String createUserAccountJson = objectMapper.writeValueAsString(createUserAccountCommand);

		mockMvc.perform(post("/accounts").contentType(MediaType.APPLICATION_JSON_VALUE).content(createUserAccountJson))
				.andExpect(status().isBadRequest());

		then(createUserAccountUseCase).should(never()).registerUserAccountCreationDemand(eq(createUserAccountCommand),
				any(Locale.class));

	}

	@Test
	void givenExistentUserAccount_WhenCreateUserAccount_ThenReturnExistentUserAccountError() throws Exception {

		CreateUserAccountCommand command = UserAccountTestData.defaultClientUserAccountCommandBuilder().build();

		doThrow(UserAccountAlreadyExistsException.class).when(createUserAccountUseCase)
				.registerUserAccountCreationDemand(eq(command), any(Locale.class));

		String createUserAccountJson = objectMapper.writeValueAsString(command);

		mockMvc.perform(post("/accounts").contentType(MediaType.APPLICATION_JSON_VALUE).content(createUserAccountJson))
				.andExpect(status().isBadRequest())
				.andExpect(responseBody().containsApiErrors(List.of("User account already exists.")));

	}

	@Test
	void givenNonExistentInternationalCallingCode_WhenCreateUserAccount_ThenReturnUnsupportedInternationalCallingCodeError()
			throws Exception {

		CreateUserAccountCommand command = UserAccountTestData.defaultClientUserAccountCommandBuilder().build();

		doThrow(UnsupportedInternationalCallingCode.class).when(createUserAccountUseCase)
				.registerUserAccountCreationDemand(eq(command), any(Locale.class));

		String createUserAccountJson = objectMapper.writeValueAsString(command);

		mockMvc.perform(post("/accounts").contentType(MediaType.APPLICATION_JSON_VALUE).content(createUserAccountJson))
				.andExpect(status().isBadRequest())
				.andExpect(responseBody().containsApiErrors(List.of("International calling code is not supported.")));

	}

}
