package com.excentria_it.wamya.application.service;

import com.excentria_it.wamya.application.port.in.LoadProfileInfoUseCase;
import com.excentria_it.wamya.application.port.in.UpdateAboutSectionUseCase;
import com.excentria_it.wamya.application.port.in.UpdateEmailSectionUseCase;
import com.excentria_it.wamya.application.port.in.UpdateMobileSectionUseCase;
import com.excentria_it.wamya.application.port.out.LoadProfileInfoPort;
import com.excentria_it.wamya.application.port.out.LoadUserAccountPort;
import com.excentria_it.wamya.application.port.out.OAuthUserAccountPort;
import com.excentria_it.wamya.application.port.out.UpdateProfileInfoPort;
import com.excentria_it.wamya.common.annotation.UseCase;
import com.excentria_it.wamya.common.exception.UserAccountAlreadyExistsException;
import com.excentria_it.wamya.domain.ProfileInfoDto;
import com.excentria_it.wamya.domain.UserAccount;
import lombok.RequiredArgsConstructor;

import javax.transaction.Transactional;
import java.util.Optional;

@UseCase
@Transactional
@RequiredArgsConstructor
public class UserProfileService implements LoadProfileInfoUseCase, UpdateAboutSectionUseCase, UpdateEmailSectionUseCase,
        UpdateMobileSectionUseCase {

    private final LoadProfileInfoPort loadProfileInfoPort;
    private final UpdateProfileInfoPort updateProfileInfoPort;
    private final OAuthUserAccountPort oAuthUserAccountPort;
    private final LoadUserAccountPort loadUserAccountPort;

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

        Optional<UserAccount> existingUserAccount = loadUserAccountPort.loadUserAccountByUsername(command.getEmail());
        if (!existingUserAccount.isEmpty()) {
            throw new UserAccountAlreadyExistsException(
                    String.format("Email address %s already registred.", command.getEmail()));
        }

        Optional<UserAccount> userAccountOptional = loadUserAccountPort.loadUserAccountByUsername(username);

        oAuthUserAccountPort.updateEmail(userAccountOptional.get().getOauthId(), command.getEmail());
        updateProfileInfoPort.updateEmailSection(username, command.getEmail());

    }

    @Override
    public void updateMobileSection(UpdateMobileSectionCommand command, String username) {
        updateProfileInfoPort.updateMobileSection(username, command.getMobileNumber(), command.getIccId());

        Optional<UserAccount> userAccountOptional = loadUserAccountPort.loadUserAccountByUsername(username);

        oAuthUserAccountPort.updateMobileNumber(userAccountOptional.get().getOauthId(),
                userAccountOptional.get().getMobilePhoneNumber().getInternationalCallingCode(),
                command.getMobileNumber());

    }

}
