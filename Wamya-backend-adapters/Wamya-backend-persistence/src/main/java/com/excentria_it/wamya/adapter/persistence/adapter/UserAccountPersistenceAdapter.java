package com.excentria_it.wamya.adapter.persistence.adapter;

import java.util.Optional;

import com.excentria_it.wamya.adapter.persistence.entity.InternationalCallingCodeJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.UserAccountJpaEntity;
import com.excentria_it.wamya.adapter.persistence.mapper.ClientMapper;
import com.excentria_it.wamya.adapter.persistence.mapper.TransporterMapper;
import com.excentria_it.wamya.adapter.persistence.mapper.UserAccountMapper;
import com.excentria_it.wamya.adapter.persistence.repository.ClientRepository;
import com.excentria_it.wamya.adapter.persistence.repository.InternationalCallingCodeRepository;
import com.excentria_it.wamya.adapter.persistence.repository.TransporterRepository;
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

	private final TransporterRepository transporterRepository;

	private final ClientRepository clientRepository;

	private final InternationalCallingCodeRepository iccRepository;

	private final UserAccountMapper userAccountMapper;

	private final TransporterMapper transporterMapper;

	private final ClientMapper clientMapper;

	@Override
	public Long createUserAccount(UserAccount userAccount) throws UnsupportedInternationalCallingCode {

		Optional<InternationalCallingCodeJpaEntity> iccEntity = iccRepository
				.findByValue(userAccount.getMobilePhoneNumber().getInternationalCallingCode());

		if (iccEntity.isEmpty())
			throw new UnsupportedInternationalCallingCode(String.format("Unsupported international calling code %s",
					userAccount.getMobilePhoneNumber().getInternationalCallingCode()));

		UserAccountJpaEntity result;
		if (userAccount.getIsTransporter()) {

			result = transporterRepository.save(transporterMapper.mapToJpaEntity(userAccount, iccEntity.get()));
		} else {
			result = clientRepository.save(clientMapper.mapToJpaEntity(userAccount, iccEntity.get()));
		}

		return result.getId();
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
	public Optional<UserAccount> loadUserAccountByUsername(String username) {

		if (username == null || (!username.contains("@") && !username.contains("_"))) {
			return Optional.empty();
		}
		if (username.contains("@")) {
			Optional<UserAccountJpaEntity> optionalEntity = userAccountRepository.findByEmail(username);
			if (optionalEntity.isEmpty())
				return Optional.empty();
			UserAccountJpaEntity entity = optionalEntity.get();

			UserAccount userAccount = userAccountMapper.mapToDomainEntity(entity);
			return Optional.of(userAccount);
		} else {
			String[] mobileNumber = username.split("_");
			Optional<UserAccountJpaEntity> optionalEntity = userAccountRepository
					.findByMobilePhoneNumber(mobileNumber[0], mobileNumber[1]);

			if (optionalEntity.isEmpty())
				return Optional.empty();

			UserAccountJpaEntity entity = optionalEntity.get();

			UserAccount userAccount = userAccountMapper.mapToDomainEntity(entity);

			return Optional.of(userAccount);
		}

	}

}
