package com.excentria_it.wamya.adapter.persistence.adapter;

import java.time.LocalDate;

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

@RequiredArgsConstructor
@PersistenceAdapter
public class ProfileInfoPersistenceAdapter implements LoadProfileInfoPort, UpdateProfileInfoPort {

	private final UserAccountRepository userAccountRepository;

	private final UserAccountMapper userAccountMapper;

	private final UserAccountUpdater userAccountUpdater;

	@Override
	public ProfileInfoDto loadInfo(String username, String locale) {

		UserAccountJpaEntity userAccount = findUserAccountJpaEntityByUsername(username);

		return userAccountMapper.mapToProfileInfoDto(userAccount, locale);
	}

	@Override
	public void updateAboutSection(String username, Long genderId, String firstname, String lastname,
			LocalDate dateOfBirth, String minibio) {

		UserAccountJpaEntity userAccount = findUserAccountJpaEntityByUsername(username);

		userAccount = userAccountUpdater.updateAboutSection(userAccount, genderId, firstname, lastname, dateOfBirth,
				minibio);
		userAccountRepository.save(userAccount);
	}

	@Override
	public void updateEmailSection(String username, String email) {
		UserAccountJpaEntity userAccount = findUserAccountJpaEntityByUsername(username);

		userAccount = userAccountUpdater.updateEmailSection(userAccount, email);
		userAccountRepository.save(userAccount);

	}

	@Override
	public void updateMobileSection(String username, String mobileNumber, Long iccId) {
		UserAccountJpaEntity userAccount = findUserAccountJpaEntityByUsername(username);

		userAccount = userAccountUpdater.updateMobileSection(userAccount, mobileNumber, iccId);
		userAccountRepository.save(userAccount);

	}

	private UserAccountJpaEntity findUserAccountJpaEntityByUsername(String username) {
		if (username == null) {
			throw new UserAccountNotFoundException("Username cannot be null.");
		}

		if (username.contains("@")) {
			return userAccountRepository.findByEmail(username).orElseThrow(
					() -> new UserAccountNotFoundException(String.format("User not found by email: %s", username)));

		}

		if (username.contains("_")) {
			String[] mobileNumber = username.split("_");
			return userAccountRepository.findByMobilePhoneNumber(mobileNumber[0], mobileNumber[1])
					.orElseThrow(() -> new UserAccountNotFoundException(
							String.format("User not found by mobile number: %s", username)));
		}

		throw new UserAccountNotFoundException("Username should be an email or a mobile number.");

	}
}
