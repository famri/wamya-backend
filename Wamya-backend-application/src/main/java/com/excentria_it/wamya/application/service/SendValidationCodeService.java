package com.excentria_it.wamya.application.service;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.context.MessageSource;

import com.excentria_it.wamya.application.port.in.SendValidationCodeUseCase;
import com.excentria_it.wamya.application.port.out.LoadUserAccountPort;
import com.excentria_it.wamya.application.port.out.MessagingPort;
import com.excentria_it.wamya.application.port.out.UpdateUserAccountPort;
import com.excentria_it.wamya.common.CodeGenerator;
import com.excentria_it.wamya.common.annotation.UseCase;
import com.excentria_it.wamya.common.annotation.ViewMessageSource;
import com.excentria_it.wamya.common.domain.EmailMessage;
import com.excentria_it.wamya.common.domain.EmailTemplate;
import com.excentria_it.wamya.common.domain.SMSMessage;
import com.excentria_it.wamya.common.domain.SMSTemplate;
import com.excentria_it.wamya.common.exception.UserAccountNotFoundException;
import com.excentria_it.wamya.common.exception.UserEmailValidationException;
import com.excentria_it.wamya.common.exception.UserMobileNumberValidationException;
import com.excentria_it.wamya.domain.EmailSender;
import com.excentria_it.wamya.domain.EmailSubject;
import com.excentria_it.wamya.domain.UserAccount;
import com.excentria_it.wamya.domain.UserAccount.MobilePhoneNumber;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@UseCase
@Transactional
public class SendValidationCodeService implements SendValidationCodeUseCase {

	private final CodeGenerator codeGenerator;

	private final MessagingPort messagingPort;

	private final LoadUserAccountPort loadUserAccountPort;

	private final UpdateUserAccountPort updateUserAccountPort;

	@ViewMessageSource
	private final MessageSource messageSource;

	@Override
	public boolean sendSMSValidationCode(SendSMSValidationCodeCommand command, Locale locale) {

		UserAccount userAccount = checkExistingAccount(command);

		MobilePhoneNumber mobilePhoneNumber = new MobilePhoneNumber(command.getIcc(), command.getMobileNumber());

		if (!userAccount.getIsValidatedMobileNumber()) {

			String validationCode = codeGenerator.generateNumericCode();

			updateSMSValidationCode(userAccount, validationCode);

			messagingPort.sendSMSMessage(SMSMessage.builder().template(SMSTemplate.PHONE_VALIDATION)
					.to(mobilePhoneNumber.toCallable()).locale(locale)
					.params(Map.of(SMSTemplate.PHONE_VALIDATION.getTemplateParams().get(0), validationCode)).build());

			return true;
		} else {
			throw new UserMobileNumberValidationException(
					String.format("Mobile number %s already validated.", mobilePhoneNumber.toString()));
		}
	}

	@Override
	public boolean sendEmailValidationCode(SendEmailValidationCodeCommand command, Locale locale) {
		UserAccount userAccount = checkExistingAccount(command);

		if (!userAccount.getIsValidatedEmail()) {

			String validationCode = codeGenerator.generateNumericCode();

			updateEmailValidationCode(userAccount, validationCode);

			messagingPort.sendEmailMessage(EmailMessage.builder().from(EmailSender.WAMYA_TEAM).to(command.getEmail())
					.subject(messageSource.getMessage(EmailSubject.EMAIL_VALIDATION, null, locale))
					.template(EmailTemplate.EMAIL_VALIDATION)
					.params(Map.of(EmailTemplate.EMAIL_VALIDATION.getTemplateParams().get(0), validationCode))
					.locale(locale).build());
			return true;
		} else {
			throw new UserEmailValidationException(
					String.format("Email %s already validated.", command.getEmail().toString()));
		}
	}

	protected UserAccount checkExistingAccount(SendSMSValidationCodeCommand command) {

		Optional<UserAccount> userAccountOptional = loadUserAccountPort
				.loadUserAccountByIccAndMobileNumber(command.getIcc(), command.getMobileNumber());
		if (userAccountOptional.isEmpty()) {
			throw new UserAccountNotFoundException(
					String.format("No account having mobile phone number %s %s was found.", command.getIcc(),
							command.getMobileNumber()));
		}
		return userAccountOptional.get();
	}

	protected UserAccount checkExistingAccount(SendEmailValidationCodeCommand command) {

		Optional<UserAccount> userAccountOptional = loadUserAccountPort.loadUserAccountByEmail(command.getEmail());
		if (userAccountOptional.isEmpty()) {
			throw new UserAccountNotFoundException(
					String.format("No account having email %s was found.", command.getEmail()));
		}
		return userAccountOptional.get();
	}

	protected void updateSMSValidationCode(UserAccount userAccount, String validationCode) {

		userAccount.setMobileNumberValidationCode(validationCode);
		userAccount.setIsValidatedMobileNumber(false);
		updateUserAccountPort.updateUserAccount(userAccount);
	}

	protected void updateEmailValidationCode(UserAccount userAccount, String validationCode) {

		userAccount.setEmailValidationCode(validationCode);
		userAccount.setIsValidatedEmail(false);
		updateUserAccountPort.updateUserAccount(userAccount);
	}

}
