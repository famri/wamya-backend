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
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.test.context.ActiveProfiles;

import com.c4_soft.springaddons.security.oauth2.test.mockmvc.MockMvcSupport;
import com.excentria_it.wamya.application.port.in.LoadProfileInfoUseCase;
import com.excentria_it.wamya.common.exception.handlers.RestApiExceptionHandler;
import com.excentria_it.wamya.test.data.common.TestConstants;

@ActiveProfiles(profiles = { "web-local" })
@Import(value = { LoadProfileInfoController.class, RestApiExceptionHandler.class, MockMvcSupport.class })
@WebMvcTest(controllers = LoadProfileInfoController.class)
public class LoadProfileInfoControllerTests {
	@Autowired
	private MockMvcSupport api;

	@MockBean
	private LoadProfileInfoUseCase loadProfileInfoUseCase;

	@Test
	void testUpdateEmailSection() throws Exception {

		// given // when
		api.with(mockAuthentication(JwtAuthenticationToken.class).name(TestConstants.DEFAULT_EMAIL)
				.authorities("SCOPE_profile:read")).perform(get("/profiles/me/info").queryParam("lang", "fr_FR"))
				.andExpect(status().isOk()).andReturn();

		// then

		then(loadProfileInfoUseCase).should(times(1)).loadProfileInfo(TestConstants.DEFAULT_EMAIL, "fr_FR");

	}
}
