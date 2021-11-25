package com.excentria_it.wamya.adapter.persistence.adapter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.excentria_it.wamya.adapter.persistence.entity.UserAccountJpaEntity;
import com.excentria_it.wamya.adapter.persistence.repository.UserAccountRepository;
import com.excentria_it.wamya.common.exception.UserAccountNotFoundException;
import com.excentria_it.wamya.test.data.common.TestConstants;
import com.excentria_it.wamya.test.data.common.UserAccountJpaEntityTestData;

@ExtendWith(MockitoExtension.class)
public class ConnectionStatusPersistenceAdapterTests {
	@Mock
	private UserAccountRepository userAccountRepository;

	@InjectMocks
	private ConnectionStatusPersistenceAdapter connectionStatusPersistenceAdapter;

	@Test
	void givenNonExistentUsername_whenUpdateConnectionStatus_thenThrowUserAccountNotFoundException() {
		// given
		given(userAccountRepository.findByEmail(any(String.class))).willReturn(Optional.empty());
		// When

		// then
		assertThrows(UserAccountNotFoundException.class,
				() -> connectionStatusPersistenceAdapter.updateConnectionStatus(TestConstants.DEFAULT_EMAIL, true));
	}

	@Test
	void givenNullUsername_whenUpdateConnectionStatus_thenThrowUserAccountNotFoundException() {
		// given

		// When

		// then
		assertThrows(UserAccountNotFoundException.class,
				() -> connectionStatusPersistenceAdapter.updateConnectionStatus(null, true));
	}

	@Test
	void givenNotAnEmailUsername_whenUpdateConnectionStatus_thenThrowUserAccountNotFoundException() {
		// given

		// When

		// then
		assertThrows(UserAccountNotFoundException.class,
				() -> connectionStatusPersistenceAdapter.updateConnectionStatus("THIS_IS_NOT_AN_EMAIL_USERNAME", true));
	}

	@Test
	void givenExistentEmailUsername_whenUpdateConnectionStatus_thenThrowUserAccountNotFoundException() {
		// given
		UserAccountJpaEntity userAccount = UserAccountJpaEntityTestData.defaultExistentClientJpaEntity();
		given(userAccountRepository.findByEmail(any(String.class))).willReturn(Optional.of(userAccount));
		// When
		connectionStatusPersistenceAdapter.updateConnectionStatus(TestConstants.DEFAULT_EMAIL, true);
		// then
		ArgumentCaptor<UserAccountJpaEntity> captor = ArgumentCaptor.forClass(UserAccountJpaEntity.class);
		then(userAccountRepository).should(times(1)).save(captor.capture());
		assertEquals(captor.getValue().getIsWebSocketConnected(), true);

	}
}
