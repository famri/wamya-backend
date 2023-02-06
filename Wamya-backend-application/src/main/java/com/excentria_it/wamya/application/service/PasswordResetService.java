package com.excentria_it.wamya.application.service;

import com.excentria_it.wamya.application.port.in.RequestPasswordResetUseCase;
import com.excentria_it.wamya.application.port.in.ResetPasswordUseCase;
import com.excentria_it.wamya.application.port.out.AsynchronousMessagingPort;
import com.excentria_it.wamya.application.port.out.LoadUserAccountPort;
import com.excentria_it.wamya.application.port.out.OAuthUserAccountPort;
import com.excentria_it.wamya.application.port.out.PasswordResetRequestPort;
import com.excentria_it.wamya.application.props.PasswordResetProperties;
import com.excentria_it.wamya.application.props.ServerUrlProperties;
import com.excentria_it.wamya.common.annotation.UseCase;
import com.excentria_it.wamya.common.domain.EmailMessage;
import com.excentria_it.wamya.common.domain.EmailTemplate;
import com.excentria_it.wamya.domain.EmailSender;
import com.excentria_it.wamya.domain.EmailSubject;
import com.excentria_it.wamya.domain.UserAccount;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StrSubstitutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.*;

@UseCase
@Transactional
@Slf4j
public class PasswordResetService implements RequestPasswordResetUseCase, ResetPasswordUseCase {

    @Autowired
    private PasswordResetProperties passwordResetProperties;

    @Autowired
    private ServerUrlProperties serverUrlProperties;

    @Autowired
    private PasswordResetRequestPort passwordResetRequestPort;

    @Autowired
    private LoadUserAccountPort loadUserAccountPort;

    @Autowired
    private OAuthUserAccountPort oAuthUserAccountPort;

    @Autowired
    private AsynchronousMessagingPort messagingPort;

    @Autowired
    private MessageSource messageSource;

    private static final String PASSWORD_RESET_URL_TEMPLATE = "${protocol}://${host}:${port}/reset-password?uuid=${uuid}&exp=${expiry}&lang=${lang}";

    @Override
    public void requestPasswordReset(String userLogin, Locale locale) {

        Optional<UserAccount> userAccountOptional = loadUserAccountPort.loadUserAccountBySubject(userLogin);
        if (userAccountOptional.isEmpty()) {
            log.warn(String.format("UserAccount not found by login: %s", userLogin));
            return;
        }

        Long userId = userAccountOptional.get().getId();
        String userEmail = userAccountOptional.get().getEmail();

        Instant now = Instant.now();

        Instant requestExpiryTimestamp = passwordResetProperties.getRequestValidity().calculateExpiry(now);

        UUID requestUUID = passwordResetRequestPort.registerRequest(userId, requestExpiryTimestamp);

        if (!this.requestSendingPasswordResetLink(userEmail, requestUUID, requestExpiryTimestamp, locale)) {
            passwordResetRequestPort.deleteRequest(requestUUID.toString());
        }

    }

    @Override
    public boolean checkRequest(String uuid, Long expiry) {
        return passwordResetRequestPort.requestExists(uuid, expiry);
    }

    @Override
    public boolean resetPassword(String uuid, String password) {

        String userOauthId = passwordResetRequestPort.getUserAccountOauthId(uuid);

        oAuthUserAccountPort.resetPassword(userOauthId, password);
        passwordResetRequestPort.deleteRequest(uuid);

        return true;

    }

    private boolean requestSendingPasswordResetLink(String userEmail, UUID requestUUID, Instant requestExpiryTimestamp,
                                                    Locale locale) {

        Map<String, String> data = new HashMap<>();
        data.put("protocol", serverUrlProperties.getProtocol());
        data.put("host", serverUrlProperties.getHost());
        data.put("port", serverUrlProperties.getPort());
        data.put("uuid", requestUUID.toString());
        data.put("expiry", Long.valueOf(requestExpiryTimestamp.toEpochMilli()).toString());
        data.put("lang", locale.toString());

        String passwordResetLink = patchURL(PASSWORD_RESET_URL_TEMPLATE, data);

        Map<String, String> emailTemplateParams = Map.of(EmailTemplate.PASSWORD_RESET.getTemplateParams().get(0),
                passwordResetLink);

        try {

            EmailMessage emailMessage = EmailMessage.builder().from(EmailSender.FRETTO_TEAM).to(userEmail)
                    .subject(messageSource.getMessage(EmailSubject.PASSWORD_RESET_REQUEST, null, locale))
                    .template(EmailTemplate.PASSWORD_RESET).params(emailTemplateParams).language(locale.toString())
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
