package com.excentria_it.wamya.adapter.persistence.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.*;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.excentria_it.wamya.adapter.persistence.entity.ClientJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.PasswordResetRequestJpaEntity;
import com.excentria_it.wamya.domain.ValidationState;
import com.excentria_it.wamya.test.data.common.TestConstants;

@DataJpaTest
@ActiveProfiles(profiles = { "persistence-local" })
@AutoConfigureTestDatabase(replace = NONE)
public class PasswordResetRequestRepositoryTests {
	@Autowired
	private ClientRepository clientRepository;

	@Autowired
	private PasswordResetRequestRepository passwordResetRequestRepository;

	@BeforeEach
	void cleanDatabase() {
		passwordResetRequestRepository.deleteAll();

	}

	@Test
	void testBatchDeleteExpired() {

		// given
		Instant startInstant = Instant.now();
		List<PasswordResetRequestJpaEntity> passwordResetRequests = givenPasswordResetRequests(startInstant);

		List<PasswordResetRequestJpaEntity> requestsThatShouldNotBeDeleted = passwordResetRequests.stream()
				.filter(p -> p.getExpiryTimestamp().isAfter(startInstant)).collect(Collectors.toList());
		// when
		passwordResetRequestRepository.batchDeleteExpired(startInstant);

		List<PasswordResetRequestJpaEntity> requestThatSurvivedPurge = passwordResetRequestRepository.findAll();
		// then
		assertEquals(1,requestThatSurvivedPurge.size());
		assertEquals(requestsThatShouldNotBeDeleted.size(), requestThatSurvivedPurge.size());
		assertTrue(requestsThatShouldNotBeDeleted.containsAll(requestThatSurvivedPurge));
	}

	private List<PasswordResetRequestJpaEntity> givenPasswordResetRequests(Instant startInstant) {

		ClientJpaEntity userAccount = new ClientJpaEntity(null, null, null, "Client1", null,ValidationState.VALIDATED, TestConstants.DEFAULT_MINIBIO,  null, "client1@gmail.com",
				null, null, null, "22111111", null, null, null, null, null, null, null, null,null);

		userAccount = clientRepository.save(userAccount);

		List<PasswordResetRequestJpaEntity> passwordResetRequests = List.of(
				new PasswordResetRequestJpaEntity(userAccount, startInstant.minusSeconds(10L)),
				new PasswordResetRequestJpaEntity(userAccount, startInstant.minusSeconds(20L)),
				new PasswordResetRequestJpaEntity(userAccount, startInstant.plusSeconds(10L)));

		return passwordResetRequestRepository.saveAll(passwordResetRequests);
	}
}
