package com.excentria_it.wamya.application.service;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import com.excentria_it.wamya.application.port.in.SendValidationCodeUseCase;
import com.excentria_it.wamya.application.port.out.LoadUserAccountPort;
import com.excentria_it.wamya.application.port.out.MessagingPort;
import com.excentria_it.wamya.application.port.out.UpdateUserAccountPort;
import com.excentria_it.wamya.application.service.helper.CodeGenerator;
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

@UseCase
@Transactional
public class SendValidationCodeService implements SendValidationCodeUseCase {
	@Autowired
	private CodeGenerator codeGenerator;
	@Autowired
	private MessagingPort messagingPort;
	@Autowired
	private LoadUserAccountPort loadUserAccountPort;

	@Autowired
	private UpdateUserAccountPort updateUserAccountPort;

	@Autowired
	@ViewMessageSource
	private MessageSource messageSource;

	@Override
	public boolean sendSMSValidationCode(SendSMSValidationCodeCommand command, Locale locale) {

		UserAccount userAccount = checkExistingAccount(command);

		MobilePhoneNumber mobilePhoneNumber = new MobilePhoneNumber(command.getIcc(), command.getMobileNumber());

		if (!userAccount.getIsValidatedMobileNumber()) {

			String validationCode = codeGenerator.generateNumericCode();

			updateSMSValidationCode(userAccount, validationCode);

			messagingPort.sendSMSMessage(SMSMessage.builder().template(SMSTemplate.PHONE_VALIDATION)
					.to(mobilePhoneNumber.toCallable()).language(locale.getLanguage())
					.params(Map.of(SMSTemplate.PHONE_VALIDATION.getTemplateParams().get(0), validationCode)).build());

			return true;
		} else {
			throw new UserMobileNumberValidationException(
					String.format("Mobile number %s already validated.", mobilePhoneNumber.toString()));
		}
	}

	@Override
	public boolean sendEmailValidationLink(SendEmailValidationLinkCommand command, Locale locale) {
		UserAccount userAccount = checkExistingAccount(command);

		if (!userAccount.getIsValidatedEmail()) {

			String validationCode = codeGenerator.generateNumericCode();

			updateEmailValidationCode(userAccount, validationCode);

			messagingPort.sendEmailMessage(EmailMessage.builder().from(EmailSender.WAMYA_TEAM).to(command.getEmail())
					.subject(messageSource.getMessage(EmailSubject.EMAIL_VALIDATION, null, locale))
					.template(EmailTemplate.EMAIL_VALIDATION)
					.params(Map.of(EmailTemplate.EMAIL_VALIDATION.getTemplateParams().get(0), validationCode))
					.language(locale.getLanguage()).build());
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

	protected UserAccount checkExistingAccount(SendEmailValidationLinkCommand command) {

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
