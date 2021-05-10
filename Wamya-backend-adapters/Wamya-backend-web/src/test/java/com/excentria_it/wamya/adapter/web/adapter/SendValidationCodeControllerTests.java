package com.excentria_it.wamya.adapter.web.adapter;

import static com.c4_soft.springaddons.security.oauth2.test.mockmvc.MockAuthenticationRequestPostProcessor.*;
import static com.excentria_it.wamya.adapter.web.helper.ResponseBodyMatchers.*;
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
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.test.context.ActiveProfiles;

import com.c4_soft.springaddons.security.oauth2.test.mockmvc.MockMvcSupport;
import com.excentria_it.wamya.adapter.web.domain.ValidationCodeRequest;
import com.excentria_it.wamya.adapter.web.domain.ValidationCodeRequestStatus;
import com.excentria_it.wamya.application.port.in.SendValidationCodeUseCase;
import com.excentria_it.wamya.application.port.in.SendValidationCodeUseCase.SendEmailValidationLinkCommand;
import com.excentria_it.wamya.application.port.in.SendValidationCodeUseCase.SendSMSValidationCodeCommand;
import com.excentria_it.wamya.common.annotation.ValidationMessageSource;
import com.excentria_it.wamya.common.exception.ApiError;
import com.excentria_it.wamya.common.exception.ApiError.ErrorCode;
import com.excentria_it.wamya.common.exception.handlers.RestApiExceptionHandler;
import com.excentria_it.wamya.common.exception.UserAccountNotFoundException;
import com.excentria_it.wamya.common.exception.UserEmailValidationException;
import com.excentria_it.wamya.common.exception.UserMobileNumberValidationException;
import com.excentria_it.wamya.test.data.common.TestConstants;
import com.fasterxml.jackson.databind.ObjectMapper;

@ActiveProfiles(profiles = { "web-local" })
@Import(value = { SendValidationCodeController.class, RestApiExceptionHandler.class, MockMvcSupport.class })
@WebMvcTest(controllers = SendValidationCodeController.class)
public class SendValidationCodeControllerTests {

	@Autowired
	private MockMvcSupport api;

	@MockBean
	private SendValidationCodeUseCase sendValidationCodeUseCase;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	@ValidationMessageSource
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

		api.with(mockAuthentication(JwtAuthenticationToken.class).name(TestConstants.DEFAULT_EMAIL)
				.authorities("SCOPE_profile:write"))
				.perform(
						post("/validation-codes/sms/send").contentType(MediaType.APPLICATION_JSON).content(commandJson))
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

		api.with(mockAuthentication(JwtAuthenticationToken.class).name(TestConstants.DEFAULT_EMAIL)
				.authorities("SCOPE_profile:read"))
				.perform(
						post("/validation-codes/sms/send").contentType(MediaType.APPLICATION_JSON).content(commandJson))
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

		api.with(mockAuthentication(JwtAuthenticationToken.class).name(TestConstants.DEFAULT_EMAIL)
				.authorities("SCOPE_profile:write"))
				.perform(
						post("/validation-codes/sms/send").contentType(MediaType.APPLICATION_JSON).content(commandJson))
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

		api.with(mockAuthentication(JwtAuthenticationToken.class).name(TestConstants.DEFAULT_EMAIL)
				.authorities("SCOPE_profile:write"))
				.perform(
						post("/validation-codes/sms/send").contentType(MediaType.APPLICATION_JSON).content(commandJson))
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

		api.with(mockAuthentication(JwtAuthenticationToken.class).name(TestConstants.DEFAULT_EMAIL)
				.authorities("SCOPE_profile:write"))
				.perform(
						post("/validation-codes/sms/send").contentType(MediaType.APPLICATION_JSON).content(commandJson))
				.andExpect(status().isBadRequest())
				.andExpect(responseBody().containsApiErrors(List.of("Some error message.")));

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
		api.with(mockAuthentication(JwtAuthenticationToken.class).name(TestConstants.DEFAULT_EMAIL)
				.authorities("SCOPE_profile:write"))
				.perform(
						post("/validation-codes/sms/send").contentType(MediaType.APPLICATION_JSON).content(commandJson))
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

		api.with(mockAuthentication(JwtAuthenticationToken.class).name(TestConstants.DEFAULT_EMAIL)
				.authorities("SCOPE_profile:write"))
				.perform(post("/validation-codes/email/send").contentType(MediaType.APPLICATION_JSON)
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

		api.with(mockAuthentication(JwtAuthenticationToken.class).name(TestConstants.DEFAULT_EMAIL)
				.authorities("SCOPE_profile:read"))
				.perform(post("/validation-codes/email/send").contentType(MediaType.APPLICATION_JSON)
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

		api.with(mockAuthentication(JwtAuthenticationToken.class).name(TestConstants.DEFAULT_EMAIL)
				.authorities("SCOPE_profile:write"))
				.perform(post("/validation-codes/email/send").contentType(MediaType.APPLICATION_JSON)
						.content(commandJson))
				.andExpect(status().isBadRequest())
				.andExpect(responseBody().containsObjectAsJson(expectedApiError, ApiError.class));

	}

	@Test
	void givenInvalidInput_WhenSendEmailValidationLink_ThenReturnBadRequest() throws Exception {

		SendEmailValidationLinkCommand command = SendEmailValidationLinkCommand.builder().email("test test@test.c123")
				.build();

		String commandJson = objectMapper.writeValueAsString(command);

		api.with(mockAuthentication(JwtAuthenticationToken.class).name(TestConstants.DEFAULT_EMAIL)
				.authorities("SCOPE_profile:write"))
				.perform(post("/validation-codes/email/send").contentType(MediaType.APPLICATION_JSON)
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

		api.with(mockAuthentication(JwtAuthenticationToken.class).name(TestConstants.DEFAULT_EMAIL)
				.authorities("SCOPE_profile:write"))
				.perform(post("/validation-codes/email/send").contentType(MediaType.APPLICATION_JSON)
						.content(commandJson))
				.andExpect(status().isAccepted())
				.andExpect(responseBody().containsObjectAsJson(expectedResponse, ValidationCodeRequest.class));

	}

}
