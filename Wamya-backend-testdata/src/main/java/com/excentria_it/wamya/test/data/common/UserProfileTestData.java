package com.excentria_it.wamya.test.data.common;

import java.time.LocalDate;

import com.excentria_it.wamya.application.port.in.UpdateAboutSectionUseCase.UpdateAboutSectionCommand;
import com.excentria_it.wamya.application.port.in.UpdateAboutSectionUseCase.UpdateAboutSectionCommand.UpdateAboutSectionCommandBuilder;
import com.excentria_it.wamya.application.port.in.UpdateEmailSectionUseCase.UpdateEmailSectionCommand;
import com.excentria_it.wamya.application.port.in.UpdateEmailSectionUseCase.UpdateEmailSectionCommand.UpdateEmailSectionCommandBuilder;
import com.excentria_it.wamya.application.port.in.UpdateMobileSectionUseCase.UpdateMobileSectionCommand;
import com.excentria_it.wamya.application.port.in.UpdateMobileSectionUseCase.UpdateMobileSectionCommand.UpdateMobileSectionCommandBuilder;
import com.excentria_it.wamya.domain.ProfileInfoDto;
import com.excentria_it.wamya.domain.ProfileInfoDto.EmailInfo;
import com.excentria_it.wamya.domain.ProfileInfoDto.GenderInfo;
import com.excentria_it.wamya.domain.ProfileInfoDto.IccInfo;
import com.excentria_it.wamya.domain.ProfileInfoDto.MobileInfo;
import com.excentria_it.wamya.domain.ProfileInfoDto.NameInfo;
import com.excentria_it.wamya.domain.ProfileInfoDto.ValidationInfo;

public class UserProfileTestData {
	public static UpdateAboutSectionCommandBuilder defaultUpdateAboutSectionCommandBuilder() {
		return UpdateAboutSectionCommand.builder().genderId(1L).firstname("client1Firstname")
				.lastname("client1Lastname").dateOfBirth(LocalDate.of(2011, 01, 14))
				.minibio("ça c'est de la vraie biographie!");
	}

	public static UpdateEmailSectionCommandBuilder defaultUpdateEmailSectionCommandBuilder() {
		return UpdateEmailSectionCommand.builder().email(TestConstants.DEFAULT_NEW_EMAIL);
	}

	public static UpdateMobileSectionCommandBuilder defaultUpdateMobileSectionCommandBuilder() {

		return UpdateMobileSectionCommand.builder().mobileNumber(TestConstants.DEFAULT_MOBILE_NUMBER).iccId(1L);
	}

	public static ProfileInfoDto defaultProfileInfoDto() {

		LocalDate dateOfBirth = LocalDate.of(2011, 01, 14);
		return new ProfileInfoDto(new GenderInfo(1L, "Homme"),
				new NameInfo("firstname", "lastname", new ValidationInfo("VALIDATED", true)), false, "/path/to/photo",
				dateOfBirth, TestConstants.DEFAULT_MINIBIO, new EmailInfo(TestConstants.DEFAULT_EMAIL, true),
				new MobileInfo(TestConstants.DEFAULT_MOBILE_NUMBER, new IccInfo(1L, "+216"), true));
	}
}
