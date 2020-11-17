package com.excentria_it.wamya.adapter.persistence.adapter;

import java.util.Optional;

import com.excentria_it.wamya.adapter.persistence.entity.InternationalCallingCodeJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.UserAccountJpaEntity;
import com.excentria_it.wamya.adapter.persistence.mapper.UserAccountMapper;
import com.excentria_it.wamya.adapter.persistence.repository.InternationalCallingCodeRepository;
import com.excentria_it.wamya.adapter.persistence.repository.UserAccountRepository;
import com.excentria_it.wamya.application.port.out.CreateUserAccountPort;
import com.excentria_it.wamya.application.port.out.LoadUserAccountPort;
import com.excentria_it.wamya.application.port.out.UpdateUserAccountPort;
import com.excentria_it.wamya.common.annotation.PersistenceAdapter;
import com.excentria_it.wamya.common.exception.UnsupportedInternationalCallingCode;
import com.excentria_it.wamya.common.exception.UserAccountNotFoundException;
import com.excentria_it.wamya.domain.UserAccount;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@PersistenceAdapter
public class UserAccountPersistenceAdapter
		implements CreateUserAccountPort, LoadUserAccountPort, UpdateUserAccountPort {

	private final UserAccountRepository userAccountRepository;

	private final InternationalCallingCodeRepository iccRepository;

	private final UserAccountMapper userAccountMapper;

	@Override
	public Long createUserAccount(UserAccount userAccount) throws UnsupportedInternationalCallingCode {
		Optional<InternationalCallingCodeJpaEntity> iccEntity = iccRepository
				.findByValue(userAccount.getMobilePhoneNumber().getInternationalCallingCode());
		if (iccEntity.isEmpty())
			throw new UnsupportedInternationalCallingCode(String.format("Unsupported international calling code %s",
					userAccount.getMobilePhoneNumber().getInternationalCallingCode()));
		UserAccountJpaEntity result = userAccountRepository
				.save(userAccountMapper.mapToJpaEntity(userAccount, iccEntity.get()));
		return result.getId();
	}

	@Override
	public Optional<UserAccount> loadUserAccountByIccAndMobileNumber(String icc, String mobileNumber) {

		if (mobileNumber == null || icc == null)
			return Optional.ofNullable(null);

		Optional<UserAccountJpaEntity> optionalEntity = userAccountRepository.findByMobilePhoneNumber(icc,
				mobileNumber);

		if (optionalEntity.isEmpty())
			return Optional.ofNullable(null);

		UserAccountJpaEntity entity = optionalEntity.get();

		UserAccount userAccount = userAccountMapper.mapToDomainEntity(entity);

		return Optional.ofNullable(userAccount);
	}

	@Override
	public void updateUserAccount(UserAccount userAccount) {

		if (userAccount.getId() != null) {

			Optional<UserAccountJpaEntity> optionalEntity = userAccountRepository.findById(userAccount.getId());

			if (optionalEntity.isPresent()) {
				Optional<InternationalCallingCodeJpaEntity> iccEntity = iccRepository
						.findByValue(userAccount.getMobilePhoneNumber().getInternationalCallingCode());
				if (iccEntity.isEmpty()) {
					throw new UnsupportedInternationalCallingCode(
							String.format("Unsupported international calling code %s",
									userAccount.getMobilePhoneNumber().getInternationalCallingCode()));
				}
				UserAccountJpaEntity entity = userAccountMapper.mapToJpaEntity(userAccount, iccEntity.get());
				userAccountRepository.save(entity);
			} else
				throw new UserAccountNotFoundException(
						String.format("No account was found by ID %d.", userAccount.getId()));
		} else {
			throw new UnsupportedOperationException("Should not update an Entity that does not already exist");
		}

	}

	@Override
	public Optional<UserAccount> loadUserAccountByEmail(String email) {
		if (email == null)
			return Optional.ofNullable(null);

		Optional<UserAccountJpaEntity> optionalEntity = userAccountRepository.findByEmail(email);

		if (optionalEntity.isEmpty())
			return Optional.ofNullable(null);

		UserAccountJpaEntity entity = optionalEntity.get();

		UserAccount userAccount = userAccountMapper.mapToDomainEntity(entity);

		return Optional.ofNullable(userAccount);
	}

	@Override
	public Optional<UserAccount> loadUserAccountByIccAndMobileNumberAndPassword(String icc, String mobileNumber,
			String password) {
		if (mobileNumber == null || icc == null || password == null)
			return Optional.ofNullable(null);

		Optional<UserAccountJpaEntity> optionalEntity = userAccountRepository.findByMobilePhoneNumberAndPassword(icc,
				mobileNumber, password);

		if (optionalEntity.isEmpty())
			return Optional.ofNullable(null);

		UserAccountJpaEntity entity = optionalEntity.get();

		UserAccount userAccount = userAccountMapper.mapToDomainEntity(entity);

		return Optional.ofNullable(userAccount);
	}

	@Override
	public Optional<UserAccount> loadUserAccountByEmailAndPassword(String email, String password) {
		if (email == null || password == null)
			return Optional.ofNullable(null);

		Optional<UserAccountJpaEntity> optionalEntity = userAccountRepository.findByEmailAndPassword(email, password);

		if (optionalEntity.isEmpty())
			return Optional.ofNullable(null);

		UserAccountJpaEntity entity = optionalEntity.get();

		UserAccount userAccount = userAccountMapper.mapToDomainEntity(entity);

		return Optional.ofNullable(userAccount);
	}

}
