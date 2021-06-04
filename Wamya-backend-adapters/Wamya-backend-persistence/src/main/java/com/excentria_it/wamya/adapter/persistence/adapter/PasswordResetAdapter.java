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

		return passwordResetRequestRepository.existsByUuidAndExpiryTimestamp(UUID.fromString(uuid),
				Instant.ofEpochMilli(expiry));
	}

	@Override
	public void deleteRequest(String uuid, Long expiry) {
		passwordResetRequestRepository.deleteByUuidAndExpiryTimestamp(uuid, expiry);

	}

	@Override
	public Long getUserAccountOauthId(String uuid, Long expiry) {
		Optional<PasswordResetRequestJpaEntity> passwordResetRequestOptional = passwordResetRequestRepository
				.findByUuidAndExpiryTimestamp(UUID.fromString(uuid), Instant.ofEpochMilli(expiry));

		if (passwordResetRequestOptional.isEmpty()) {
			return null;
		}
		return passwordResetRequestOptional.get().getAccount().getOauthId();
	}

}
