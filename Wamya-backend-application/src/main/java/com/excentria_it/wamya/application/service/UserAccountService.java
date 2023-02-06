package com.excentria_it.wamya.application.service;

import com.excentria_it.wamya.application.port.in.CreateUserAccountUseCase;
import com.excentria_it.wamya.application.port.out.AsynchronousMessagingPort;
import com.excentria_it.wamya.application.port.out.CreateUserAccountPort;
import com.excentria_it.wamya.application.port.out.LoadUserAccountPort;
import com.excentria_it.wamya.application.port.out.OAuthUserAccountPort;
import com.excentria_it.wamya.application.props.ServerUrlProperties;
import com.excentria_it.wamya.application.service.helper.CodeGenerator;
import com.excentria_it.wamya.common.annotation.UseCase;
import com.excentria_it.wamya.common.domain.EmailMessage;
import com.excentria_it.wamya.common.domain.EmailTemplate;
import com.excentria_it.wamya.common.domain.SMSMessage;
import com.excentria_it.wamya.common.domain.SMSTemplate;
import com.excentria_it.wamya.common.exception.UserAccountAlreadyExistsException;
import com.excentria_it.wamya.domain.*;
import com.excentria_it.wamya.domain.UserAccount.MobilePhoneNumber;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StrSubstitutor;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.transaction.Transactional;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.*;

@UseCase
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserAccountService implements CreateUserAccountUseCase {

    private final LoadUserAccountPort loadUserAccountPort;

    private final CreateUserAccountPort createUserAccountPort;

    private final OAuthUserAccountPort oAuthUserAccountPort;

    private final AsynchronousMessagingPort messagingPort;

    private final CodeGenerator codeGenerator;

    private final PasswordEncoder passwordEncoder;

    private final ServerUrlProperties serverUrlProperties;

    private final MessageSource messageSource;

    public static final String EMAIL_VALIDATION_URL_TEMPLATE = "${protocol}://${host}:${port}/accounts/validate?email=${email}&code=${code}";

    @Override
    public OpenIdAuthResponse registerUserAccountCreationDemand(CreateUserAccountCommand command, Locale locale) {

        checkExistingAccount(command);

        final String mobileNumberValidationCode = codeGenerator.generateNumericCode();
        final String emailValidationCode = codeGenerator.generateUUID();

        final OAuthUserAccount oauthUserAccount = OAuthUserAccount.builder().firstName(command.getFirstname())
                .lastName(command.getLastname()).email(command.getEmail())
                .username(command.getEmail()).enabled(true)
                .emailVerified(false)
                .credentials(Arrays.asList(OAuthUserAccount.Credentials.builder().type("password").value(command.getUserPassword())
                        .temporary(false).build()))
                .attributes(Map.of(OAuthUserAccountAttribute.PHONE_NUMBER, command.getIcc() + "_" + command.getMobileNumber()))

                .realmRoles(List.of(command.getIsTransporter() ? UserRole.ROLE_TRANSPORTER : UserRole.ROLE_CLIENT)).build();

        final String oauthId = oAuthUserAccountPort.createOAuthUserAccount(oauthUserAccount);

        final UserAccount userAccount = UserAccount.builder().oauthId(oauthId).isTransporter(command.getIsTransporter())
                .genderId(command.getGenderId()).firstname(command.getFirstname()).lastname(command.getLastname())
                .dateOfBirth(command.getDateOfBirth()).email(command.getEmail())
                .emailValidationCode(emailValidationCode).isValidatedEmail(false)
                .mobilePhoneNumber(new MobilePhoneNumber(command.getIcc(), command.getMobileNumber()))
                .mobileNumberValidationCode(mobileNumberValidationCode)
                .isValidatedMobileNumber(false)
                .creationDateTime(ZonedDateTime.now(ZoneOffset.UTC))
                .receiveNewsletter(command.getReceiveNewsletter()).globalRating(5.0).build();

        createUserAccountPort.createUserAccount(userAccount);

        requestSendingEmailValidationLink(command.getEmail(), emailValidationCode, locale);

        requestSendingSMSValidationCode(userAccount.getMobilePhoneNumber(), mobileNumberValidationCode, locale);

        OpenIdAuthResponse accessToken = oAuthUserAccountPort.fetchJwtTokenForUser(command.getEmail(),
                command.getUserPassword());

        return accessToken;
    }

    public void checkExistingAccount(CreateUserAccountCommand command) {

        Optional<UserAccount> userAccountOptional = loadUserAccountPort.loadUserAccountBySubject(command.getEmail());
        if (userAccountOptional.isPresent()) {
            throw new UserAccountAlreadyExistsException(
                    String.format("Email already registred for another account %s", command.getEmail()));
        }

        userAccountOptional = loadUserAccountPort
                .loadUserAccountBySubject(command.getIcc() + "_" + command.getMobileNumber());
        if (userAccountOptional.isPresent()) {
            throw new UserAccountAlreadyExistsException(
                    String.format("Mobile number already registred for another account %s %s", command.getIcc(),
                            command.getMobileNumber()));
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
        String validationUrl = StrSubstitutor.replace(url, data);

        return validationUrl;
    }

    protected boolean requestSendingEmailValidationLink(String email, String validationCode, Locale locale) {

        String emailValidationLink = patchURL(EMAIL_VALIDATION_URL_TEMPLATE, serverUrlProperties.getProtocol(),
                serverUrlProperties.getHost(), serverUrlProperties.getPort(), email, validationCode);
        Map<String, String> emailTemplateParams = Map.of(EmailTemplate.EMAIL_VALIDATION.getTemplateParams().get(0),
                emailValidationLink);

        try {

            EmailMessage emailMessage = EmailMessage.builder().from(EmailSender.FRETTO_TEAM).to(email)
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
