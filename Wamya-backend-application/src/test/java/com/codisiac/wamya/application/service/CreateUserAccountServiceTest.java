package com.codisiac.wamya.application.service;

import static com.codisiac.wamya.common.UserAccountTestData.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;


import java.util.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.*;
import org.mockito.junit.jupiter.*;
import org.springframework.security.crypto.password.*;

import com.codisiac.wamya.application.port.in.CreateUserAccountUseCase.*;
import com.codisiac.wamya.application.port.out.*;
import com.codisiac.wamya.application.service.exception.*;
import com.codisiac.wamya.common.*;
import com.codisiac.wamya.domain.*;
import com.codisiac.wamya.domain.MessageBuilder.*;
import com.codisiac.wamya.domain.UserAccount.*;

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
	private MessageBuilder messageBuilder;

	@InjectMocks
	private CreateUserAccountService createUserAccountService;

	@Test
	void givenExistingMobilePhoneNumber_whenRegisterUserAccountCreationDemand_thenThrowUserAccountAlreadyExistsException() {

		MobilePhoneNumber mobilePhoneNumber = defaultMobilePhoneNumber().build();

		UserPasswordPair UserPasswordPair = defaultUserPasswordPairBuilder().build();

		givenAnExistingMobilePhoneNumber(mobilePhoneNumber);

		CreateUserAccountCommand command = new CreateUserAccountCommand(mobilePhoneNumber, UserPasswordPair);

		assertThrows(UserAccountAlreadyExistsException.class,
				() -> createUserAccountService.registerUserAccountCreationDemand(command));

	}

	@Test
	void givenNonExistingMobilePhoneNumber_whenRegisterUserAccountCreationDemand_thenSucceed() {

		MobilePhoneNumber mobilePhoneNumber = defaultMobilePhoneNumber().build();

		UserPasswordPair userPasswordPair = defaultUserPasswordPairBuilder().build();

		givenNonExistingMobilePhoneNumber(mobilePhoneNumber);

		String encodedPassword = givenDefaultEncodedPassword();

		String code = givenDefaultGeneratedCode();

		Message message = givenDefaultValidationMessage(code);

		CreateUserAccountCommand command = new CreateUserAccountCommand(mobilePhoneNumber, userPasswordPair);

		boolean success = createUserAccountService.registerUserAccountCreationDemand(command);

		assertThat(success).isTrue();

		ArgumentCaptor<UserAccount> userAccountCaptor = ArgumentCaptor.forClass(UserAccount.class);

		then(createUserAccountPort).should(times(1)).createUserAccount(userAccountCaptor.capture());

		assertThat(userAccountCaptor.getValue().getMobilePhoneNumber()).isEqualTo(mobilePhoneNumber);
		assertThat(userAccountCaptor.getValue().getUserPassword().getEncodedPassword()).isEqualTo(encodedPassword);
		assertThat(userAccountCaptor.getValue().getValidationCode().getValue()).isEqualTo(code);

		then(codeGenerator).should(times(1)).generateNumericCode();

		then(messagingPort).should().sendMessage(eq(mobilePhoneNumber), eq(message));

	}

	private String givenDefaultEncodedPassword() {
		given(passwordEncoder.encode(any(String.class))).willReturn(DEFAULT_ENCODED_PASSWORD);
		return DEFAULT_ENCODED_PASSWORD;
	}

	private Message givenDefaultValidationMessage(String code) {

		Message message = new MessageBuilder().withContent(code).build();
		given(messageBuilder.withContent(code)).willReturn(messageBuilder);
		given(messageBuilder.build()).willReturn(message);
		return message;
	}

	private String givenDefaultGeneratedCode() {
		given(codeGenerator.generateNumericCode()).willReturn(DEFAULT_VALIDATION_CODE);
		return DEFAULT_VALIDATION_CODE;
	}

	private void givenNonExistingMobilePhoneNumber(MobilePhoneNumber mobilePhoneNumber) {

		given(loadUserAccountPort.loadUserAccount(any(MobilePhoneNumber.class))).willReturn(Optional.ofNullable(null));

	}

	private void givenAnExistingMobilePhoneNumber(MobilePhoneNumber mobilePhoneNumber) {

		given(loadUserAccountPort.loadUserAccount(any(MobilePhoneNumber.class)))
				.willReturn(Optional.of(notYetValidatedUserAccount().build()));

	}
}
