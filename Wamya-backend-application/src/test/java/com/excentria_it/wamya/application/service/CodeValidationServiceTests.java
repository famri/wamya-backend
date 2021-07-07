package com.excentria_it.wamya.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.excentria_it.wamya.application.port.in.ValidateMobileValidationCodeUseCase.ValidateMobileValidationCodeCommand;
import com.excentria_it.wamya.application.port.out.LoadMobileValidationCodePort;
import com.excentria_it.wamya.test.data.common.TestConstants;

@ExtendWith(MockitoExtension.class)
public class CodeValidationServiceTests {
	@Mock
	private LoadMobileValidationCodePort loadMobileValidationCodePort;

	@InjectMocks
	private CodeValidationService codeValidationService;

	@Test
	void givenInexistentUserAccountByEmail_whenValidateCode_thenReturnFalse() {
		// given
		given(loadMobileValidationCodePort.loadMobileValidationCode(any(String.class))).willReturn(Optional.empty());
		// when
		boolean result = codeValidationService.validateCode(
				ValidateMobileValidationCodeCommand.builder().code("1234").build(), TestConstants.DEFAULT_EMAIL);
		// then
		assertFalse(result);
	}

	@Test
	void givenExistentUserAccountByEmailAndSameCode_whenValidateCode_thenReturnTrue() {
		// given
		given(loadMobileValidationCodePort.loadMobileValidationCode(any(String.class))).willReturn(Optional.of("1234"));
		// when
		boolean result = codeValidationService.validateCode(
				ValidateMobileValidationCodeCommand.builder().code("1234").build(), TestConstants.DEFAULT_EMAIL);
		// then
		assertTrue(result);
	}

	@Test
	void givenExistentUserAccountByEmailAndDifferentCode_whenValidateCode_thenReturnFAlse() {
		// given
		given(loadMobileValidationCodePort.loadMobileValidationCode(any(String.class))).willReturn(Optional.of("4321"));
		// when
		boolean result = codeValidationService.validateCode(
				ValidateMobileValidationCodeCommand.builder().code("1234").build(), TestConstants.DEFAULT_EMAIL);
		// then
		assertFalse(result);
	}
}
