package com.codisiac.wamya.adapter.persistence.adapter;

import java.util.Optional;

import com.codisiac.wamya.adapter.persistence.entity.UserAccountJpaEntity;
import com.codisiac.wamya.adapter.persistence.mapper.UserAccountMapper;
import com.codisiac.wamya.adapter.persistence.repository.UserAccountRepository;
import com.codisiac.wamya.application.port.out.CreateUserAccountPort;
import com.codisiac.wamya.application.port.out.LoadUserAccountPort;
import com.codisiac.wamya.common.annotation.PersistenceAdapter;
import com.codisiac.wamya.domain.UserAccount;
import com.codisiac.wamya.domain.UserAccount.MobilePhoneNumber;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@PersistenceAdapter
public class UserAccountPersistenceAdapter implements CreateUserAccountPort, LoadUserAccountPort {

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

}
