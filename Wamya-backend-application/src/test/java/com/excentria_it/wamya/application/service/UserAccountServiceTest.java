package com.excentria_it.wamya.application.service;

import com.excentria_it.wamya.application.port.in.CreateUserAccountUseCase.CreateUserAccountCommand;
import com.excentria_it.wamya.application.port.in.CreateUserAccountUseCase.CreateUserAccountCommand.CreateUserAccountCommandBuilder;
import com.excentria_it.wamya.application.port.out.AsynchronousMessagingPort;
import com.excentria_it.wamya.application.port.out.CreateUserAccountPort;
import com.excentria_it.wamya.application.port.out.LoadUserAccountPort;
import com.excentria_it.wamya.application.port.out.OAuthUserAccountPort;
import com.excentria_it.wamya.application.props.ServerUrlProperties;
import com.excentria_it.wamya.application.service.helper.CodeGenerator;
import com.excentria_it.wamya.common.domain.EmailMessage;
import com.excentria_it.wamya.common.domain.EmailTemplate;
import com.excentria_it.wamya.common.domain.SMSMessage;
import com.excentria_it.wamya.common.domain.SMSTemplate;
import com.excentria_it.wamya.common.exception.UserAccountAlreadyExistsException;
import com.excentria_it.wamya.domain.*;
import com.excentria_it.wamya.domain.UserAccount.MobilePhoneNumber;
import com.excentria_it.wamya.test.data.common.TestConstants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static com.excentria_it.wamya.test.data.common.TestConstants.*;
import static com.excentria_it.wamya.test.data.common.UserAccountTestData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.not;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class UserAccountServiceTest {

    @Mock
    private LoadUserAccountPort loadUserAccountPort;

    @Mock
    private CreateUserAccountPort createUserAccountPort;

    @Mock
    private AsynchronousMessagingPort messagingPort;

    @Mock
    private CodeGenerator codeGenerator;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ServerUrlProperties serverUrlProperties;

    @Mock
    private MessageSource messageSource;

    @Mock
    private OAuthUserAccountPort oAuthUserAccountPort;

    @Spy
    @InjectMocks
    private UserAccountService userAccountService;

    private Locale locale = new Locale("fr");

    @Test
    void givenExistentEmailUsername_whenRegisterUserAccountCreationDemand_thenThrowUserAccountAlreadyExistsException() {

        givenAnExistentEmailUsername();

        CreateUserAccountCommandBuilder commandBuilder = defaultClientUserAccountCommandBuilder();

        assertThrows(UserAccountAlreadyExistsException.class,
                () -> userAccountService.registerUserAccountCreationDemand(commandBuilder.build(), locale));

    }

    @Test
    void givenExistentMobileNumberUsername_whenRegisterUserAccountCreationDemand_thenThrowUserAccountAlreadyExistsException() {
        givenNonExistentEmailUsername();
        givenAnExistentMobileNumberUsername();

        CreateUserAccountCommandBuilder commandBuilder = defaultClientUserAccountCommandBuilder();

        assertThrows(UserAccountAlreadyExistsException.class,
                () -> userAccountService.registerUserAccountCreationDemand(commandBuilder.build(), locale));

    }

    @Test
    void givenNonExistentUsername_whenRegisterCustomerUserAccountCreationDemand_thenSucceed() {

        givenNonExistentEmailUsername();
        givenNonExistentMobileNumberUsername();

        givenServerUrlProperties();

        String validationCode = givenDefaultGeneratedCode();
        String uuid = givenDefaultGeneratedUUID();

        CreateUserAccountCommandBuilder commandBuilder = defaultClientUserAccountCommandBuilder();
        CreateUserAccountCommand command = commandBuilder.build();

        String emailValidationLink = givenPatchUrl();

        assertDoesNotThrow(() -> userAccountService.registerUserAccountCreationDemand(command, locale));

        ArgumentCaptor<UserAccount> userAccountCaptor = ArgumentCaptor.forClass(UserAccount.class);

        then(createUserAccountPort).should(times(1)).createUserAccount(userAccountCaptor.capture());

        assertThat(userAccountCaptor.getValue().getMobilePhoneNumber().getInternationalCallingCode())
                .isEqualTo(command.getIcc());

        assertThat(userAccountCaptor.getValue().getMobilePhoneNumber().getMobileNumber())
                .isEqualTo(command.getMobileNumber());

        assertThat(userAccountCaptor.getValue().getEmail()).isEqualTo(command.getEmail());

        assertThat(userAccountCaptor.getValue().getEmailValidationCode()).isEqualTo(uuid);

        assertThat(userAccountCaptor.getValue().getMobileNumberValidationCode()).isEqualTo(validationCode);

        then(codeGenerator).should(times(1)).generateNumericCode();
        then(codeGenerator).should(times(1)).generateUUID();

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
    void givenNonExistentMobilePhoneNumberAndNonExistentEmail_whenRegisterTransporterUserAccountCreationDemand_thenSucceed() {

        givenNonExistentEmailUsername();
        givenNonExistentMobileNumberUsername();

        givenServerUrlProperties();

        String validationCode = givenDefaultGeneratedCode();
        String uuid = givenDefaultGeneratedUUID();

        CreateUserAccountCommandBuilder commandBuilder = defaultTransporterUserAccountCommandBuilder();
        CreateUserAccountCommand command = commandBuilder.build();

        String emailValidationLink = givenPatchUrl();

        assertDoesNotThrow(() -> userAccountService.registerUserAccountCreationDemand(command, locale));

        ArgumentCaptor<UserAccount> userAccountCaptor = ArgumentCaptor.forClass(UserAccount.class);

        then(createUserAccountPort).should(times(1)).createUserAccount(userAccountCaptor.capture());

        assertThat(userAccountCaptor.getValue().getMobilePhoneNumber().getInternationalCallingCode())
                .isEqualTo(command.getIcc());

        assertThat(userAccountCaptor.getValue().getMobilePhoneNumber().getMobileNumber())
                .isEqualTo(command.getMobileNumber());

        assertThat(userAccountCaptor.getValue().getEmail()).isEqualTo(command.getEmail());

        assertThat(userAccountCaptor.getValue().getEmailValidationCode()).isEqualTo(uuid);

        assertThat(userAccountCaptor.getValue().getMobileNumberValidationCode()).isEqualTo(validationCode);

        assertThat(userAccountCaptor.getValue().getIsTransporter()).isEqualTo(command.getIsTransporter());

        then(codeGenerator).should(times(1)).generateNumericCode();
        then(codeGenerator).should(times(1)).generateUUID();

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

        ArgumentCaptor<OAuthUserAccount> oAuthUserAccount = ArgumentCaptor.forClass(OAuthUserAccount.class);

        then(oAuthUserAccountPort).should(times(1)).createOAuthUserAccount(oAuthUserAccount.capture());
        assertEquals(oAuthUserAccount.getValue().getFirstName(), command.getFirstname());
        assertEquals(oAuthUserAccount.getValue().getLastName(), command.getLastname());
        assertEquals(oAuthUserAccount.getValue().getEmail(), command.getEmail());
        assertEquals(oAuthUserAccount.getValue().getAttributes().get(OAuthUserAccountAttribute.PHONE_NUMBER), command.getIcc() + "_" + command.getMobileNumber());
        assertEquals(oAuthUserAccount.getValue().getCredentials().get(0).getValue(), command.getUserPassword());
        assertEquals(oAuthUserAccount.getValue().isEnabled(), true);

        assertEquals(oAuthUserAccount.getValue().isEnabled(), true);
        assertEquals(oAuthUserAccount.getValue().getRealmRoles(),
                command.getIsTransporter() ? List.of(UserRole.ROLE_TRANSPORTER) : List.of(UserRole.ROLE_CLIENT));

    }

    @Test
    void givenSendingSMSValidationCodeFailed_whenRegisterUserAccountCreationDemand_thenReturnAccessToken() {

        givenNonExistentEmailUsername();
        givenDefaultGeneratedCode();
        givenDefaultGeneratedUUID();
        CreateUserAccountCommand command = defaultClientUserAccountCommandBuilder().build();
        givenRequestSendingEmailValidationLinkReturns(true);
        givenRequestSendingSMSValidationCodeReturns(false);

        OpenIdAuthResponse oAuth2AccessToken = Mockito.mock(OpenIdAuthResponse.class);
        given(oAuth2AccessToken.getAccessToken()).willReturn(TestConstants.DEFAULT_ACCESS_TOKEN);

        given(oAuthUserAccountPort.fetchJwtTokenForUser(command.getEmail(), command.getUserPassword()))
                .willReturn(oAuth2AccessToken);

        OpenIdAuthResponse accessToken = userAccountService.registerUserAccountCreationDemand(command, locale);

        assertEquals(oAuth2AccessToken.getAccessToken(), accessToken.getAccessToken());

    }

    @Test
    void givenSendingEmailValidationLinkFailed_whenRegisterUserAccountCreationDemand_thenReturnCreatedUserAccountId() {

        // givenNonExistingMobilePhoneNumber();
        givenNonExistentEmailUsername();
        givenDefaultGeneratedCode();
        givenDefaultGeneratedUUID();
        CreateUserAccountCommand command = defaultClientUserAccountCommandBuilder().build();
        givenRequestSendingEmailValidationLinkReturns(false);

        OpenIdAuthResponse oAuth2AccessToken = new OpenIdAuthResponse(DEFAULT_ACCESS_TOKEN, 300L, 36000L, "REFRESH_TOEKEN", "Bearer", "ID_TOKEN",
                "read write");

        given(oAuthUserAccountPort.fetchJwtTokenForUser(command.getEmail(), command.getUserPassword()))
                .willReturn(oAuth2AccessToken);

        OpenIdAuthResponse accessToken = userAccountService.registerUserAccountCreationDemand(command, locale);

        assertEquals(oAuth2AccessToken.getAccessToken(), accessToken.getAccessToken());

    }

    @Test
    void givenSendMailMessageThrowsInvalidEmailMessageException_whenRequestSendingEmailValidationLink_thenReturnFalse() {
        givenSendMailMessageThrowsIllegalArgumentException();

        CreateUserAccountCommand command = defaultClientUserAccountCommandBuilder().build();

        boolean result = userAccountService.requestSendingEmailValidationLink(command.getEmail(),
                "SOME VALIDATION CODE", locale);
        assertEquals(false, result);
    }

    @Test
    void givenSendSMSMessageThrowsInvalidSMSMessageException_whenRequestSendingSMSValidationCode_thenReturnFalse() {
        givenSendSMSMessageThrowsIllegalArgumentException();

        CreateUserAccountCommand command = defaultClientUserAccountCommandBuilder().build();

        boolean result = userAccountService.requestSendingSMSValidationCode(
                new MobilePhoneNumber(command.getIcc(), command.getMobileNumber()), "SOME VALIDATION CODE", locale);
        assertEquals(false, result);
    }

    private String givenDefaultGeneratedCode() {
        given(codeGenerator.generateNumericCode()).willReturn(DEFAULT_VALIDATION_CODE);
        return DEFAULT_VALIDATION_CODE;
    }

    private String givenDefaultGeneratedUUID() {
        given(codeGenerator.generateUUID()).willReturn(DEFAULT_VALIDATION_UUID);
        return DEFAULT_VALIDATION_UUID;
    }

    private void givenNonExistentEmailUsername() {

        given(loadUserAccountPort.loadUserAccountByUsername(eq(TestConstants.DEFAULT_EMAIL)))
                .willReturn(Optional.empty());

    }

    private void givenAnExistentEmailUsername() {

        given(loadUserAccountPort.loadUserAccountByUsername(eq(TestConstants.DEFAULT_EMAIL)))
                .willReturn(Optional.of(notYetValidatedEmailUserAccount()));

    }

    private void givenNonExistentMobileNumberUsername() {

        given(loadUserAccountPort.loadUserAccountByUsername(eq(TestConstants.DEFAULT_MOBILE_NUMBER_USERNAME)))
                .willReturn(Optional.empty());

    }

    private void givenAnExistentMobileNumberUsername() {

        given(loadUserAccountPort.loadUserAccountByUsername(eq(TestConstants.DEFAULT_MOBILE_NUMBER_USERNAME)))
                .willReturn(Optional.of(notYetValidatedMobileNumberUserAccount()));

    }

    private void givenServerUrlProperties() {
        given(serverUrlProperties.getHost()).willReturn("HOST");
        given(serverUrlProperties.getPort()).willReturn("PORT");
        given(serverUrlProperties.getProtocol()).willReturn("PROTOCOL");

    }

    private String givenPatchUrl() {
        String result = "SOME_EMAIL_VALIDATION_LINK";

        doReturn(result).when(userAccountService).patchURL(any(String.class), any(String.class), any(String.class),
                any(String.class), any(String.class), any(String.class));
        return result;
    }

    private void givenRequestSendingSMSValidationCodeReturns(boolean result) {
        doReturn(result).when(userAccountService).requestSendingSMSValidationCode(any(MobilePhoneNumber.class),
                any(String.class), any(Locale.class));
    }

    private void givenRequestSendingEmailValidationLinkReturns(boolean result) {
        doReturn(result).when(userAccountService).requestSendingEmailValidationLink(any(String.class),
                any(String.class), any(Locale.class));
    }

    private void givenSendMailMessageThrowsIllegalArgumentException() {
        doThrow(IllegalArgumentException.class).when(messagingPort).sendEmailMessage(any(EmailMessage.class));

    }

    private void givenSendSMSMessageThrowsIllegalArgumentException() {
        doThrow(IllegalArgumentException.class).when(messagingPort).sendSMSMessage(any(SMSMessage.class));

    }
}
