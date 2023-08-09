package com.excentria_it.wamya.adapter.persistence.adapter;

import com.excentria_it.wamya.adapter.persistence.entity.UserAccountJpaEntity;
import com.excentria_it.wamya.adapter.persistence.repository.UserAccountRepository;
import com.excentria_it.wamya.application.port.out.ConnectionStatusUpdatePort;
import com.excentria_it.wamya.common.annotation.PersistenceAdapter;
import com.excentria_it.wamya.common.exception.UserAccountNotFoundException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@PersistenceAdapter
public class ConnectionStatusPersistenceAdapter implements ConnectionStatusUpdatePort {

	private final UserAccountRepository userAccountRepository;

	@Override
	public void updateConnectionStatus(String username, Boolean isConnected) {
		UserAccountJpaEntity userAccount = findUserAccountJpaEntityByUsername(username);

		userAccount.setIsWebSocketConnected(isConnected);
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

		throw new UserAccountNotFoundException("Username should be an email or a mobile number.");

	}
}
