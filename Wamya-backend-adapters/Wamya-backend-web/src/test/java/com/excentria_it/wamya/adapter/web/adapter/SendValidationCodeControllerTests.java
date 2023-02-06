package com.excentria_it.wamya.adapter.web.adapter;

import com.excentria_it.wamya.adapter.web.WebConfiguration;
import com.excentria_it.wamya.adapter.web.WebSecurityConfiguration;
import com.excentria_it.wamya.adapter.web.domain.ValidationCodeRequest;
import com.excentria_it.wamya.adapter.web.domain.ValidationCodeRequestStatus;
import com.excentria_it.wamya.application.port.in.SendValidationCodeUseCase;
import com.excentria_it.wamya.application.port.in.SendValidationCodeUseCase.SendEmailValidationLinkCommand;
import com.excentria_it.wamya.application.port.in.SendValidationCodeUseCase.SendSMSValidationCodeCommand;
import com.excentria_it.wamya.common.exception.ApiError;
import com.excentria_it.wamya.common.exception.ApiError.ErrorCode;
import com.excentria_it.wamya.common.exception.UserAccountNotFoundException;
import com.excentria_it.wamya.common.exception.UserEmailValidationException;
import com.excentria_it.wamya.common.exception.UserMobileNumberValidationException;
import com.excentria_it.wamya.common.exception.handlers.RestApiExceptionHandler;
import com.excentria_it.wamya.test.data.common.TestConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static com.excentria_it.wamya.adapter.web.helper.ResponseBodyMatchers.responseBody;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles(profiles = {"web-local"})
@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {WebSecurityConfiguration.class, WebConfiguration.class})
@Import(value = {SendValidationCodeController.class, RestApiExceptionHandler.class})
@WebMvcTest(controllers = SendValidationCodeController.class)
public class SendValidationCodeControllerTests {

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
    private SendValidationCodeUseCase sendValidationCodeUseCase;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MessageSource validationMessageSource;

