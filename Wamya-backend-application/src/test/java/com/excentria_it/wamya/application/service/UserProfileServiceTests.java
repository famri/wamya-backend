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

import com.excentria_it.wamya.application.port.in.UpdateAboutSectionUseCase.UpdateAboutSectionCommand;
import com.excentria_it.wamya.application.port.in.UpdateEmailSectionUseCase.UpdateEmailSectionCommand;
import com.excentria_it.wamya.application.port.in.UpdateMobileSectionUseCase.UpdateMobileSectionCommand;
import com.excentria_it.wamya.application.port.out.LoadProfileInfoPort;
import com.excentria_it.wamya.application.port.out.LoadUserAccountPort;
import com.excentria_it.wamya.application.port.out.OAuthUserAccountPort;
import com.excentria_it.wamya.application.port.out.UpdateProfileInfoPort;
import com.excentria_it.wamya.common.exception.UserAccountAlreadyExistsException;
import com.excentria_it.wamya.domain.UserAccount;
import com.excentria_it.wamya.test.data.common.TestConstants;
import com.excentria_it.wamya.test.data.common.UserAccountTestData;
import com.excentria_it.wamya.test.data.common.UserProfileTestData;

@ExtendWith(MockitoExtension.class)
public class UserProfileServiceTests {
	@Mock
	private LoadProfileInfoPort loadProfileInfoPort;
	@Mock
	private UpdateProfileInfoPort updateProfileInfoPort;
	@Mock
	private OAuthUserAccountPort oAuthUserAccountPort;
	@Mock
	private LoadUserAccountPort loadUserAccountPort;

	@InjectMocks
	private UserProfileService userProfileService;

	@Test
	void testLoadProfileInfo() {
		// given //when
		userProfileService.loadProfileInfo(TestConstants.DEFAULT_EMAIL, "fr_FR");

		// then
		then(loadProfileInfoPort).should(times(1)).loadInfo(TestConstants.DEFAULT_EMAIL, "fr_FR");
	}

	@Test
	void testUpdateAboutSection() {
		// given
		UpdateAboutSectionCommand command = UserProfileTestData.defaultUpdateAboutSectionCommandBuilder().build();
		// when
		userProfileService.updateAboutSection(command, TestConstants.DEFAULT_EMAIL);

		// then
		then(updateProfileInfoPort).should(times(1)).updateAboutSection(TestConstants.DEFAULT_EMAIL,
				command.getGenderId(), command.getFirstname(), command.getLastname(), command.getDateOfBirth(),
				command.getMinibio());
	}

	@Test
	void testUpdateEmailSection() {
		// given
		UpdateEmailSectionCommand command = UserProfileTestData.defaultUpdateEmailSectionCommandBuilder().build();

		given(loadUserAccountPort.loadUserAccountByUsername(command.getEmail())).willReturn(Optional.empty());

		UserAccount userAccount = UserAccountTestData.defaultClientUserAccountBuilder().build();

		given(loadUserAccountPort.loadUserAccountByUsername(TestConstants.DEFAULT_EMAIL))
				.willReturn(Optional.of(userAccount));

		// when
		userProfileService.updateEmailSection(command, TestConstants.DEFAULT_EMAIL);

		// then

		then(loadUserAccountPort).should(times(1)).loadUserAccountByUsername(command.getEmail());

		then(loadUserAccountPort).should(times(1)).loadUserAccountByUsername(TestConstants.DEFAULT_EMAIL);

		then(updateProfileInfoPort).should(times(1)).updateEmailSection(TestConstants.DEFAULT_EMAIL,
				command.getEmail());

		then(oAuthUserAccountPort).should(times(1)).updateEmail(userAccount.getOauthId(), command.getEmail());
	}

	@Test
	void givenExistingNewEmailAccount_whenUpdateEmailSection_thenThrowUserAccountAlreadyExistsException() {
		// given
		UpdateEmailSectionCommand command = UserProfileTestData.defaultUpdateEmailSectionCommandBuilder().build();

		UserAccount existentUserAccount = UserAccountTestData.defaultClientUserAccountBuilder()
				.email(command.getEmail()).build();

		given(loadUserAccountPort.loadUserAccountByUsername(command.getEmail()))
				.willReturn(Optional.of(existentUserAccount));

		// when // then

		assertThrows(UserAccountAlreadyExistsException.class,
				() -> userProfileService.updateEmailSection(command, TestConstants.DEFAULT_EMAIL));

		then(loadUserAccountPort).should(times(1)).loadUserAccountByUsername(command.getEmail());

		then(updateProfileInfoPort).should(never()).updateEmailSection(TestConstants.DEFAULT_EMAIL, command.getEmail());

		then(oAuthUserAccountPort).should(never()).updateEmail(any(Long.class), eq(command.getEmail()));
	}

	@Test
	void testUpdateMobileSection() {
		// given
		UpdateMobileSectionCommand command = UserProfileTestData.defaultUpdateMobileSectionCommandBuilder().build();

		UserAccount userAccount = UserAccountTestData.defaultClientUserAccountBuilder().build();
		given(loadUserAccountPort.loadUserAccountByUsername(TestConstants.DEFAULT_EMAIL))
				.willReturn(Optional.of(userAccount));
		// when
		userProfileService.updateMobileSection(command, TestConstants.DEFAULT_EMAIL);

		// then
		then(updateProfileInfoPort).should(times(1)).updateMobileSection(TestConstants.DEFAULT_EMAIL,
				command.getMobileNumber(), command.getIccId());

		then(oAuthUserAccountPort).should(times(1)).updateMobileNumber(userAccount.getOauthId(),
				userAccount.getMobilePhoneNumber().getInternationalCallingCode(), command.getMobileNumber());
	}
}
