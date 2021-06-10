package com.excentria_it.wamya.application.service;

import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.excentria_it.wamya.application.port.in.UpdateAboutSectionUseCase.UpdateAboutSectionCommand;
import com.excentria_it.wamya.application.port.in.UpdateEmailSectionUseCase.UpdateEmailSectionCommand;
import com.excentria_it.wamya.application.port.in.UpdateMobileSectionUseCase.UpdateMobileSectionCommand;
import com.excentria_it.wamya.application.port.out.LoadProfileInfoPort;
import com.excentria_it.wamya.application.port.out.UpdateProfileInfoPort;
import com.excentria_it.wamya.test.data.common.TestConstants;
import com.excentria_it.wamya.test.data.common.UserProfileTestData;

@ExtendWith(MockitoExtension.class)
public class UserProfileServiceTests {
	@Mock
	private LoadProfileInfoPort loadProfileInfoPort;
	@Mock
	private UpdateProfileInfoPort updateProfileInfoPort;
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
		// when
		userProfileService.updateEmailSection(command, TestConstants.DEFAULT_EMAIL);

		// then
		then(updateProfileInfoPort).should(times(1)).updateEmailSection(TestConstants.DEFAULT_EMAIL,
				command.getEmail());
	}

	@Test
	void testUpdateMobileSection() {
		// given
		UpdateMobileSectionCommand command = UserProfileTestData.defaultUpdateMobileSectionCommandBuilder().build();
		// when
		userProfileService.updateMobileSection(command, TestConstants.DEFAULT_EMAIL);

		// then
		then(updateProfileInfoPort).should(times(1)).updateMobileSection(TestConstants.DEFAULT_EMAIL,
				command.getMobileNumber(), command.getIccId());
	}
}
