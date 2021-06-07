package com.excentria_it.wamya.adapter.web.adapter;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Instant;
import java.util.Locale;
import java.util.UUID;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.c4_soft.springaddons.security.oauth2.test.mockmvc.MockMvcSupport;
import com.excentria_it.wamya.application.port.in.ResetPasswordUseCase;
import com.excentria_it.wamya.common.exception.handlers.RestApiExceptionHandler;
import com.excentria_it.wamya.common.utils.LocaleUtils;

@ActiveProfiles(profiles = { "web-local" })
@Import(value = { PasswordResetWebController.class, RestApiExceptionHandler.class, MockMvcSupport.class })
@WebMvcTest(controllers = PasswordResetWebController.class)
public class PasswordResetWebControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ResetPasswordUseCase resetPasswordUseCase;

	@Autowired
	private MessageSource messageSource;

	@Test
	void givenRequestExists_whenShowPasswordResetForm_thenReturnPassowrdResetView() throws Exception {
		// given

		UUID uuid = UUID.randomUUID();
		Instant expiry = Instant.now().plusMillis(3600000);
		given(resetPasswordUseCase.checkRequest(any(String.class), any(Long.class))).willReturn(true);
		// when

		mockMvc.perform(get("/accounts/password-reset").queryParam("lang", "fr_FR").param("uuid", uuid.toString())
				.param("exp", Long.valueOf(expiry.toEpochMilli()).toString())).andExpect(status().isOk())
				.andExpect(model().attribute("uuid", Matchers.equalTo(uuid.toString())))
				.andExpect(model().attribute("exp", Matchers.equalTo(expiry.toEpochMilli())))
				.andExpect(view().name("password-reset")).andReturn();

		// then

		then(resetPasswordUseCase).should(times(1)).checkRequest(eq(uuid.toString()), eq(expiry.toEpochMilli()));

	}

	@Test
	void givenRequestDoesNotExist_whenShowPasswordResetForm_thenReturErrorView() throws Exception {
		// given

		Locale supportedLocale = LocaleUtils.getSupporedLocale(new Locale("fr", "FR"));

		UUID uuid = UUID.randomUUID();
		Instant expiry = Instant.now().plusMillis(3600000);

		given(resetPasswordUseCase.checkRequest(any(String.class), any(Long.class))).willReturn(false);
		String errorMessage = messageSource.getMessage(
				"com.excentria_it.wamya.domain.error.message.reset.password.link.expired", null, supportedLocale);
		// when

		mockMvc.perform(get("/accounts/password-reset").queryParam("lang", "fr_FR").param("uuid", uuid.toString())
				.param("exp", Long.valueOf(expiry.toEpochMilli()).toString())).andExpect(status().isOk())
				.andExpect(model().attribute("error", Matchers.equalTo(errorMessage))).andExpect(view().name("error"))
				.andReturn();

		// then

		then(resetPasswordUseCase).should(times(1)).checkRequest(eq(uuid.toString()), eq(expiry.toEpochMilli()));

	}

}
