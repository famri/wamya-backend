package com.excentria_it.wamya.application.service;

import javax.transaction.Transactional;

import com.excentria_it.wamya.application.port.in.LoadProfileInfoUseCase;
import com.excentria_it.wamya.application.port.in.UpdateAboutSectionUseCase;
import com.excentria_it.wamya.application.port.in.UpdateEmailSectionUseCase;
import com.excentria_it.wamya.application.port.in.UpdateMobileSectionUseCase;
import com.excentria_it.wamya.application.port.out.LoadProfileInfoPort;
import com.excentria_it.wamya.application.port.out.UpdateProfileInfoPort;
import com.excentria_it.wamya.common.annotation.UseCase;
import com.excentria_it.wamya.domain.ProfileInfoDto;

import lombok.RequiredArgsConstructor;

@UseCase
@Transactional
@RequiredArgsConstructor
public class UserProfileService implements LoadProfileInfoUseCase, UpdateAboutSectionUseCase, UpdateEmailSectionUseCase,
		UpdateMobileSectionUseCase {

	private final LoadProfileInfoPort loadProfileInfoPort;
	private final UpdateProfileInfoPort updateProfileInfoPort;

	@Override
	public ProfileInfoDto loadProfileInfo(String username, String locale) {
		return loadProfileInfoPort.loadInfo(username, locale);
	}

	@Override
	public void updateAboutSection(UpdateAboutSectionCommand command, String username) {

		updateProfileInfoPort.updateAboutSection(username, command.getGenderId(), command.getFirstname(),
				command.getLastname(), command.getDateOfBirth(), command.getMinibio());
	}

	@Override
	public void updateEmailSection(UpdateEmailSectionCommand command, String username) {
		updateProfileInfoPort.updateEmailSection(username, command.getEmail());
	}

	@Override
	public void updateMobileSection(UpdateMobileSectionCommand command, String username) {
		updateProfileInfoPort.updateMobileSection(username, command.getMobileNumber(), command.getIccId());

	}

}
