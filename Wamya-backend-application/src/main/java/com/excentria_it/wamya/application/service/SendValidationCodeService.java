package com.excentria_it.wamya.application.service;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.apache.commons.text.StrSubstitutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

import com.excentria_it.wamya.application.port.in.SendValidationCodeUseCase;
import com.excentria_it.wamya.application.port.out.LoadUserAccountPort;
import com.excentria_it.wamya.application.port.out.MessagingPort;
import com.excentria_it.wamya.application.port.out.UpdateUserAccountPort;
import com.excentria_it.wamya.application.props.ServerUrlProperties;
import com.excentria_it.wamya.application.service.helper.CodeGenerator;
import com.excentria_it.wamya.common.annotation.UseCase;
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

import lombok.extern.slf4j.Slf4j;

@UseCase
@Transactional
@Slf4j
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
	private MessageSource messageSource;

	@Autowired
	private ServerUrlProperties serverUrlProperties;

	private static final String EMAIL_VALIDATION_URL_TEMPLATE = "${protocol}://${host}:${port}/wamya-backend/validation-codes/email/validate?uuid=${uuid}&lang=${lang}";

	@Override
	public boolean sendSMSValidationCode(SendSMSValidationCodeCommand command, Locale locale) {

		UserAccount userAccount = checkExistingAccount(command);

		MobilePhoneNumber mobilePhoneNumber = new MobilePhoneNumber(command.getIcc(), command.getMobileNumber());

		if (!userAccount.getIsValidatedMobileNumber()) {

			String validationCode = codeGenerator.generateNumericCode();

			updateUserAccountPort.updateSMSValidationCode(userAccount.getId(), validationCode);

			messagingPort.sendSMSMessage(SMSMessage.builder().template(SMSTemplate.PHONE_VALIDATION)
					.to(mobilePhoneNumber.toCallable()).language(locale.toString())
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

			String validationCode = codeGenerator.generateUUID();

			updateUserAccountPort.updateEmailValidationCode(userAccount.getId(), validationCode);

			return this.requestSendingEmailValidationLink(command.getEmail(), validationCode, locale);

		} else {
			throw new UserEmailValidationException(
					String.format("Email %s already validated.", command.getEmail().toString()));
		}
	}

	protected UserAccount checkExistingAccount(SendSMSValidationCodeCommand command) {

		Optional<UserAccount> userAccountOptional = loadUserAccountPort
				.loadUserAccountByUsername(command.getIcc() + "_" + command.getMobileNumber());
		if (userAccountOptional.isEmpty()) {
			throw new UserAccountNotFoundException(
					String.format("No account having mobile phone number %s %s was found.", command.getIcc(),
							command.getMobileNumber()));
		}
		return userAccountOptional.get();
	}

	protected UserAccount checkExistingAccount(SendEmailValidationLinkCommand command) {

		Optional<UserAccount> userAccountOptional = loadUserAccountPort.loadUserAccountByUsername(command.getEmail());
		if (userAccountOptional.isEmpty()) {
			throw new UserAccountNotFoundException(
					String.format("No account having email %s was found.", command.getEmail()));
		}
		return userAccountOptional.get();
	}

	private boolean requestSendingEmailValidationLink(String userEmail, String requestUUID, Locale locale) {

		Map<String, String> data = new HashMap<>();
		data.put("protocol", serverUrlProperties.getProtocol());
		data.put("host", serverUrlProperties.getHost());
		data.put("port", serverUrlProperties.getPort());
		data.put("uuid", requestUUID);

		data.put("lang", locale.toString());

		String emailValidationLink = patchURL(EMAIL_VALIDATION_URL_TEMPLATE, data);

		Map<String, String> emailTemplateParams = Map.of(EmailTemplate.EMAIL_VALIDATION.getTemplateParams().get(0),
				emailValidationLink);

		try {

			EmailMessage emailMessage = EmailMessage.builder().from(EmailSender.WAMYA_TEAM).to(userEmail)
					.subject(messageSource.getMessage(EmailSubject.EMAIL_VALIDATION, null, locale))
					.template(EmailTemplate.EMAIL_VALIDATION).params(emailTemplateParams).language(locale.toString())
					.build();

			messagingPort.sendEmailMessage(emailMessage);

		} catch (IllegalArgumentException | NoSuchMessageException e) {
			log.error("Exception sending EmailMessage:", e);
			return false;
		}

		return true;
	}

	private String patchURL(String url, Map<String, String> data) {

		String patchedUrl = StrSubstitutor.replace(url, data);

		return patchedUrl;
	}
}
