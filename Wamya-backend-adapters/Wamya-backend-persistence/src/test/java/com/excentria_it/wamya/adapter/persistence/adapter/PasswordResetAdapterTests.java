package com.excentria_it.wamya.adapter.persistence.adapter;

import com.excentria_it.wamya.adapter.persistence.entity.PasswordResetRequestJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.UserAccountJpaEntity;
import com.excentria_it.wamya.adapter.persistence.repository.PasswordResetRequestRepository;
import com.excentria_it.wamya.adapter.persistence.repository.UserAccountRepository;
import com.excentria_it.wamya.test.data.common.UserAccountJpaEntityTestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class PasswordResetAdapterTests {
    @Mock
    private PasswordResetRequestRepository passwordResetRequestRepository;
    @Mock
    private UserAccountRepository userAccountRepository;
    @InjectMocks
    private PasswordResetAdapter passwordResetAdapter;

    @Test
    void testRegisterRequest() {
        // given

        UserAccountJpaEntity userAccount = UserAccountJpaEntityTestData.defaultExistentClientJpaEntity();
        given(userAccountRepository.findById(any(Long.class))).willReturn(Optional.of(userAccount));

        Instant expiry = Instant.now().plusMillis(3600000);

        PasswordResetRequestJpaEntity passwordResetRequestJpaEntity = new PasswordResetRequestJpaEntity(userAccount,
                expiry);

        UUID uuid = UUID.randomUUID();
        passwordResetRequestJpaEntity.setUuid(uuid);

        given(passwordResetRequestRepository.save(any(PasswordResetRequestJpaEntity.class)))
                .willReturn(passwordResetRequestJpaEntity);
        // when

        UUID returnedUuid = passwordResetAdapter.registerRequest(1L, expiry);
        ArgumentCaptor<PasswordResetRequestJpaEntity> resetPasswordRequestCaptor = ArgumentCaptor
                .forClass(PasswordResetRequestJpaEntity.class);
        // then
        then(passwordResetRequestRepository).should(times(1)).save(resetPasswordRequestCaptor.capture());
        assertEquals(userAccount, resetPasswordRequestCaptor.getValue().getAccount());
        assertEquals(expiry, resetPasswordRequestCaptor.getValue().getExpiryTimestamp());
        assertEquals(uuid, returnedUuid);

    }

    @Test
    void testPurgeExpired() {
        // given
        Instant startTime = Instant.now().minusMillis(10);
        ArgumentCaptor<Instant> instantCaptor = ArgumentCaptor.forClass(Instant.class);
        // when
        passwordResetAdapter.purgeExpired();
        // then
        then(passwordResetRequestRepository).should(times(1)).batchDeleteExpired(instantCaptor.capture());
        assertTrue(instantCaptor.getValue().isAfter(startTime));
    }

    @Test
    void givenNullUUID_whenRequestExists_thenReturnFalse() {
        // given
        Instant expiry = Instant.now().plusMillis(3600000);

        // When
        boolean result = passwordResetAdapter.requestExists(null, expiry.toEpochMilli());
        // then
        assertFalse(result);
    }

    @Test
    void givenNullExpiry_whenRequestExists_thenReturnFalse() {
        // given

        UUID uuid = UUID.randomUUID();
        // When
        boolean result = passwordResetAdapter.requestExists(uuid.toString(), null);
        // then
        assertFalse(result);
    }

    @Test
    void givenBadUUID_whenRequestExists_thenReturnFalse() {
        // given

        Instant expiry = Instant.now().plusMillis(3600000);

        // When
        boolean result = passwordResetAdapter.requestExists("ThisIsABadUUID", expiry.toEpochMilli());
        // then
        assertFalse(result);
    }

    @Test
    void testRequestExistsTrue() {
        // given
        UUID uuid = UUID.randomUUID();
        Instant expiry = Instant.now().plusMillis(3600000);

        UserAccountJpaEntity userAccount = UserAccountJpaEntityTestData.defaultExistentClientJpaEntity();
        PasswordResetRequestJpaEntity passwordResetRequestJpaEntity = new PasswordResetRequestJpaEntity(userAccount,
                expiry);

        passwordResetRequestJpaEntity.setUuid(uuid);

        given(passwordResetRequestRepository.findById(any(UUID.class)))
                .willReturn(Optional.of(passwordResetRequestJpaEntity));
        // When
        boolean result = passwordResetAdapter.requestExists(uuid.toString(), expiry.toEpochMilli());
        // then
        assertTrue(result);
    }

    @Test
    void testRequestExistsFalse() {
        // given
        UUID uuid = UUID.randomUUID();
        Instant expiry = Instant.now().plusMillis(3600000);
        given(passwordResetRequestRepository.findById(any(UUID.class))).willReturn(Optional.empty());
        // When
        boolean result = passwordResetAdapter.requestExists(uuid.toString(), expiry.toEpochMilli());
        // then
        assertFalse(result);
    }

    @Test
    void givenNullUUID_whenDeleteRequestThenExit() {
        // given

        // When
        passwordResetAdapter.deleteRequest(null);
        // then
        then(passwordResetRequestRepository).should(never()).deleteById(any(UUID.class));
    }

    @Test
    void givenBadUUID_whenDeleteRequestThenExit() {
        // given

        // When
        passwordResetAdapter.deleteRequest("ThisIsABadUUID");
        // then
        then(passwordResetRequestRepository).should(never()).deleteById(any(UUID.class));
    }

    @Test
    void testDeleteRequest() {
        // given
        UUID uuid = UUID.randomUUID();

        // When
        passwordResetAdapter.deleteRequest(uuid.toString());
        // then
        then(passwordResetRequestRepository).should(times(1)).deleteById(uuid);
    }

    @Test
    void givenExistentPasswordResetRequest_whenGetUserAccountOauthId_thenReturnNull() {

        // given
        UserAccountJpaEntity userAccount = UserAccountJpaEntityTestData.defaultExistentClientJpaEntity();

        Instant expiry = Instant.now().plusMillis(3600000);
        PasswordResetRequestJpaEntity passwordResetRequestJpaEntity = new PasswordResetRequestJpaEntity(userAccount,
                expiry);

        UUID uuid = UUID.randomUUID();
        passwordResetRequestJpaEntity.setUuid(uuid);
        given(passwordResetRequestRepository.findById(any(UUID.class)))
                .willReturn(Optional.of(passwordResetRequestJpaEntity));
        // When
        String oauthId = passwordResetAdapter.getUserAccountOauthId(uuid.toString());
        // then
        assertEquals(userAccount.getOauthId(), oauthId);
    }

    @Test
    void givenInexistentPasswordResetRequest_whenGetUserAccountOauthId_thenReturnNull() {

        // given

        UUID uuid = UUID.randomUUID();

        given(passwordResetRequestRepository.findById(any(UUID.class))).willReturn(Optional.empty());
        // When
        String oauthId = passwordResetAdapter.getUserAccountOauthId(uuid.toString());
        // then
        assertNull(oauthId);
    }

}
