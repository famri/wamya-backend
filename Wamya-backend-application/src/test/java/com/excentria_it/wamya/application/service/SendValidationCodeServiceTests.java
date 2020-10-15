package com.excentria_it.wamya.application.service;

import static com.excentria_it.wamya.test.data.common.TestConstants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import java.util.Locale;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import com.excentria_it.wamya.application.port.in.SendValidationCodeUseCase.SendEmailValidationLinkCommand;
import com.excentria_it.wamya.application.port.in.SendValidationCodeUseCase.SendSMSValidationCodeCommand;
import com.excentria_it.wamya.application.port.out.LoadUserAccountPort;
import com.excentria_it.wamya.application.port.out.MessagingPort;
import com.excentria_it.wamya.application.port.out.UpdateUserAccountPort;
import com.excentria_it.wamya.common.CodeGenerator;
import com.excentria_it.wamya.common.domain.EmailMessage;
import com.excentria_it.wamya.common.domain.SMSMessage;
import com.excentria_it.wamya.common.exception.UserAccountNotFoundException;
import com.excentria_it.wamya.common.exception.UserEmailValidationException;
import com.excentria_it.wamya.common.exception.UserMobileNumberValidationException;
import com.excentria_it.wamya.domain.UserAccount;
import com.excentria_it.wamya.test.data.common.TestConstants;

@ExtendWith(MockitoExtension.class)
public class SendValidationCodeServiceTests {

	@Mock
	private CodeGenerator codeGenerator;
	@Mock
	private MessagingPort messagingPort;
	@Mock
	private LoadUserAccountPort loadUserAccountPort;
	@Mock
	private UpdateUserAccountPort updateUserAccountPort;
	@Mock
	private MessageSource messageSource;

	@Spy
	@InjectMocks
	private SendValidationCodeService sendValidationCodeService;

	@Test
	void testUpdateSMSValidationCode() {

		String validationCode = TestConstants.DEFAULT_VALIDATION_CODE;
		UserAccount userAccount = Mockito.mock(UserAccount.class);

		sendValidationCodeService.updateSMSValidationCode(userAccount, validationCode);

		InOrder inOrder = Mockito.inOrder(userAccount, updateUserAccountPort);

		then(userAccount).should(inOrder).setMobileNumberValidationCode(validationCode);

		then(userAccount).should(inOrder).setIsValidatedMobileNumber(false);

		then(updateUserAccountPort).should(inOrder).updateUserAccount(userAccount);
	}

	@Test
	void testUpdateEmailValidationCode() {

		String validationCode = TestConstants.DEFAULT_VALIDATION_CODE;
		UserAccount userAccount = Mockito.mock(UserAccount.class);

		sendValidationCodeService.updateEmailValidationCode(userAccount, validationCode);

		InOrder inOrder = Mockito.inOrder(userAccount, updateUserAccountPort);

		then(userAccount).should(inOrder).setEmailValidationCode(validationCode);

		then(userAccount).should(inOrder).setIsValidatedEmail(false);

		then(updateUserAccountPort).should(inOrder).updateUserAccount(userAccount);
	}

	@Test
	void givenNonExistentEmail_whenCheckExistingAccount_ThenThrowUserAccountNotFoundException() {
		givenNonExistentEmail();

		SendEmailValidationLinkCommand command = new SendEmailValidationLinkCommand(TestConstants.DEFAULT_EMAIL);

		assertThrows(UserAccountNotFoundException.class, () -> sendValidationCodeService.checkExistingAccount(command));
	}

	@Test
	void givenNonExistentMobilePhoneNumber_whenCheckExistingAccount_ThenThrowUserAccountNotFoundException() {
		givenNonExistentMobilePhoneNumber();

		SendSMSValidationCodeCommand sendSMSValidationCodeCommand = new SendSMSValidationCodeCommand(
				TestConstants.DEFAULT_INTERNATIONAL_CALLING_CODE, TestConstants.DEFAULT_MOBILE_NUMBER);

		assertThrows(UserAccountNotFoundException.class,
				() -> sendValidationCodeService.checkExistingAccount(sendSMSValidationCodeCommand));
	}

	@Test
	void givenExistentEmail_whenCheckExistingAccount_ThenReturnUserAccount() {

		UserAccount account = Mockito.mock(UserAccount.class);
		givenExistentEmail(account);
		SendEmailValidationLinkCommand command = new SendEmailValidationLinkCommand(TestConstants.DEFAULT_EMAIL);

		UserAccount result = sendValidationCodeService.checkExistingAccount(command);

		assertEquals(account, result);
	}

	@Test
	void givenExistentMobilePhoneNumber_whenCheckExistingAccount_ThenReturnUserAccount() {

		UserAccount account = Mockito.mock(UserAccount.class);
		givenExistentMobilePhoneNumber(account);
		SendSMSValidationCodeCommand sendSMSValidationCodeCommand = new SendSMSValidationCodeCommand(
				TestConstants.DEFAULT_INTERNATIONAL_CALLING_CODE, TestConstants.DEFAULT_MOBILE_NUMBER);

		UserAccount result = sendValidationCodeService.checkExistingAccount(sendSMSValidationCodeCommand);

		assertEquals(account, result);
	}

	@Test
	void givenNonExistentEmailAccount_WhenSendEmailValidationCode_ThenThrowUserAccountNotFoundException() {
		givenNonExistentEmail();
		SendEmailValidationLinkCommand command = new SendEmailValidationLinkCommand(TestConstants.DEFAULT_EMAIL);

		assertThrows(UserAccountNotFoundException.class,
				() -> sendValidationCodeService.sendEmailValidationLink(command, new Locale("fr")));
	}

