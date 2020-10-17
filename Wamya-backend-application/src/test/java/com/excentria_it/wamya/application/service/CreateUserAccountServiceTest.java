package com.excentria_it.wamya.application.service;

import static com.excentria_it.wamya.test.data.common.TestConstants.*;
import static com.excentria_it.wamya.test.data.common.UserAccountTestData.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import java.util.Locale;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.excentria_it.wamya.application.port.in.CreateUserAccountUseCase.CreateUserAccountCommand;
import com.excentria_it.wamya.application.port.in.CreateUserAccountUseCase.CreateUserAccountCommand.CreateUserAccountCommandBuilder;
import com.excentria_it.wamya.application.port.out.CreateUserAccountPort;
import com.excentria_it.wamya.application.port.out.LoadUserAccountPort;
import com.excentria_it.wamya.application.port.out.MessagingPort;
import com.excentria_it.wamya.application.props.ServerUrlProperties;
import com.excentria_it.wamya.application.service.helper.CodeGenerator;
import com.excentria_it.wamya.common.domain.EmailMessage;
import com.excentria_it.wamya.common.domain.EmailTemplate;
import com.excentria_it.wamya.common.domain.SMSMessage;
import com.excentria_it.wamya.common.domain.SMSTemplate;
import com.excentria_it.wamya.common.exception.UserAccountAlreadyExistsException;
import com.excentria_it.wamya.domain.UserAccount;
import com.excentria_it.wamya.domain.UserAccount.MobilePhoneNumber;

@ExtendWith(MockitoExtension.class)
public class CreateUserAccountServiceTest {

	@Mock
	private LoadUserAccountPort loadUserAccountPort;

	@Mock
	private CreateUserAccountPort createUserAccountPort;

	@Mock
	private MessagingPort messagingPort;

	@Mock
	private CodeGenerator codeGenerator;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
	private ServerUrlProperties serverUrlProperties;

	@Mock
	private MessageSource messageSource;

	@Spy
	@InjectMocks
	private CreateUserAccountService createUserAccountService;

	private Locale locale = new Locale("fr");

	@Test
	void givenExistingMobilePhoneNumber_whenRegisterUserAccountCreationDemand_thenThrowUserAccountAlreadyExistsException() {

		givenAnExistingMobilePhoneNumber();

		CreateUserAccountCommandBuilder commandBuilder = defaultCreateUserAccountCommandBuilder();

		assertThrows(UserAccountAlreadyExistsException.class,
				() -> createUserAccountService.registerUserAccountCreationDemand(commandBuilder.build(), locale));

	}

	@Test
	void givenExistingEmail_whenRegisterUserAccountCreationDemand_thenThrowUserAccountAlreadyExistsException() {

		givenAnExistingEmail();
		givenNonExistingMobilePhoneNumber();

		CreateUserAccountCommandBuilder commandBuilder = defaultCreateUserAccountCommandBuilder();

		assertThrows(UserAccountAlreadyExistsException.class,
				() -> createUserAccountService.registerUserAccountCreationDemand(commandBuilder.build(), locale));

	}

