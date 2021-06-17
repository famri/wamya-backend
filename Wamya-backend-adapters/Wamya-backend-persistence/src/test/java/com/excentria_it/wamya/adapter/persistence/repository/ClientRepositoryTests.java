package com.excentria_it.wamya.adapter.persistence.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.excentria_it.wamya.adapter.persistence.entity.ClientJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.InternationalCallingCodeJpaEntity;
import com.excentria_it.wamya.domain.ValidationState;
import com.excentria_it.wamya.test.data.common.TestConstants;

@DataJpaTest
@ActiveProfiles(profiles = { "persistence-local" })
@AutoConfigureTestDatabase(replace = NONE)
public class ClientRepositoryTests {
	@Autowired
	private ClientRepository clientRepository;

	@Autowired
	private InternationalCallingCodeRepository internationalCallingCodeRepository;

	@BeforeEach
	void cleanDatabase() {
		clientRepository.deleteAll();
		internationalCallingCodeRepository.deleteAll();
	}

	@Test
	void testFindByIcc_ValueAndMobileNumber() {

		InternationalCallingCodeJpaEntity icc = givenIcc("+216");
		// given
		givenClients(icc);

		// when
		Optional<ClientJpaEntity> clientJpaEntityOptional = clientRepository.findByIcc_ValueAndMobileNumber("+216",
				"22111111");

		ClientJpaEntity clientJpaEntity = clientJpaEntityOptional.get();
		// then

		assertEquals("22111111", clientJpaEntity.getMobileNumber());
		assertEquals("+216", clientJpaEntity.getIcc().getValue());
	}

	@Test
	void testFindByEmail() {

		InternationalCallingCodeJpaEntity icc = givenIcc("+216");
		// given
		givenClients(icc);

		// when
		Optional<ClientJpaEntity> clientJpaEntityOptional = clientRepository.findByEmail("client1@gmail.com");

		ClientJpaEntity clientJpaEntity = clientJpaEntityOptional.get();
		// then

		assertEquals("client1@gmail.com", clientJpaEntity.getEmail());

	}

	private List<ClientJpaEntity> givenClients(InternationalCallingCodeJpaEntity icc) {
		List<ClientJpaEntity> clients = List.of(
				new ClientJpaEntity(null, null, null, "Client1", null, ValidationState.VALIDATED,
						TestConstants.DEFAULT_MINIBIO, null, "client1@gmail.com", null, null, icc, "22111111", null,
						null, null, null, null, null, null, null),
				new ClientJpaEntity(null, null, null, "Client2", null, ValidationState.VALIDATED,
						TestConstants.DEFAULT_MINIBIO, null, "client2@gmail.com", null, null, icc, "22222222", null,
						null, null, null, null, null, null, null),
				new ClientJpaEntity(null, null, null, "Client3", null, ValidationState.VALIDATED,
						TestConstants.DEFAULT_MINIBIO, null, "client3@gmail.com", null, null, icc, "22333333", null,
						null, null, null, null, null, null, null));

		return clientRepository.saveAll(clients);
	}

	private InternationalCallingCodeJpaEntity givenIcc(String code) {
		InternationalCallingCodeJpaEntity icc = InternationalCallingCodeJpaEntity.builder().value(code).enabled(true)
				.build();
		return internationalCallingCodeRepository.save(icc);
	}
}
