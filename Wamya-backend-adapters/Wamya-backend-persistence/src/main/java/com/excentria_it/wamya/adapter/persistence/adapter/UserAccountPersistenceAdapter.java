package com.excentria_it.wamya.adapter.persistence.adapter;

import java.util.Optional;

import com.excentria_it.wamya.adapter.persistence.entity.UserAccountJpaEntity;
import com.excentria_it.wamya.adapter.persistence.mapper.UserAccountMapper;
import com.excentria_it.wamya.adapter.persistence.repository.UserAccountRepository;
import com.excentria_it.wamya.application.port.out.CreateUserAccountPort;
import com.excentria_it.wamya.application.port.out.LoadUserAccountPort;
import com.excentria_it.wamya.application.port.out.UpdateUserAccountPort;
import com.excentria_it.wamya.application.service.exception.UserAccountNotFoundException;
import com.excentria_it.wamya.common.annotation.PersistenceAdapter;
import com.excentria_it.wamya.domain.UserAccount;
import com.excentria_it.wamya.domain.UserAccount.MobilePhoneNumber;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@PersistenceAdapter
public class UserAccountPersistenceAdapter
		implements CreateUserAccountPort, LoadUserAccountPort, UpdateUserAccountPort {

	private final UserAccountRepository userAccountRepository;
	private final UserAccountMapper userAccountMapper;

	@Override
	public void createUserAccount(UserAccount userAccount) {
		userAccountRepository.save(userAccountMapper.mapToJpaEntity(userAccount));
	}

	@Override
	public Optional<UserAccount> loadUserAccount(MobilePhoneNumber mobilePhoneNumber) {

		Optional<UserAccountJpaEntity> optionalEntity = userAccountRepository.findByMobilePhoneNumber(
				mobilePhoneNumber.getInternationalCallingCode(), mobilePhoneNumber.getMobileNumber());

		UserAccountJpaEntity entity = optionalEntity.get();

		UserAccount userAccount = userAccountMapper.mapToDomainEntity(entity);

		return Optional.ofNullable(userAccount);
	}

	@Override
	public void updateUserAccount(UserAccount userAccount) {

		if (userAccount.getId() != null && userAccount.getId().getValue() != null) {
			Optional<UserAccountJpaEntity> optionalEntity = userAccountRepository
					.findById(userAccount.getId().getValue());
			if (optionalEntity.isPresent()) {
				userAccountRepository.save(userAccountMapper.mapToJpaEntity(userAccount));
			} else
				throw new UserAccountNotFoundException(userAccount.getId().getValue());
		}

	}

}