	@Test
	void givenNonExistingMobilePhoneNumberAndNonExistingEmail_whenRegisterUserAccountCreationDemand_thenSucceed() {

		givenNonExistingMobilePhoneNumber();
		givenNonExistingEmail();
		givenServerUrlProperties();

		String validationCode = givenDefaultGeneratedCode();

		String encodedPassword = givenDefaultEncodedPassword();

		CreateUserAccountCommandBuilder commandBuilder = defaultCreateUserAccountCommandBuilder();
		CreateUserAccountCommand command = commandBuilder.build();

		String emailValidationLink = givenPatchUrl(command.getEmail(), validationCode);

		assertDoesNotThrow(() -> createUserAccountService.registerUserAccountCreationDemand(command, locale));

		ArgumentCaptor<UserAccount> userAccountCaptor = ArgumentCaptor.forClass(UserAccount.class);

		then(createUserAccountPort).should(times(1)).createUserAccount(userAccountCaptor.capture());

		assertThat(userAccountCaptor.getValue().getMobilePhoneNumber().getInternationalCallingCode())
				.isEqualTo(command.getIcc());

		assertThat(userAccountCaptor.getValue().getMobilePhoneNumber().getMobileNumber())
				.isEqualTo(command.getMobileNumber());

		assertThat(userAccountCaptor.getValue().getEmail()).isEqualTo(command.getEmail());

		assertThat(userAccountCaptor.getValue().getUserPassword()).isEqualTo(encodedPassword);

		assertThat(userAccountCaptor.getValue().getEmailValidationCode()).isEqualTo(validationCode);

		assertThat(userAccountCaptor.getValue().getMobileNumberValidationCode()).isEqualTo(validationCode);

		then(codeGenerator).should(times(2)).generateNumericCode();

		// Testing mobile phone number validation SMS
		ArgumentCaptor<SMSMessage> smsMessageCaptor = ArgumentCaptor.forClass(SMSMessage.class);

		then(messagingPort).should(times(1)).sendSMSMessage(smsMessageCaptor.capture());

		assertThat(smsMessageCaptor.getValue().getTo())
				.isEqualTo(userAccountCaptor.getValue().getMobilePhoneNumber().toCallable());

		assertThat(smsMessageCaptor.getValue().getTemplate()).isEqualTo(SMSTemplate.PHONE_VALIDATION);

		assertThat(not(smsMessageCaptor.getValue().getParams().isEmpty()));

		assertNotNull(
				smsMessageCaptor.getValue().getParams().get(SMSTemplate.PHONE_VALIDATION.getTemplateParams().get(0)));

		assertThat(smsMessageCaptor.getValue().getParams().get(SMSTemplate.PHONE_VALIDATION.getTemplateParams().get(0)))
				.isEqualTo(validationCode);

		// Testing email validation
		ArgumentCaptor<EmailMessage> emailMessageCaptor = ArgumentCaptor.forClass(EmailMessage.class);

		then(messagingPort).should(times(1)).sendEmailMessage(emailMessageCaptor.capture());

		assertThat(emailMessageCaptor.getValue().getTo()).isEqualTo(userAccountCaptor.getValue().getEmail());

		assertThat(emailMessageCaptor.getValue().getTemplate()).isEqualTo(EmailTemplate.EMAIL_VALIDATION);

		assertThat(not(emailMessageCaptor.getValue().getParams().isEmpty()));

		assertNotNull(emailMessageCaptor.getValue().getParams()
				.get(EmailTemplate.EMAIL_VALIDATION.getTemplateParams().get(0)));

		assertThat(emailMessageCaptor.getValue().getParams()
				.get(EmailTemplate.EMAIL_VALIDATION.getTemplateParams().get(0))).isEqualTo(emailValidationLink);
	}

	@Test
	void givenSendingSMSValidationCodeFailed_whenRegisterUserAccountCreationDemand_thenReturnCreatedUserAccountId() {

		givenNonExistingMobilePhoneNumber();
		givenNonExistingEmail();
		givenDefaultGeneratedCode();

		CreateUserAccountCommand command = defaultCreateUserAccountCommandBuilder().build();
		givenRequestSendingEmailValidationLinkReturns(true);
		givenRequestSendingSMSValidationCodeReturns(false);
		Long userAccountId = createUserAccountService.registerUserAccountCreationDemand(command, locale);
		assertNotNull(userAccountId);

	}

	@Test
	void givenSendingEmailValidationLinkFailed_whenRegisterUserAccountCreationDemand_thenReturnCreatedUserAccountId() {

		givenNonExistingMobilePhoneNumber();
		givenNonExistingEmail();
		givenDefaultGeneratedCode();

		CreateUserAccountCommand command = defaultCreateUserAccountCommandBuilder().build();
		givenRequestSendingEmailValidationLinkReturns(false);

		Long userAccountId = createUserAccountService.registerUserAccountCreationDemand(command, locale);
		assertNotNull(userAccountId);

	}