	@Test
	void givenNonExistentMobilePhoneNumberAccount_WhenSendSMSValidationCode_ThenThrowUserAccountNotFoundException() {
		givenNonExistentMobilePhoneNumber();
		SendSMSValidationCodeCommand sendSMSValidationCodeCommand = new SendSMSValidationCodeCommand(
				TestConstants.DEFAULT_INTERNATIONAL_CALLING_CODE, TestConstants.DEFAULT_MOBILE_NUMBER);

		assertThrows(UserAccountNotFoundException.class,
				() -> sendValidationCodeService.sendSMSValidationCode(sendSMSValidationCodeCommand, new Locale("fr")));
	}

	@Test
	void givenAlreadyValidatedEmail_WhenSendEmailValidationCode_ThenThrowUserEmailValidationException() {

		UserAccount userAccount = Mockito.mock(UserAccount.class);
		given(userAccount.getIsValidatedEmail()).willReturn(true);

		SendEmailValidationLinkCommand command = new SendEmailValidationLinkCommand(TestConstants.DEFAULT_EMAIL);

		// Use doReturn() instead of given() because Spy not Mock
		doReturn(userAccount).when(sendValidationCodeService).checkExistingAccount(command);

		assertThrows(UserEmailValidationException.class,
				() -> sendValidationCodeService.sendEmailValidationLink(command, new Locale("fr")));
	}

	@Test
	void givenAlreadyValidatedMobileNumber_WhenSendSMSValidationCode_ThenThrowUserMobileNumberValidationException() {

		UserAccount userAccount = Mockito.mock(UserAccount.class);
		given(userAccount.getIsValidatedMobileNumber()).willReturn(true);

		SendSMSValidationCodeCommand sendSMSValidationCodeCommand = new SendSMSValidationCodeCommand(
				TestConstants.DEFAULT_INTERNATIONAL_CALLING_CODE, TestConstants.DEFAULT_MOBILE_NUMBER);

		// Use doReturn() instead of given() because Spy not Mock
		doReturn(userAccount).when(sendValidationCodeService).checkExistingAccount(sendSMSValidationCodeCommand);

		assertThrows(UserMobileNumberValidationException.class,
				() -> sendValidationCodeService.sendSMSValidationCode(sendSMSValidationCodeCommand, new Locale("fr")));
	}

	@Test
	void testSendSMSValidationCode() {
		UserAccount userAccount = Mockito.mock(UserAccount.class);

		given(userAccount.getIsValidatedMobileNumber()).willReturn(false);
		givenDefaultGeneratedCode();

		// given(SMSMessage.builder()).willReturn(smsMessageBuilder);

		SendSMSValidationCodeCommand sendSMSValidationCodeCommand = new SendSMSValidationCodeCommand(
				TestConstants.DEFAULT_INTERNATIONAL_CALLING_CODE, TestConstants.DEFAULT_MOBILE_NUMBER);

		// Use doReturn() instead of given() because Spy not Mock
		doReturn(userAccount).when(sendValidationCodeService).checkExistingAccount(sendSMSValidationCodeCommand);

		sendValidationCodeService.sendSMSValidationCode(sendSMSValidationCodeCommand, new Locale("fr"));

		InOrder inOrder = Mockito.inOrder(codeGenerator, sendValidationCodeService, messagingPort);

		inOrder.verify(codeGenerator).generateNumericCode();
		inOrder.verify(sendValidationCodeService).updateSMSValidationCode(userAccount,
				TestConstants.DEFAULT_VALIDATION_CODE);
		inOrder.verify(messagingPort).sendSMSMessage(any(SMSMessage.class));
	}

	@Test
	void testSendEmailValidationCode() {
		UserAccount userAccount = Mockito.mock(UserAccount.class);

		given(userAccount.getIsValidatedEmail()).willReturn(false);
		givenDefaultGeneratedCode();

		SendEmailValidationLinkCommand command = new SendEmailValidationLinkCommand(TestConstants.DEFAULT_EMAIL);

		// Use doReturn() instead of given() because Spy not Mock
		doReturn(userAccount).when(sendValidationCodeService).checkExistingAccount(command);

		sendValidationCodeService.sendEmailValidationLink(command, new Locale("fr"));

		InOrder inOrder = Mockito.inOrder(codeGenerator, sendValidationCodeService, messagingPort);

		inOrder.verify(codeGenerator).generateNumericCode();
		inOrder.verify(sendValidationCodeService).updateEmailValidationCode(userAccount,
				TestConstants.DEFAULT_VALIDATION_CODE);
		inOrder.verify(messagingPort).sendEmailMessage(any(EmailMessage.class));
	}

	private String givenDefaultGeneratedCode() {
		given(codeGenerator.generateNumericCode()).willReturn(DEFAULT_VALIDATION_CODE);
		return DEFAULT_VALIDATION_CODE;
	}

	private void givenNonExistentEmail() {

		given(loadUserAccountPort.loadUserAccountByEmail(any(String.class))).willReturn(Optional.ofNullable(null));

	}

	private void givenExistentEmail(UserAccount account) {

		given(loadUserAccountPort.loadUserAccountByEmail(any(String.class))).willReturn(Optional.ofNullable(account));

	}

	private void givenExistentMobilePhoneNumber(UserAccount account) {

		given(loadUserAccountPort.loadUserAccountByIccAndMobileNumber(any(String.class), any(String.class)))
				.willReturn(Optional.ofNullable(account));

	}

	private void givenNonExistentMobilePhoneNumber() {

		given(loadUserAccountPort.loadUserAccountByIccAndMobileNumber(any(String.class), any(String.class)))
				.willReturn(Optional.ofNullable(null));

	}

}
