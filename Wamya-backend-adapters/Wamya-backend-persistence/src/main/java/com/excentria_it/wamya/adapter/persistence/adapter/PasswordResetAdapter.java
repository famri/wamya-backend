package com.excentria_it.wamya.adapter.persistence.adapter;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import com.excentria_it.wamya.adapter.persistence.entity.PasswordResetRequestJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.UserAccountJpaEntity;
import com.excentria_it.wamya.adapter.persistence.repository.PasswordResetRequestRepository;
import com.excentria_it.wamya.adapter.persistence.repository.UserAccountRepository;
import com.excentria_it.wamya.application.port.out.PasswordResetRequestPort;
import com.excentria_it.wamya.common.annotation.PersistenceAdapter;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@PersistenceAdapter
public class PasswordResetAdapter implements PasswordResetRequestPort {

	private final PasswordResetRequestRepository passwordResetRequestRepository;
	private final UserAccountRepository userAccountRepository;

	@Override
	public UUID registerRequest(Long userId, Instant expityTimestamp) {

		Optional<UserAccountJpaEntity> userAccountOptional = userAccountRepository.findById(userId);

		PasswordResetRequestJpaEntity passwordResetEntity = new PasswordResetRequestJpaEntity(userAccountOptional.get(),
				expityTimestamp);

		passwordResetEntity = passwordResetRequestRepository.save(passwordResetEntity);

		return passwordResetEntity.getUuid();
	}

	@Override
	public void purgeExpired() {

		passwordResetRequestRepository.batchDeleteExpired(Instant.now());

	}

	@Override
	public boolean requestExists(String uuid, Long expiry) {
		if (expiry == null || uuid == null) {
			return false;
		}
		UUID uuidObj;
		try {
			uuidObj = UUID.fromString(uuid);
		} catch (IllegalArgumentException e) {
			return false;
		}

		Optional<PasswordResetRequestJpaEntity> passwordResetRequestOptional = passwordResetRequestRepository
				.findById(uuidObj);

		if (passwordResetRequestOptional.isEmpty()) {
			return false;
		} else {
			return expiry.equals(passwordResetRequestOptional.get().getExpiryTimestamp().toEpochMilli());
		}
	}

	@Override
	public void deleteRequest(String uuid) {
		if (uuid == null) {
			return;
		}
		UUID uuidObj;
		try {
			uuidObj = UUID.fromString(uuid);
		} catch (IllegalArgumentException e) {
			return;
		}
		passwordResetRequestRepository.deleteById(uuidObj);

	}

	@Override
	public Long getUserAccountOauthId(String uuid) {
		Optional<PasswordResetRequestJpaEntity> passwordResetRequestOptional = passwordResetRequestRepository
				.findById(UUID.fromString(uuid));

		if (passwordResetRequestOptional.isEmpty()) {
			return null;
		}
		return passwordResetRequestOptional.get().getAccount().getOauthId();
	}

}
