package com.excentria_it.wamya.application.service;

import java.util.Optional;

import javax.transaction.Transactional;

import com.excentria_it.wamya.application.port.in.ValidateMobileValidationCodeUseCase;
import com.excentria_it.wamya.application.port.out.LoadUserAccountPort;
import com.excentria_it.wamya.application.port.out.UpdateUserAccountPort;
import com.excentria_it.wamya.common.annotation.UseCase;
import com.excentria_it.wamya.domain.UserAccount;

import lombok.RequiredArgsConstructor;

@UseCase
@Transactional
@RequiredArgsConstructor
public class CodeValidationService implements ValidateMobileValidationCodeUseCase {

	private final LoadUserAccountPort loadUserAccountPort;
	private final UpdateUserAccountPort updateUserAccountPort;

	@Override
	public boolean validateCode(ValidateMobileValidationCodeCommand command, String username) {

		Optional<UserAccount> userAccount = loadUserAccountPort.loadUserAccountByUsername(username);
		if (userAccount.isEmpty()) {
			return false;
		}
		boolean isValid = command.getCode().equals(userAccount.get().getMobileNumberValidationCode());
		if (isValid) {
			updateUserAccountPort.updateIsValidatedMobileNumber(userAccount.get().getId(), true);
		}
		return isValid;
	}

}