    // Test sendSMSValidaionCode
    @Test
    void givenValidInput_WhenSendSMSValidationCode_ThenSendSMSValidationCode() throws Exception {

        // Given
        SendSMSValidationCodeCommand command = SendSMSValidationCodeCommand.builder()
                .icc(TestConstants.DEFAULT_INTERNATIONAL_CALLING_CODE).mobileNumber(TestConstants.DEFAULT_MOBILE_NUMBER)
                .build();

        given(sendValidationCodeUseCase.sendSMSValidationCode(eq(command), any(Locale.class))).willReturn(true);

        // When
        String commandJson = objectMapper.writeValueAsString(command);
        ValidationCodeRequest expectedResponse = new ValidationCodeRequest(ValidationCodeRequestStatus.REGISTRED);

        mvc.perform(
                        post("/validation-codes/sms/send").with(jwt().jwt(builder -> builder.claim("sub", TestConstants.DEFAULT_EMAIL)).authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_CLIENT"))))
                                .contentType(MediaType.APPLICATION_JSON).content(commandJson))
                .andExpect(status().isAccepted())
                .andExpect(responseBody().containsObjectAsJson(expectedResponse, ValidationCodeRequest.class));

        // Then
        then(sendValidationCodeUseCase).should(times(1)).sendSMSValidationCode(eq(command), any(Locale.class));

    }

    @Test
    void givenValidInputAndBadAuthority_WhenSendSMSValidationCode_ThenReturnForbidden() throws Exception {

        // Given
        SendSMSValidationCodeCommand command = SendSMSValidationCodeCommand.builder()
                .icc(TestConstants.DEFAULT_INTERNATIONAL_CALLING_CODE).mobileNumber(TestConstants.DEFAULT_MOBILE_NUMBER)
                .build();

        given(sendValidationCodeUseCase.sendSMSValidationCode(eq(command), any(Locale.class))).willReturn(true);

        // When
        String commandJson = objectMapper.writeValueAsString(command);


        mvc.perform(
                        post("/validation-codes/sms/send").with(jwt().jwt(builder -> builder.claim("sub", TestConstants.DEFAULT_EMAIL)).authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_BAD_ROLE"))))
                                .contentType(MediaType.APPLICATION_JSON).content(commandJson))
                .andExpect(status().isForbidden());

        // Then
        then(sendValidationCodeUseCase).should(never()).sendSMSValidationCode(eq(command), any(Locale.class));

    }

    @Test
    void givenSendSMSValidationCodeThrowsUserMobileNumberValidationException_WhenSendSMSValidationCode_ThenReturn400AndApiError()
            throws Exception {

        // Given
        SendSMSValidationCodeCommand command = SendSMSValidationCodeCommand.builder()
                .icc(TestConstants.DEFAULT_INTERNATIONAL_CALLING_CODE).mobileNumber(TestConstants.DEFAULT_MOBILE_NUMBER)
                .build();

        given(sendValidationCodeUseCase.sendSMSValidationCode(eq(command), any(Locale.class)))
                .willThrow(new UserMobileNumberValidationException("Some error message."));

        // When, Then
        String commandJson = objectMapper.writeValueAsString(command);

        ApiError expectedApiError = new ApiError(HttpStatus.BAD_REQUEST, ErrorCode.MOBILE_VALIDATION,
                "Some error message.");

        mvc.perform(
                        post("/validation-codes/sms/send").with(jwt().jwt(builder -> builder.claim("sub", TestConstants.DEFAULT_EMAIL)).authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_CLIENT"))))
                                .contentType(MediaType.APPLICATION_JSON).content(commandJson))
                .andExpect(status().isBadRequest())
                .andExpect(responseBody().containsObjectAsJson(expectedApiError, ApiError.class));

    }

    @Test
    void givenInvalidInput_WhenSendSMSValidationCode_ThenReturnBadRequest() throws Exception {

        String message = validationMessageSource.getMessage("com.excentria_it.wamya.domain.mobilephone.number.message",
                null, "Default", new Locale("en"));
        System.out.println("FOUND com.excentria_it.wamya.domain.mobilephone.number.message =====> " + message);

        SendSMSValidationCodeCommand command = SendSMSValidationCodeCommand.builder().icc("00216")
                .mobileNumber("2309341").build();

        String commandJson = objectMapper.writeValueAsString(command);

        mvc.perform(post("/validation-codes/sms/send").with(jwt().jwt(builder -> builder.claim("sub", TestConstants.DEFAULT_EMAIL)).authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_CLIENT"))))
                        .contentType(MediaType.APPLICATION_JSON).content(commandJson))
                .andExpect(status().isBadRequest())
                .andExpect(responseBody()
                        .containsApiErrors(List.of("mobileNumber: The format of phone number is incorrect.",
                                "icc: The international calling code is incorrect.")));

        then(sendValidationCodeUseCase).should(never()).sendSMSValidationCode(eq(command), any(Locale.class));

    }

    @Test
    void givenSendSMSValidationCodeThrowsUserAccountNotFoundException_WhenSendSMSValidationCode_ThenReturn400AndApiError()
            throws Exception {

        // Given
        SendSMSValidationCodeCommand command = SendSMSValidationCodeCommand.builder()
                .icc(TestConstants.DEFAULT_INTERNATIONAL_CALLING_CODE).mobileNumber(TestConstants.DEFAULT_MOBILE_NUMBER)
                .build();

        given(sendValidationCodeUseCase.sendSMSValidationCode(eq(command), any(Locale.class)))
                .willThrow(new UserAccountNotFoundException("Some error message."));

        // When, Then
        String commandJson = objectMapper.writeValueAsString(command);

        mvc.perform(post("/validation-codes/sms/send").with(jwt().jwt(builder -> builder.claim("sub", TestConstants.DEFAULT_EMAIL)).authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_CLIENT"))))
                        .contentType(MediaType.APPLICATION_JSON).content(commandJson))
                .andExpect(status().isUnauthorized())
                .andExpect(responseBody().containsApiErrors(List.of("Bad credentials.")));

    }

    @Test
    void givenSendSMSValidationCodeReturnsFalse_WhenSendSMSValidationCode_ThenReturnValidationCodeRequestStatusREJECTED()
            throws Exception {

        // Given
        SendSMSValidationCodeCommand command = SendSMSValidationCodeCommand.builder()
                .icc(TestConstants.DEFAULT_INTERNATIONAL_CALLING_CODE).mobileNumber(TestConstants.DEFAULT_MOBILE_NUMBER)
                .build();

        given(sendValidationCodeUseCase.sendSMSValidationCode(eq(command), any(Locale.class))).willReturn(false);

        // When, Then
        String commandJson = objectMapper.writeValueAsString(command);

        ValidationCodeRequest expectedResponse = new ValidationCodeRequest(ValidationCodeRequestStatus.REJECTED);
        mvc.perform(post("/validation-codes/sms/send").with(jwt().jwt(builder -> builder.claim("sub", TestConstants.DEFAULT_EMAIL)).authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_CLIENT"))))
                        .contentType(MediaType.APPLICATION_JSON).content(commandJson))
                .andExpect(status().isAccepted())
                .andExpect(responseBody().containsObjectAsJson(expectedResponse, ValidationCodeRequest.class));

    }

    // Test sendEmailValidationLink

    @Test
    void givenValidInput_WhenSendEmailValidationLink_ThenSendEmailValidationLink() throws Exception {

        // Given
        SendEmailValidationLinkCommand command = SendEmailValidationLinkCommand.builder()
                .email(TestConstants.DEFAULT_EMAIL).build();

        given(sendValidationCodeUseCase.sendEmailValidationLink(eq(command), any(Locale.class))).willReturn(true);

        // When
        String commandJson = objectMapper.writeValueAsString(command);
        ValidationCodeRequest expectedResponse = new ValidationCodeRequest(ValidationCodeRequestStatus.REGISTRED);

        mvc.perform(post("/validation-codes/email/send").with(jwt().jwt(builder -> builder.claim("sub", TestConstants.DEFAULT_EMAIL)).authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_CLIENT"))))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(commandJson))
                .andExpect(status().isAccepted())
                .andExpect(responseBody().containsObjectAsJson(expectedResponse, ValidationCodeRequest.class));

        // Then
        then(sendValidationCodeUseCase).should(times(1)).sendEmailValidationLink(eq(command), any(Locale.class));

    }

    @Test
    void givenValidInputAndBadAuthority_WhenSendEmailValidationLink_ThenReturnForbidden() throws Exception {

        // Given
        SendEmailValidationLinkCommand command = SendEmailValidationLinkCommand.builder()
                .email(TestConstants.DEFAULT_EMAIL).build();

        given(sendValidationCodeUseCase.sendEmailValidationLink(eq(command), any(Locale.class))).willReturn(true);

        // When
        String commandJson = objectMapper.writeValueAsString(command);

        mvc.perform(post("/validation-codes/email/send").with(jwt().jwt(builder -> builder.claim("sub", TestConstants.DEFAULT_EMAIL)).authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_BAD_ROLE"))))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(commandJson))
                .andExpect(status().isForbidden());

        // Then
        then(sendValidationCodeUseCase).should(never()).sendEmailValidationLink(eq(command), any(Locale.class));

    }

    @Test
    void givenSendEmailValidationLinkThrowsUserEmailValidationException_WhenSendEmailValidationLink_ThenReturn400AndApiError()
            throws Exception {

        // Given
        SendEmailValidationLinkCommand command = SendEmailValidationLinkCommand.builder()
                .email(TestConstants.DEFAULT_EMAIL).build();

        given(sendValidationCodeUseCase.sendEmailValidationLink(eq(command), any(Locale.class)))
                .willThrow(new UserEmailValidationException("Some error message."));

        // When, Then
        String commandJson = objectMapper.writeValueAsString(command);

        ApiError expectedApiError = new ApiError(HttpStatus.BAD_REQUEST, ErrorCode.EMAIL_VALIDATION,
                "Some error message.");

        mvc.perform(post("/validation-codes/email/send").with(jwt().jwt(builder -> builder.claim("sub", TestConstants.DEFAULT_EMAIL)).authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_CLIENT"))))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(commandJson))
                .andExpect(status().isBadRequest())
                .andExpect(responseBody().containsObjectAsJson(expectedApiError, ApiError.class));

    }

    @Test
    void givenInvalidInput_WhenSendEmailValidationLink_ThenReturnBadRequest() throws Exception {

        SendEmailValidationLinkCommand command = SendEmailValidationLinkCommand.builder().email("test test@test.c123")
                .build();

        String commandJson = objectMapper.writeValueAsString(command);

        mvc.perform(post("/validation-codes/email/send").with(jwt().jwt(builder -> builder.claim("sub", TestConstants.DEFAULT_EMAIL)).authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_CLIENT"))))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(commandJson))
                .andExpect(status().isBadRequest())
                .andExpect(responseBody().containsApiErrors(List.of("email: The format of email is incorrect.")));

        then(sendValidationCodeUseCase).should(never()).sendEmailValidationLink(eq(command), any(Locale.class));

    }

    @Test
    void givenSendEmailValidationLinkReturnsFalse_WhenSendEmailValidationLink_ThenReturnValidationCodeRequestStatusREJECTED()
            throws Exception {

        // Given
        SendEmailValidationLinkCommand command = SendEmailValidationLinkCommand.builder()
                .email(TestConstants.DEFAULT_EMAIL).build();

        given(sendValidationCodeUseCase.sendEmailValidationLink(eq(command), any(Locale.class))).willReturn(false);

        // When, Then
        String commandJson = objectMapper.writeValueAsString(command);

        ValidationCodeRequest expectedResponse = new ValidationCodeRequest(ValidationCodeRequestStatus.REJECTED);

        mvc.perform(post("/validation-codes/email/send").with(jwt().jwt(builder -> builder.claim("sub", TestConstants.DEFAULT_EMAIL)).authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_CLIENT"))))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(commandJson))
                .andExpect(status().isAccepted())
                .andExpect(responseBody().containsObjectAsJson(expectedResponse, ValidationCodeRequest.class));

    }

}
