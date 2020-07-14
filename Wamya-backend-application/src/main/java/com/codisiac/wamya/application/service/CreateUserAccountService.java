package com.codisiac.wamya.application.service;

import java.util.*;

import javax.transaction.*;

import org.springframework.security.crypto.password.*;

import com.codisiac.wamya.application.port.in.*;
import com.codisiac.wamya.application.port.out.*;
import com.codisiac.wamya.application.service.exception.*;
import com.codisiac.wamya.common.*;
import com.codisiac.wamya.common.annotation.*;
import com.codisiac.wamya.domain.*;
import com.codisiac.wamya.domain.UserAccount.*;

import lombok.*;

@RequiredArgsConstructor
@UseCase
@Transactional
public class CreateUserAccountService implements CreateUserAccountUseCase {

	private final LoadUserAccountPort loadUserAccountPort;

	private final CreateUserAccountPort createUserAccountPort;

	private final MessagingPort messagingPort;

	private final CodeGenerator codeGenerator;

	private final PasswordEncoder passwordEncoder;

	private final MessageBuilder messageBuilder;

	@Override
	public boolean registerUserAccountCreationDemand(CreateUserAccountCommand command) {

		checkExistingAccount(command);

		String validationCode = codeGenerator.generateNumericCode();

		UserAccount userAccount = UserAccount.withoutId(command.getMobilePhoneNumber(),
				new UserPassword(passwordEncoder.encode(command.getUserPasswordPair().getPassword())),
				new ValidationCode(validationCode));

		createUserAccountPort.createUserAccount(userAccount);

		messagingPort.sendMessage(command.getMobilePhoneNumber(), messageBuilder.withContent(validationCode).build());

		return true;
	}

	private void checkExistingAccount(CreateUserAccountCommand command) {
		try {
			loadUserAccountPort.loadUserAccount(command.getMobilePhoneNumber()).get();
			throw new UserAccountAlreadyExistsException(command.getMobilePhoneNumber());
		} catch (NoSuchElementException e) {
			// It's OK Account does not exist
		}

	}

}
