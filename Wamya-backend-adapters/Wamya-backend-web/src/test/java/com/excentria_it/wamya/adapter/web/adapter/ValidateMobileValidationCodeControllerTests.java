package com.excentria_it.wamya.adapter.web.adapter;

import com.excentria_it.wamya.adapter.web.WebConfiguration;
import com.excentria_it.wamya.adapter.web.WebSecurityConfiguration;
import com.excentria_it.wamya.application.port.in.ValidateMobileValidationCodeUseCase;
import com.excentria_it.wamya.application.port.in.ValidateMobileValidationCodeUseCase.ValidateMobileValidationCodeCommand;
import com.excentria_it.wamya.common.exception.handlers.RestApiExceptionHandler;
import com.excentria_it.wamya.domain.CodeValidationResult;
import com.excentria_it.wamya.test.data.common.TestConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles(profiles = {"web-local"})
@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {WebSecurityConfiguration.class, WebConfiguration.class})
@Import(value = {ValidateMobileValidationCodeController.class, RestApiExceptionHandler.class})
@WebMvcTest(controllers = ValidateMobileValidationCodeController.class)
public class ValidateMobileValidationCodeControllerTests {
    @Autowired
    private WebApplicationContext context;
    private static MockMvc mvc;

    @BeforeEach
    void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @MockBean
    private ValidateMobileValidationCodeUseCase validateMobileValidationCodeUseCase;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void givenValidCodeAndRoleTransporter_WhenValidateMobileValidationCode_ThenReturnTrueCodeValidationResult() throws Exception {
        // given
        ValidateMobileValidationCodeCommand command = new ValidateMobileValidationCodeCommand("1234");

        given(validateMobileValidationCodeUseCase.validateCode(command, TestConstants.DEFAULT_EMAIL)).willReturn(true);

        String commandJson = objectMapper.writeValueAsString(command);
        // when
        MvcResult mvcResult = mvc
                .perform(post("/validation-codes/sms/validate", 1L).with(jwt().jwt(builder -> builder.claim("sub", TestConstants.DEFAULT_EMAIL)).authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_TRANSPORTER"))))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(commandJson))
                .andExpect(status().isOk()).andReturn();

        // then
        then(validateMobileValidationCodeUseCase).should(times(1)).validateCode(command, TestConstants.DEFAULT_EMAIL);

        CodeValidationResult response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                CodeValidationResult.class);
        assertTrue(response.isValid());

    }

    @Test
    void givenValidCodeAndRoleClient_WhenValidateMobileValidationCode_ThenReturnTrueCodeValidationResult() throws Exception {
        // given
        ValidateMobileValidationCodeCommand command = new ValidateMobileValidationCodeCommand("1234");

        given(validateMobileValidationCodeUseCase.validateCode(command, TestConstants.DEFAULT_EMAIL)).willReturn(true);

        String commandJson = objectMapper.writeValueAsString(command);
        // when
        MvcResult mvcResult = mvc
                .perform(post("/validation-codes/sms/validate", 1L).with(jwt().jwt(builder -> builder.claim("sub", TestConstants.DEFAULT_EMAIL)).authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_CLIENT"))))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(commandJson))
                .andExpect(status().isOk()).andReturn();

        // then
        then(validateMobileValidationCodeUseCase).should(times(1)).validateCode(command, TestConstants.DEFAULT_EMAIL);

        CodeValidationResult response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                CodeValidationResult.class);
        assertTrue(response.isValid());

    }


    @Test
    void givenValidCodeAndBadRole_WhenValidateMobileValidationCode_ThenReturnForbidden() throws Exception {
        // given
        ValidateMobileValidationCodeCommand command = new ValidateMobileValidationCodeCommand("1234");

        given(validateMobileValidationCodeUseCase.validateCode(command, TestConstants.DEFAULT_EMAIL)).willReturn(true);

        String commandJson = objectMapper.writeValueAsString(command);
        // when
        mvc.perform(post("/validation-codes/sms/validate", 1L).with(jwt().jwt(builder -> builder.claim("sub", TestConstants.DEFAULT_EMAIL)).authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_BAD_ROLE"))))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(commandJson))
                .andExpect(status().isForbidden());

        // then
        then(validateMobileValidationCodeUseCase).should(never()).validateCode(any(ValidateMobileValidationCodeCommand.class), any(String.class));

    }

    @Test
    void givenInvalidCodeAndTransporterRole_WhenValidateMobileValidationCode_ThenReturnTrueCodeValidationResult() throws Exception {
        // given
        ValidateMobileValidationCodeCommand command = new ValidateMobileValidationCodeCommand("1234");

        given(validateMobileValidationCodeUseCase.validateCode(command, TestConstants.DEFAULT_EMAIL)).willReturn(false);

        String commandJson = objectMapper.writeValueAsString(command);
        // when
        MvcResult mvcResult = mvc.perform(post("/validation-codes/sms/validate", 1L).with(jwt().jwt(builder -> builder.claim("sub", TestConstants.DEFAULT_EMAIL)).authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_TRANSPORTER"))))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(commandJson))
                .andExpect(status().isOk()).andReturn();

        // then
        then(validateMobileValidationCodeUseCase).should(times(1)).validateCode(command, TestConstants.DEFAULT_EMAIL);

        CodeValidationResult response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                CodeValidationResult.class);
        assertFalse(response.isValid());

    }

    @Test
    void givenInvalidCodeAndClientRole_WhenValidateMobileValidationCode_ThenReturnTrueCodeValidationResult() throws Exception {
        // given
        ValidateMobileValidationCodeCommand command = new ValidateMobileValidationCodeCommand("1234");

        given(validateMobileValidationCodeUseCase.validateCode(command, TestConstants.DEFAULT_EMAIL)).willReturn(false);

        String commandJson = objectMapper.writeValueAsString(command);
        // when
        MvcResult mvcResult = mvc.perform(post("/validation-codes/sms/validate", 1L).with(jwt().jwt(builder -> builder.claim("sub", TestConstants.DEFAULT_EMAIL)).authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_CLIENT"))))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(commandJson))
                .andExpect(status().isOk()).andReturn();

        // then
        then(validateMobileValidationCodeUseCase).should(times(1)).validateCode(command, TestConstants.DEFAULT_EMAIL);

        CodeValidationResult response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                CodeValidationResult.class);
        assertFalse(response.isValid());

    }

    @Test
    void givenInvalidCodeAndBadRole_WhenValidateMobileValidationCode_ThenReturnForbidden() throws Exception {
        // given
        ValidateMobileValidationCodeCommand command = new ValidateMobileValidationCodeCommand("1234");

        given(validateMobileValidationCodeUseCase.validateCode(command, TestConstants.DEFAULT_EMAIL)).willReturn(false);

        String commandJson = objectMapper.writeValueAsString(command);
        // when
        mvc.perform(post("/validation-codes/sms/validate", 1L).with(jwt().jwt(builder -> builder.claim("sub", TestConstants.DEFAULT_EMAIL)).authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_BAD_ROLE"))))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(commandJson))
                .andExpect(status().isForbidden());

        // then
        then(validateMobileValidationCodeUseCase).should(never()).validateCode(any(ValidateMobileValidationCodeCommand.class), any(String.class));

    }

}
