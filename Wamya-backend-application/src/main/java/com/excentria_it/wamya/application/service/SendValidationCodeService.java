package com.excentria_it.wamya.application.service;

import java.util.Optional;

import javax.transaction.Transactional;

import com.excentria_it.wamya.application.port.in.SendValidationCodeUseCase;
import com.excentria_it.wamya.application.port.out.LoadUserAccountPort;
import com.excentria_it.wamya.application.port.out.MessagingPort;
import com.excentria_it.wamya.application.port.out.UpdateUserAccountPort;
import com.excentria_it.wamya.application.service.exception.UserAccountNotFoundException;
import com.excentria_it.wamya.common.CodeGenerator;
import com.excentria_it.wamya.common.annotation.UseCase;
import com.excentria_it.wamya.domain.MessageBuilder;
import com.excentria_it.wamya.domain.UserAccount;
import com.excentria_it.wamya.domain.ValidationCode;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@UseCase
@Transactional
public class SendValidationCodeService implements SendValidationCodeUseCase {

	private final CodeGenerator codeGenerator;

	private final MessagingPort messagingPort;

	private final MessageBuilder messageBuilder;

	private final LoadUserAccountPort loadUserAccountPort;

	private final UpdateUserAccountPort updateUserAccountPort;

	@Override
	public boolean sendValidationCode(SendValidationCodeCommand command) {

		UserAccount userAccount = checkExistingAccount(command);

		String validationCode = codeGenerator.generateNumericCode();

		updateValidationCode(userAccount, new ValidationCode(validationCode));

		messagingPort.sendMessage(command.getMobilePhoneNumber(), messageBuilder.withContent(validationCode).build());

		return true;
	}

	private UserAccount checkExistingAccount(SendValidationCodeCommand command) {

		Optional<UserAccount> userAccountOptional = loadUserAccountPort.loadUserAccount(command.getMobilePhoneNumber());
		if (userAccountOptional.isEmpty()) {
			throw new UserAccountNotFoundException(command.getMobilePhoneNumber());
		}
		return userAccountOptional.get();
	}

	private void updateValidationCode(UserAccount userAccount, ValidationCode validationCode) {

		userAccount.setValidationCode(validationCode);
		updateUserAccountPort.updateUserAccount(userAccount);
	}

}
