package com.excentria_it.wamya.application.service;

import java.util.Optional;

import javax.transaction.Transactional;

import com.excentria_it.wamya.application.port.in.ValidateMobileValidationCodeUseCase;
import com.excentria_it.wamya.application.port.out.LoadMobileValidationCodePort;
import com.excentria_it.wamya.common.annotation.UseCase;

import lombok.RequiredArgsConstructor;

@UseCase
@Transactional
@RequiredArgsConstructor
public class CodeValidationService implements ValidateMobileValidationCodeUseCase {

	private final LoadMobileValidationCodePort loadMobileValidationCodePort;

	@Override
	public boolean validateCode(ValidateMobileValidationCodeCommand command, String username) {
		Optional<String> mobileValidationCode = loadMobileValidationCodePort.loadMobileValidationCode(username);
		if (mobileValidationCode.isEmpty()) {
			return false;
		}
		return command.getCode().equals(mobileValidationCode.get());
	}

}
