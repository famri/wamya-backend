package com.excentria_it.wamya.adapter.web.adapter;

import com.excentria_it.wamya.application.port.in.RequestPasswordResetUseCase;
import com.excentria_it.wamya.application.port.in.RequestPasswordResetUseCase.RequestPasswordResetCommand;
import com.excentria_it.wamya.application.port.in.ResetPasswordUseCase;
import com.excentria_it.wamya.application.port.in.ResetPasswordUseCase.ResetPasswordCommand;
import com.excentria_it.wamya.common.exception.handlers.RestApiExceptionHandler;
import com.excentria_it.wamya.common.utils.LocaleUtils;
import com.excentria_it.wamya.test.data.common.TestConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.Locale;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles(profiles = {"web-local"})
@Import(value = {PasswordResetRestController.class, RestApiExceptionHandler.class})
@WebMvcTest(controllers = PasswordResetRestController.class)
public class PasswordResetRestControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ResetPasswordUseCase resetPasswordUseCase;

    @MockBean
    private RequestPasswordResetUseCase requestPasswordResetUseCase;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testRequestPasswordReset() throws Exception {
        // given

        Locale supportedLocale = LocaleUtils.getSupporedLocale(new Locale("fr", "FR"));

        RequestPasswordResetCommand command = new RequestPasswordResetCommand(TestConstants.DEFAULT_EMAIL);
        String requestPasswordResetCommandJson = objectMapper.writeValueAsString(command);

        // when

        mockMvc.perform(post("/accounts/do-request-password-reset").queryParam("lang", "fr_FR")
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(requestPasswordResetCommandJson))
                .andExpect(status().isOk());

        // then

        then(requestPasswordResetUseCase).should(times(1)).requestPasswordReset(eq(TestConstants.DEFAULT_EMAIL),
                eq(supportedLocale));

    }

    @Test
    void givenExistentRequest_whenResetPassword_thenReturnStatusOK() throws Exception {
        // given

        given(resetPasswordUseCase.checkRequest(any(String.class), any(Long.class))).willReturn(true);

        UUID uuid = UUID.randomUUID();
        Instant expiry = Instant.now().plusMillis(3600000);

        ResetPasswordCommand command = new ResetPasswordCommand("MyNewPAssword",
                uuid.toString(), expiry.toEpochMilli());

        String resetPasswordCommandJson = objectMapper.writeValueAsString(command);

        // when
        mockMvc.perform(post("/accounts/password-reset").queryParam("lang", "fr_FR")
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(resetPasswordCommandJson))
                .andExpect(status().isOk());

        then(resetPasswordUseCase).should(times(1)).resetPassword(eq(command.getUuid()), eq(command.getPassword()));

    }

    @Test
    void givenInexistentRequest_whenResetPassword_thenReturnStatusIsNotFound() throws Exception {
        // given

        given(resetPasswordUseCase.checkRequest(any(String.class), any(Long.class))).willReturn(false);

        UUID uuid = UUID.randomUUID();
        Instant expiry = Instant.now().plusMillis(3600000);

        ResetPasswordCommand command = new ResetPasswordCommand("MyNewPAssword",
                uuid.toString(), expiry.toEpochMilli());

        String resetPasswordCommandJson = objectMapper.writeValueAsString(command);

        // when
        mockMvc.perform(post("/accounts/password-reset").queryParam("lang", "fr_FR")
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(resetPasswordCommandJson))
                .andExpect(status().isNotFound());

        then(resetPasswordUseCase).should(never()).resetPassword(any(String.class), any(String.class));

    }

}
