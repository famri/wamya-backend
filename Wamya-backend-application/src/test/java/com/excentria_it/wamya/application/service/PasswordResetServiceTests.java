package com.excentria_it.wamya.application.service;

import com.excentria_it.wamya.application.port.out.AsynchronousMessagingPort;
import com.excentria_it.wamya.application.port.out.LoadUserAccountPort;
import com.excentria_it.wamya.application.port.out.OAuthUserAccountPort;
import com.excentria_it.wamya.application.port.out.PasswordResetRequestPort;
import com.excentria_it.wamya.application.props.PasswordResetProperties;
import com.excentria_it.wamya.application.props.ServerUrlProperties;
import com.excentria_it.wamya.common.domain.EmailMessage;
import com.excentria_it.wamya.common.domain.EmailTemplate;
import com.excentria_it.wamya.domain.UserAccount;
import com.excentria_it.wamya.domain.Validity;
import com.excentria_it.wamya.test.data.common.TestConstants;
import com.excentria_it.wamya.test.data.common.UserAccountTestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.time.Instant;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class PasswordResetServiceTests {

    @Mock
    private PasswordResetProperties passwordResetProperties;

    @Mock
    private ServerUrlProperties serverUrlProperties;

    @Mock
    private PasswordResetRequestPort passwordResetRequestPort;

    @Mock
    private LoadUserAccountPort loadUserAccountPort;

    @Mock
    private OAuthUserAccountPort oAuthUserAccountPort;

    @Mock
    private AsynchronousMessagingPort messagingPort;

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private PasswordResetService passwordResetService;

    @Test
    void givenUserAccountNotFoundByUsername_whenRequestPasswordReset_thenReturnWithoutRegistringRequest() {
        // given
        given(loadUserAccountPort.loadUserAccountBySubject(any(String.class))).willReturn(Optional.empty());

        // when
        passwordResetService.requestPasswordReset(TestConstants.DEFAULT_EMAIL, new Locale("fr", "FR"));
        // then
        then(passwordResetRequestPort).should(never()).registerRequest(any(Long.class), any(Instant.class));
        then(messagingPort).should(never()).sendEmailMessage(any(EmailMessage.class));
    }

    @Test
    void givenExistentUserAccoun_whenRequestPasswordReset_thenReturnWithoutRegistringRequest() {

        // given
        UserAccount userAccount = UserAccountTestData.defaultClientUserAccountBuilder().build();
        given(loadUserAccountPort.loadUserAccountBySubject(any(String.class))).willReturn(Optional.of(userAccount));
        given(passwordResetProperties.getRequestValidity()).willReturn(Validity.H3);

        UUID uuid = UUID.randomUUID();

        given(passwordResetRequestPort.registerRequest(any(Long.class), any(Instant.class))).willReturn(uuid);

        ArgumentCaptor<Instant> expiryCaptor = ArgumentCaptor.forClass(Instant.class);
        ArgumentCaptor<EmailMessage> emailMessageCaptor = ArgumentCaptor.forClass(EmailMessage.class);

        Instant startInstant = Instant.now().minusMillis(10);

        // when

        passwordResetService.requestPasswordReset(TestConstants.DEFAULT_EMAIL, new Locale("fr", "FR"));
        // then
        then(passwordResetRequestPort).should(times(1)).registerRequest(eq(userAccount.getId()),
                expiryCaptor.capture());

        assertTrue(expiryCaptor.getValue().isAfter(Validity.H3.calculateExpiry(startInstant)));

        then(messagingPort).should(times(1)).sendEmailMessage(emailMessageCaptor.capture());

        assertEquals(userAccount.getEmail(), emailMessageCaptor.getValue().getTo());
        assertTrue(emailMessageCaptor.getValue().getParams()
                .get(EmailTemplate.PASSWORD_RESET.getTemplateParams().get(0)).contains(uuid.toString()));
        assertTrue(
                emailMessageCaptor.getValue().getParams().get(EmailTemplate.PASSWORD_RESET.getTemplateParams().get(0))
                        .contains(Long.valueOf(expiryCaptor.getValue().toEpochMilli()).toString()));
    }

    @Test
    void givenExceptionWhenSendingEmailMessage_whenRequestPasswordReset_thenDeleteRegistredRequest() {

        // given
        UserAccount userAccount = UserAccountTestData.defaultClientUserAccountBuilder().build();
        given(loadUserAccountPort.loadUserAccountBySubject(any(String.class))).willReturn(Optional.of(userAccount));
        given(passwordResetProperties.getRequestValidity()).willReturn(Validity.H3);

        UUID uuid = UUID.randomUUID();
        ArgumentCaptor<Instant> expiryCaptor = ArgumentCaptor.forClass(Instant.class);

        given(passwordResetRequestPort.registerRequest(any(Long.class), any(Instant.class))).willReturn(uuid);
        doThrow(IllegalArgumentException.class).when(messagingPort).sendEmailMessage(any(EmailMessage.class));

        // when

        passwordResetService.requestPasswordReset(TestConstants.DEFAULT_EMAIL, new Locale("fr", "FR"));
        // then
        then(passwordResetRequestPort).should(times(1)).registerRequest(eq(userAccount.getId()),
                expiryCaptor.capture());
        then(passwordResetRequestPort).should(times(1)).deleteRequest(uuid.toString());

    }

    @Test
    void testCheckRequestReturnsTrue() {
        // given
        given(passwordResetRequestPort.requestExists(any(String.class), any(Long.class))).willReturn(true);
        // When // Then
        assertTrue(passwordResetService.checkRequest(UUID.randomUUID().toString(), Instant.now().toEpochMilli()));

    }

    @Test
    void testCheckRequestReturnsFalse() {
        // given
        given(passwordResetRequestPort.requestExists(any(String.class), any(Long.class))).willReturn(false);
        // When

        // Then
        assertFalse(passwordResetService.checkRequest(UUID.randomUUID().toString(), Instant.now().toEpochMilli()));

    }

    @Test
    void givenUserOauthIdIsFound_whenResetPassword_thenResetPasswordAndDeleteRequest() {
        // given
        given(passwordResetRequestPort.getUserAccountOauthId(any(String.class))).willReturn("1");

        String uuid = UUID.randomUUID().toString();
        // When
        assertTrue(passwordResetService.resetPassword(uuid, "MyNewPassword"));

        // then

        then(oAuthUserAccountPort).should(times(1)).resetPassword("1", "MyNewPassword");
        then(passwordResetRequestPort).should(times(1)).deleteRequest(uuid);
    }

}
