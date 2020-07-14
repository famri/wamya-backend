package com.codisiac.wamya.adapter.web;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.codisiac.wamya.adapter.web.dto.CreateUserAccountDto;
import com.codisiac.wamya.application.port.in.CreateUserAccountUseCase;
import com.codisiac.wamya.application.port.in.CreateUserAccountUseCase.CreateUserAccountCommand;
import com.codisiac.wamya.common.UserAccountTestData;
import com.codisiac.wamya.domain.UserAccount.MobilePhoneNumber;
import com.codisiac.wamya.domain.UserAccount.UserPasswordPair;
import com.google.gson.Gson;

@WebMvcTest(controllers = CreateUserAccountController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
class CreateUserAccountControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CreateUserAccountUseCase createUserAccountUseCase;

	@Test
	void testCreateUserAccount() throws Exception {
		Gson gson = new Gson();
		CreateUserAccountDto createUserAccountDto = new CreateUserAccountDto(
				UserAccountTestData.DEFAULT_INTERNATIONAL_CALLING_CODE, UserAccountTestData.DEFAULT_MOBILE_NUMBER,
				UserAccountTestData.DEFAULT_RAW_PASSWORD, UserAccountTestData.DEFAULT_RAW_PASSWORD);
		String createUserAccountJson = gson.toJson(createUserAccountDto);
		mockMvc.perform(post("/accounts").contentType(MediaType.APPLICATION_JSON).content(createUserAccountJson))
				.andExpect(status().isCreated());

		then(createUserAccountUseCase).should()
				.registerUserAccountCreationDemand(eq(new CreateUserAccountCommand(
						new MobilePhoneNumber(UserAccountTestData.DEFAULT_INTERNATIONAL_CALLING_CODE,
								UserAccountTestData.DEFAULT_MOBILE_NUMBER),
						new UserPasswordPair(UserAccountTestData.DEFAULT_RAW_PASSWORD,
								UserAccountTestData.DEFAULT_RAW_PASSWORD))));

	}

}
