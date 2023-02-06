package com.excentria_it.wamya.adapter.persistence.repository;

import com.excentria_it.wamya.adapter.persistence.entity.ClientJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.InternationalCallingCodeJpaEntity;
import com.excentria_it.wamya.domain.ValidationState;
import com.excentria_it.wamya.test.data.common.TestConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest
@ActiveProfiles(profiles = {"persistence-local"})
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
        Optional<ClientJpaEntity> clientJpaEntityOptional = clientRepository.findClientByIcc_ValueAndMobileNumber("+216",
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
        Optional<ClientJpaEntity> clientJpaEntityOptional = clientRepository.findClientByEmail("client1@gmail.com");

        ClientJpaEntity clientJpaEntity = clientJpaEntityOptional.get();
        // then

        assertEquals("client1@gmail.com", clientJpaEntity.getEmail());

    }

    @Test
    void testFindClientByClientNullSubject() {
        // given
        // when
        Optional<ClientJpaEntity> clientJpaEntityOptional = clientRepository.findClientBySubject(null);

        // then
        assertTrue(clientJpaEntityOptional.isEmpty());
    }

    @Test
    void testFindClientByClientEmailSubject() {
        InternationalCallingCodeJpaEntity icc = givenIcc("+216");
        // given
        List<ClientJpaEntity> clients = givenClients(icc);

        // when
        Optional<ClientJpaEntity> clientJpaEntityOptional = clientRepository.findClientBySubject(clients.get(0).getEmail());

        ClientJpaEntity clientJpaEntity = clientJpaEntityOptional.get();
        // then

        assertEquals(clients.get(0).getEmail(), clientJpaEntity.getEmail());
    }

    @Test
    void testFindClientByClientMobileNumberSubject() {
        InternationalCallingCodeJpaEntity icc = givenIcc("+216");
        // given
        List<ClientJpaEntity> clients = givenClients(icc);

        // when
        Optional<ClientJpaEntity> clientJpaEntityOptional = clientRepository.findClientBySubject(clients.get(0).getIcc().getValue() + "_" + clients.get(0).getMobileNumber());

        ClientJpaEntity clientJpaEntity = clientJpaEntityOptional.get();
        // then

        assertEquals(clients.get(0).getIcc().getValue(), clientJpaEntity.getIcc().getValue());
        assertEquals(clients.get(0).getMobileNumber(), clientJpaEntity.getMobileNumber());

    }


    @Test
    void testFindClientByFrenchClientMobileNumberWithoutLeadingZeroSubject() {
        InternationalCallingCodeJpaEntity icc = givenIcc("+33");
        // given
        List<ClientJpaEntity> clients = givenClientsWithoutLeadingZeroMobileNumber(icc);

        // when
        Optional<ClientJpaEntity> clientJpaEntityOptional = clientRepository.findClientBySubject(clients.get(0).getIcc().getValue() + "_0" + clients.get(0).getMobileNumber());

        ClientJpaEntity clientJpaEntity = clientJpaEntityOptional.get();
        // then

        assertEquals(clients.get(0).getIcc().getValue(), clientJpaEntity.getIcc().getValue());
        assertEquals(clients.get(0).getMobileNumber(), clientJpaEntity.getMobileNumber());

    }

    @Test
    void testFindClientByClientOauthIdSubject() {
        InternationalCallingCodeJpaEntity icc = givenIcc("+216");
        // given
        List<ClientJpaEntity> clients = givenClients(icc);

        // when
        Optional<ClientJpaEntity> clientJpaEntityOptional = clientRepository.findClientBySubject(clients.get(0).getOauthId());

        ClientJpaEntity clientJpaEntity = clientJpaEntityOptional.get();
        // then

        assertEquals(clients.get(0).getOauthId(), clientJpaEntity.getOauthId());
    }

    private List<ClientJpaEntity> givenClients(InternationalCallingCodeJpaEntity icc) {
        List<ClientJpaEntity> clients = List.of(
                new ClientJpaEntity(null, "client1-oauth-id", null, "Client1", null, ValidationState.VALIDATED,
                        TestConstants.DEFAULT_MINIBIO, null, "client1@gmail.com", null, null, icc, "22111111", null,
                        null, null, null, null, null, null, null, null),
                new ClientJpaEntity(null, "client2-oauth-id", null, "Client2", null, ValidationState.VALIDATED,
                        TestConstants.DEFAULT_MINIBIO, null, "client2@gmail.com", null, null, icc, "22222222", null,
                        null, null, null, null, null, null, null, null),
                new ClientJpaEntity(null, "client3-oauth-id", null, "Client3", null, ValidationState.VALIDATED,
                        TestConstants.DEFAULT_MINIBIO, null, "client3@gmail.com", null, null, icc, "22333333", null,
                        null, null, null, null, null, null, null, null));

        return clientRepository.saveAll(clients);
    }

    private List<ClientJpaEntity> givenClientsWithoutLeadingZeroMobileNumber(InternationalCallingCodeJpaEntity icc) {
        List<ClientJpaEntity> clients = List.of(
                new ClientJpaEntity(null, "client1-oauth-id", null, "Client1", null, ValidationState.VALIDATED,
                        TestConstants.DEFAULT_MINIBIO, null, "client1@gmail.com", null, null, icc, "711111111", null,
                        null, null, null, null, null, null, null, null),
                new ClientJpaEntity(null, "client2-oauth-id", null, "Client2", null, ValidationState.VALIDATED,
                        TestConstants.DEFAULT_MINIBIO, null, "client2@gmail.com", null, null, icc, "722222222", null,
                        null, null, null, null, null, null, null, null),
                new ClientJpaEntity(null, "client3-oauth-id", null, "Client3", null, ValidationState.VALIDATED,
                        TestConstants.DEFAULT_MINIBIO, null, "client3@gmail.com", null, null, icc, "733333333", null,
                        null, null, null, null, null, null, null, null));

        return clientRepository.saveAll(clients);
    }

    private InternationalCallingCodeJpaEntity givenIcc(String code) {
        InternationalCallingCodeJpaEntity icc = InternationalCallingCodeJpaEntity.builder().value(code).enabled(true)
                .build();
        return internationalCallingCodeRepository.save(icc);
    }
}