	@Test
	void givenSendMailMessageThrowsInvalidEmailMessageException_whenRequestSendingEmailValidationLink_thenReturnFalse() {
		givenSendMailMessageThrowsIllegalArgumentException();

		CreateUserAccountCommand command = defaultCreateUserAccountCommandBuilder().build();

		boolean result = createUserAccountService.requestSendingEmailValidationLink(command.getEmail(),
				"SOME VALIDATION CODE", locale);
		assertEquals(false, result);
	}

	@Test
	void givenSendSMSMessageThrowsInvalidSMSMessageException_whenRequestSendingSMSValidationCode_thenReturnFalse() {
		givenSendSMSMessageThrowsIllegalArgumentException();

		CreateUserAccountCommand command = defaultCreateUserAccountCommandBuilder().build();

		boolean result = createUserAccountService.requestSendingSMSValidationCode(
				new MobilePhoneNumber(command.getIcc(), command.getMobileNumber()), "SOME VALIDATION CODE", locale);
		assertEquals(false, result);
	}

	private String givenDefaultEncodedPassword() {
		given(passwordEncoder.encode(any(String.class))).willReturn(DEFAULT_ENCODED_PASSWORD);
		return DEFAULT_ENCODED_PASSWORD;
	}

	private String givenDefaultGeneratedCode() {
		given(codeGenerator.generateNumericCode()).willReturn(DEFAULT_VALIDATION_CODE);
		return DEFAULT_VALIDATION_CODE;
	}

	private void givenNonExistingMobilePhoneNumber() {

		given(loadUserAccountPort.loadUserAccountByIccAndMobileNumber(any(String.class), any(String.class)))
				.willReturn(Optional.ofNullable(null));

	}

	private void givenAnExistingMobilePhoneNumber() {

		given(loadUserAccountPort.loadUserAccountByIccAndMobileNumber(any(String.class), any(String.class)))
				.willReturn(Optional.of(notYetValidatedMobileNumberUserAccount()));

	}

	private void givenNonExistingEmail() {

		given(loadUserAccountPort.loadUserAccountByEmail(any(String.class))).willReturn(Optional.ofNullable(null));

	}

	private void givenAnExistingEmail() {

		given(loadUserAccountPort.loadUserAccountByEmail(any(String.class)))
				.willReturn(Optional.of(notYetValidatedEmailUserAccount()));

	}

	private void givenServerUrlProperties() {
		given(serverUrlProperties.getHost()).willReturn("HOST");
		given(serverUrlProperties.getPort()).willReturn("PORT");
		given(serverUrlProperties.getProtocol()).willReturn("PROTOCOL");

	}

	private String givenPatchUrl(String email, String validationCode) {
		String result = "SOME_EMAIL_VALIDATION_LINK";

		doReturn(result).when(createUserAccountService).patchURL(any(String.class), any(String.class),
				any(String.class), any(String.class), any(String.class), any(String.class));
		return result;
	}

	private void givenRequestSendingSMSValidationCodeReturns(boolean result) {
		doReturn(result).when(createUserAccountService).requestSendingSMSValidationCode(any(MobilePhoneNumber.class),
				any(String.class), any(Locale.class));
	}

	private void givenRequestSendingEmailValidationLinkReturns(boolean result) {
		doReturn(result).when(createUserAccountService).requestSendingEmailValidationLink(any(String.class),
				any(String.class), any(Locale.class));
	}

	private void givenSendMailMessageThrowsIllegalArgumentException() {
		doThrow(IllegalArgumentException.class).when(messagingPort).sendEmailMessage(any(EmailMessage.class));

	}

	private void givenSendSMSMessageThrowsIllegalArgumentException() {
		doThrow(IllegalArgumentException.class).when(messagingPort).sendSMSMessage(any(SMSMessage.class));

	}
}
