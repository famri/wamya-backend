package com.excentria_it.wamya.adapter.web;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Locale;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.excentria_it.wamya.application.port.in.CreateUserAccountUseCase;
import com.excentria_it.wamya.application.port.in.CreateUserAccountUseCase.CreateUserAccountCommand;
import com.excentria_it.wamya.test.data.common.UserAccountTestData;
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
		CreateUserAccountCommand createUserAccountCommand = UserAccountTestData.defaultCreateUserAccountCommandBuilder()
				.build();
		String createUserAccountJson = gson.toJson(createUserAccountCommand);

		mockMvc.perform(post("/accounts").contentType(MediaType.APPLICATION_JSON).content(createUserAccountJson))
				.andExpect(status().isCreated());

		then(createUserAccountUseCase).should(times(1)).registerUserAccountCreationDemand(eq(createUserAccountCommand),
				any(Locale.class));

	}

}
