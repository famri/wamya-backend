package com.excentria_it.wamya.adapter.persistence.adapter;

import com.excentria_it.wamya.adapter.persistence.entity.UserAccountJpaEntity;
import com.excentria_it.wamya.adapter.persistence.mapper.UserAccountMapper;
import com.excentria_it.wamya.adapter.persistence.mapper.UserAccountUpdater;
import com.excentria_it.wamya.adapter.persistence.repository.UserAccountRepository;
import com.excentria_it.wamya.application.port.out.LoadProfileInfoPort;
import com.excentria_it.wamya.application.port.out.UpdateProfileInfoPort;
import com.excentria_it.wamya.common.annotation.PersistenceAdapter;
import com.excentria_it.wamya.common.exception.UserAccountNotFoundException;
import com.excentria_it.wamya.domain.ProfileInfoDto;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@RequiredArgsConstructor
@PersistenceAdapter
public class ProfileInfoPersistenceAdapter implements LoadProfileInfoPort, UpdateProfileInfoPort {

    private final UserAccountRepository userAccountRepository;

    private final UserAccountMapper userAccountMapper;

    private final UserAccountUpdater userAccountUpdater;

    @Override
    public ProfileInfoDto loadInfo(String subject, String locale) {

        UserAccountJpaEntity userAccount = userAccountRepository.findBySubject(subject).orElseThrow(
                () -> new UserAccountNotFoundException(String.format("User not found by subject: %s", subject)));


        return userAccountMapper.mapToProfileInfoDto(userAccount, locale);
    }

    @Override
    public void updateAboutSection(String subject, Long genderId, String firstname, String lastname,
                                   LocalDate dateOfBirth, String miniBio) {

        UserAccountJpaEntity userAccount = userAccountRepository.findBySubject(subject).orElseThrow(
                () -> new UserAccountNotFoundException(String.format("User not found by subject: %s", subject)));


        userAccount = userAccountUpdater.updateAboutSection(userAccount, genderId, firstname, lastname, dateOfBirth,
                miniBio);
        userAccountRepository.save(userAccount);
    }

    @Override
    public void updateEmailSection(String subject, String email) {
        UserAccountJpaEntity userAccount = userAccountRepository.findBySubject(subject).orElseThrow(
                () -> new UserAccountNotFoundException(String.format("User not found by subject: %s", subject)));


        userAccount = userAccountUpdater.updateEmailSection(userAccount, email);
        userAccountRepository.save(userAccount);

    }

    @Override
    public void updateMobileSection(String subject, String mobileNumber, Long iccId) {
        UserAccountJpaEntity userAccount = userAccountRepository.findBySubject(subject).orElseThrow(
                () -> new UserAccountNotFoundException(String.format("User not found by subject: %s", subject)));


        userAccount = userAccountUpdater.updateMobileSection(userAccount, mobileNumber, iccId);
        userAccountRepository.save(userAccount);

    }

}
