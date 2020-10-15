package com.excentria_it.wamya.adapter.web;

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
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.excentria_it.wamya.application.port.in.CreateUserAccountUseCase;
import com.excentria_it.wamya.application.port.in.CreateUserAccountUseCase.CreateUserAccountCommand;
import com.excentria_it.wamya.common.exception.UnsupportedInternationalCallingCode;
import com.excentria_it.wamya.common.exception.UserAccountAlreadyExistsException;
import com.excentria_it.wamya.domain.UserAccount;
import com.excentria_it.wamya.domain.UserAccount.MobilePhoneNumber;
import com.excentria_it.wamya.test.data.common.UserAccountTestData;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = CreateUserAccountController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
public class CreateUserAccountControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CreateUserAccountUseCase createUserAccountUseCase;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void givenValidInput_WhenCreateUserAccount_ThenReturnCreatedUserAccount() throws Exception {

		CreateUserAccountCommand command = UserAccountTestData.defaultCreateUserAccountCommandBuilder().build();
		given(createUserAccountUseCase.registerUserAccountCreationDemand(eq(command), any(Locale.class)))
				.willReturn(1L);

		String createUserAccountJson = objectMapper.writeValueAsString(command);

		MvcResult mvcResult = mockMvc
				.perform(post("/accounts").contentType(MediaType.APPLICATION_JSON).content(createUserAccountJson))
				.andExpect(status().isCreated()).andReturn();

		then(createUserAccountUseCase).should(times(1)).registerUserAccountCreationDemand(eq(command),
				any(Locale.class));

		UserAccount expectedResponseBody = UserAccount.builder().id(1L).isTransporter(command.getIsTransporter())
				.gender(command.getGender()).firstName(command.getFirstName()).lastName(command.getLastName())
				.dateOfBirth(command.getDateOfBirth()).email(command.getEmail()).emailValidationCode("****")
				.isValidatedEmail(false)
				.mobilePhoneNumber(new MobilePhoneNumber(command.getIcc(), command.getMobileNumber()))
				.mobileNumberValidationCode("****").isValidatedMobileNumber(false).userPassword("********")
				.receiveNewsletter(command.getReceiveNewsletter()).creationTimestamp(null).build();

		String actualResponseBody = mvcResult.getResponse().getContentAsString();

		assertThat(actualResponseBody)
				.isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(expectedResponseBody));

	}

	@Test
	void givenInvalidInput_WhenCreateUserAccount_ThenReturnBadRequest() throws Exception {

		CreateUserAccountCommand createUserAccountCommand = UserAccountTestData.defaultCreateUserAccountCommandBuilder()
				.email("invalid email@test.com").build();

		String createUserAccountJson = objectMapper.writeValueAsString(createUserAccountCommand);

		mockMvc.perform(post("/accounts").contentType(MediaType.APPLICATION_JSON).content(createUserAccountJson))
				.andExpect(status().isBadRequest());

		then(createUserAccountUseCase).should(never()).registerUserAccountCreationDemand(eq(createUserAccountCommand),
				any(Locale.class));

	}

	@Test
	void givenExistentUserAccoun_WhenCreateUserAccount_ThenThrowUserAccountAlreadyExistsException() throws Exception {

		CreateUserAccountCommand command = UserAccountTestData.defaultCreateUserAccountCommandBuilder().build();
		
		doThrow(UserAccountAlreadyExistsException.class).when(createUserAccountUseCase).registerUserAccountCreationDemand(eq(command), any(Locale.class));	

		String createUserAccountJson = objectMapper.writeValueAsString(command);

		mockMvc.perform(post("/accounts").contentType(MediaType.APPLICATION_JSON).content(createUserAccountJson))
				.andExpect(status().isBadRequest())
				.andExpect(responseBody().containsErrors(List.of("User account already exists.")));

	}
	
	@Test
	void givenExistentUserAccoun_WhenCreateUserAccount_ThenThrowUnsupportedInternationalCallingCodeException() throws Exception {

		CreateUserAccountCommand command = UserAccountTestData.defaultCreateUserAccountCommandBuilder().build();
		
		doThrow(UnsupportedInternationalCallingCode.class).when(createUserAccountUseCase).registerUserAccountCreationDemand(eq(command), any(Locale.class));	

		String createUserAccountJson = objectMapper.writeValueAsString(command);

		mockMvc.perform(post("/accounts").contentType(MediaType.APPLICATION_JSON).content(createUserAccountJson))
				.andExpect(status().isBadRequest())
				.andExpect(responseBody().containsErrors(List.of("International calling code is not supported.")));

	}

}
