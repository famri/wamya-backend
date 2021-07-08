package com.excentria_it.wamya.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.excentria_it.wamya.application.port.in.ValidateMobileValidationCodeUseCase.ValidateMobileValidationCodeCommand;
import com.excentria_it.wamya.application.port.out.LoadUserAccountPort;
import com.excentria_it.wamya.application.port.out.UpdateUserAccountPort;
import com.excentria_it.wamya.domain.UserAccount;
import com.excentria_it.wamya.test.data.common.TestConstants;
import com.excentria_it.wamya.test.data.common.UserAccountTestData;

@ExtendWith(MockitoExtension.class)
public class CodeValidationServiceTests {
	@Mock
	private LoadUserAccountPort loadUserAccountPort;

	@Mock
	private UpdateUserAccountPort updateUserAccountPort;

	@InjectMocks
	private CodeValidationService codeValidationService;

	@Test
	void givenInexistentUserAccountByEmail_whenValidateCode_thenReturnFalse() {
		// given
		given(loadUserAccountPort.loadUserAccountByUsername(any(String.class))).willReturn(Optional.empty());
		// when
		boolean result = codeValidationService.validateCode(
				ValidateMobileValidationCodeCommand.builder().code("1234").build(), TestConstants.DEFAULT_EMAIL);
		// then
		assertFalse(result);
	}

	@Test
	void givenExistentUserAccountByEmailAndSameCode_whenValidateCode_thenReturnTrue() {
		// given
		UserAccount userAccount = UserAccountTestData.defaultClientUserAccountBuilder().build();
		given(loadUserAccountPort.loadUserAccountByUsername(any(String.class))).willReturn(Optional.of(userAccount));
		// when
		boolean result = codeValidationService.validateCode(
				ValidateMobileValidationCodeCommand.builder().code(userAccount.getMobileNumberValidationCode()).build(),
				TestConstants.DEFAULT_EMAIL);
		// then
		then(updateUserAccountPort).should(times(1)).updateIsValidatedMobileNumber(userAccount.getId(), true);
		assertTrue(result);
	}

	@Test
	void givenExistentUserAccountByEmailAndDifferentCode_whenValidateCode_thenReturnFalse() {
		// given
		UserAccount userAccount = UserAccountTestData.defaultClientUserAccountBuilder().build();
		given(loadUserAccountPort.loadUserAccountByUsername(any(String.class))).willReturn(Optional.of(userAccount));
		// when
		boolean result = codeValidationService.validateCode(
				ValidateMobileValidationCodeCommand.builder().code("0000").build(), TestConstants.DEFAULT_EMAIL);
		// then
		then(updateUserAccountPort).should(never()).updateIsValidatedMobileNumber(any(Long.class), any(Boolean.class));
		assertFalse(result);
	}
}
