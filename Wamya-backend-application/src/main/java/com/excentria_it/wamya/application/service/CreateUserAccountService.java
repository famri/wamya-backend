package com.excentria_it.wamya.application.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.apache.commons.text.StrSubstitutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.excentria_it.wamya.application.port.in.CreateUserAccountUseCase;
import com.excentria_it.wamya.application.port.out.CreateUserAccountPort;
import com.excentria_it.wamya.application.port.out.LoadUserAccountPort;
import com.excentria_it.wamya.application.port.out.MessagingPort;
import com.excentria_it.wamya.application.props.ServerUrlProperties;
import com.excentria_it.wamya.application.service.helper.CodeGenerator;
import com.excentria_it.wamya.common.annotation.UseCase;
import com.excentria_it.wamya.common.annotation.ViewMessageSource;
import com.excentria_it.wamya.common.domain.EmailMessage;
import com.excentria_it.wamya.common.domain.EmailTemplate;
import com.excentria_it.wamya.common.domain.SMSMessage;
import com.excentria_it.wamya.common.domain.SMSTemplate;
import com.excentria_it.wamya.common.exception.UserAccountAlreadyExistsException;
import com.excentria_it.wamya.domain.EmailSender;
import com.excentria_it.wamya.domain.EmailSubject;
import com.excentria_it.wamya.domain.UserAccount;
import com.excentria_it.wamya.domain.UserAccount.MobilePhoneNumber;

import lombok.extern.slf4j.Slf4j;

@UseCase
@Transactional
@Slf4j
public class CreateUserAccountService implements CreateUserAccountUseCase {

	@Autowired
	private LoadUserAccountPort loadUserAccountPort;

	@Autowired
	private CreateUserAccountPort createUserAccountPort;

	@Autowired
	private MessagingPort messagingPort;

	@Autowired
	private CodeGenerator codeGenerator;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private ServerUrlProperties serverUrlProperties;

	@Autowired
	@ViewMessageSource
	private MessageSource messageSource;

	public static final String EMAIL_VALIDATION_URL_TEMPLATE = "${protocol}://${host}:${port}/accounts/validate?email=${email}&code=${code}";

	@Override
	public Long registerUserAccountCreationDemand(CreateUserAccountCommand command, Locale locale) {

		checkExistingAccount(command);

		String mobileNumberValidationCode = codeGenerator.generateNumericCode();
		String emailValidationCode = codeGenerator.generateNumericCode();

		UserAccount userAccount = UserAccount.builder().isTransporter(command.getIsTransporter())
				.gender(command.getGender()).firstName(command.getFirstName()).lastName(command.getLastName())
				.dateOfBirth(command.getDateOfBirth()).email(command.getEmail())
				.emailValidationCode(emailValidationCode).isValidatedEmail(false)
				.mobilePhoneNumber(new MobilePhoneNumber(command.getIcc(), command.getMobileNumber()))
				.isValidatedMobileNumber(false).mobileNumberValidationCode(mobileNumberValidationCode)
				.isValidatedMobileNumber(false).userPassword(passwordEncoder.encode(command.getUserPassword()))
				.creationTimestamp(LocalDateTime.now()).receiveNewsletter(command.getReceiveNewsletter()).build();

		Long userAccountId = createUserAccountPort.createUserAccount(userAccount);

		requestSendingEmailValidationLink(command.getEmail(), emailValidationCode, locale);

		requestSendingSMSValidationCode(userAccount.getMobilePhoneNumber(), mobileNumberValidationCode, locale);

		return userAccountId;
	}

	public void checkExistingAccount(CreateUserAccountCommand command) {

		MobilePhoneNumber mobilePhoneNumber = new MobilePhoneNumber(command.getIcc(), command.getMobileNumber());
		try {

			loadUserAccountPort.loadUserAccountByIccAndMobileNumber(command.getIcc(), command.getMobileNumber()).get();
			throw new UserAccountAlreadyExistsException(String
					.format("Mobile number already registred for another account %s", mobilePhoneNumber.toString()));
		} catch (NoSuchElementException e) {
			// It's OK Account does not exist
		}
		try {

			loadUserAccountPort.loadUserAccountByEmail(command.getEmail()).get();
			throw new UserAccountAlreadyExistsException(
					String.format("Email already registred for another account %s", command.getEmail()));
		} catch (NoSuchElementException e) {
			// It's OK Account does not exist
		}
	}

	protected String patchURL(String url, String protocol, String host, String port, String email,
			String validationCode) {

		Map<String, String> data = new HashMap<>();
		data.put("protocol", protocol);
		data.put("host", host);
		data.put("port", port);
		data.put("email", email);
		data.put("code", validationCode);
		String validation_url = StrSubstitutor.replace(url, data);

		return validation_url;
	}

	protected boolean requestSendingEmailValidationLink(String email, String validationCode, Locale locale) {

		String emailValidationLink = patchURL(EMAIL_VALIDATION_URL_TEMPLATE, serverUrlProperties.getProtocol(),
				serverUrlProperties.getHost(), serverUrlProperties.getPort(), email, validationCode);
		Map<String, String> emailTemplateParams = Map.of(EmailTemplate.EMAIL_VALIDATION.getTemplateParams().get(0),
				emailValidationLink);

		try {

			EmailMessage emailMessage = EmailMessage.builder().from(EmailSender.WAMYA_TEAM).to(email)
					.subject(messageSource.getMessage(EmailSubject.EMAIL_VALIDATION, null, locale))
					.template(EmailTemplate.EMAIL_VALIDATION).params(emailTemplateParams).language(locale.getLanguage())
					.build();

			messagingPort.sendEmailMessage(emailMessage);

		} catch (IllegalArgumentException | NoSuchMessageException e) {
			log.error("Exception sending EmailMessage:", e);
			return false;
		}

		return true;
	}

	protected boolean requestSendingSMSValidationCode(MobilePhoneNumber mobileNumber, String validationCode,
			Locale locale) {

		Map<String, String> smsTemplateParams = Map.of(SMSTemplate.PHONE_VALIDATION.getTemplateParams().get(0),
				validationCode);

		SMSMessage smsMessage = SMSMessage.builder().to(mobileNumber.toCallable())
				.template(SMSTemplate.PHONE_VALIDATION).params(smsTemplateParams).language(locale.getLanguage())
				.build();
		try {
			messagingPort.sendSMSMessage(smsMessage);
		} catch (IllegalArgumentException e) {
			log.error("Exception sending SMSMessage:", e);
			return false;
		}

		return true;
	}

}
