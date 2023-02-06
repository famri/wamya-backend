package com.excentria_it.wamya.adapter.persistence.adapter;

import com.excentria_it.wamya.adapter.persistence.entity.UserAccountJpaEntity;
import com.excentria_it.wamya.adapter.persistence.repository.UserAccountRepository;
import com.excentria_it.wamya.adapter.persistence.repository.UserPreferenceRepository;
import com.excentria_it.wamya.domain.UserPreference;
import com.excentria_it.wamya.test.data.common.UserAccountJpaEntityTestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class UserPreferencePersistenceAdapterTests {
    private final String preferenceKey = "SOME_KEY";
    private final String preferenceValue = "SOME_VALUE";
    private final String userSubject = "some_user-oauth-id";
    private final String timezone = "timezone";
    private final String timezoneValue = "Europe/Paris";
    @Mock
    private UserAccountRepository userAccountRepository;
    @Mock
    private UserPreferenceRepository userPreferenceRepository;

    @InjectMocks
    private UserPreferencePersistenceAdapter userPreferencePersistenceAdapter;

    @Test
    void testSaveUserPreference() {
        // given
        UserAccountJpaEntity userAccountEntity = UserAccountJpaEntityTestData.defaultExistentClientJpaEntity();
        given(userAccountRepository.findBySubjectWithUserPreferences(any(String.class)))
                .willReturn(Optional.of(userAccountEntity));

        // When
        userPreferencePersistenceAdapter.saveUserPreference(preferenceKey, preferenceValue, userSubject);
        // then
        ArgumentCaptor<UserAccountJpaEntity> captor = ArgumentCaptor.forClass(UserAccountJpaEntity.class);

        then(userAccountRepository).should(times(1)).save(captor.capture());

        assertTrue(captor.getValue().getPreferences().get(preferenceKey) != null
                && preferenceValue.equals(captor.getValue().getPreferences().get(preferenceKey).getValue()));
        assertTrue(captor.getValue().getPreferences().get(preferenceKey) != null
                && captor.getValue().getPreferences().get(preferenceKey).getUserAccount().equals(userAccountEntity));
    }


    @Test
    void givenUserAccountNotFoundBySubject_WhenSaveUserPreference_ThenDoNotSavePreference() {
        // given


        given(userAccountRepository.findBySubjectWithUserPreferences(any(String.class))).willReturn(Optional.empty());

        // When
        userPreferencePersistenceAdapter.saveUserPreference(preferenceKey, preferenceValue, userSubject);
        // then

        then(userAccountRepository).should(never()).save(any(UserAccountJpaEntity.class));

    }

    @Test
    void givenUserIdAndExistentUserPreference_WhenLoadUserPreferenceByKeyAndUserId_ThenReturnUserPreference() {
        // given
        UserPreference userPreference = new UserPreference(1L, timezone, timezoneValue);
        given(userPreferenceRepository.findByKeyAndUserAccountId(any(String.class), any(Long.class)))
                .willReturn(Optional.of(userPreference));
        // when
        Optional<UserPreference> userPreferenceOptional = userPreferencePersistenceAdapter
                .loadUserPreferenceByKeyAndUserId(timezone, 1L);
        // Then

        then(userPreferenceRepository).should(times(1)).findByKeyAndUserAccountId(eq(timezone),
                eq(1L));
        assertEquals(userPreference, userPreferenceOptional.get());
    }

    @Test
    void givenNonExistentUserPreference_WhenLoadUserPreferenceByKeyAndUserId_ThenReturnEmpty() {
        // given
        given(userPreferenceRepository.findByKeyAndUserAccountId(any(String.class), any(Long.class)))
                .willReturn(Optional.empty());
        // when
        Optional<UserPreference> userPreferenceOptional = userPreferencePersistenceAdapter
                .loadUserPreferenceByKeyAndUserId(timezone, 1L);
        // Then

        then(userPreferenceRepository).should(times(1)).findByKeyAndUserAccountId(eq(timezone),
                eq(1L));
        assertTrue(userPreferenceOptional.isEmpty());
    }

    @Test
    void testLoadUserPreferenceByKeyAndSubject() {
        // given
        UserPreference userPreference = new UserPreference(1L, timezone, timezoneValue);
        given(userPreferenceRepository.findByKeyAndUserAccountSubject(any(String.class), any(String.class))).willReturn(Optional.of(userPreference));
        // when
        Optional<UserPreference> userPreferenceOptional = userPreferencePersistenceAdapter.loadUserPreferenceByKeyAndSubject("timezone", "some-user-oauth-id");
        // then
        assertEquals(userPreference.getKey(), userPreferenceOptional.get().getKey());
        assertEquals(userPreference.getId(), userPreferenceOptional.get().getId());
        assertEquals(userPreference.getValue(), userPreferenceOptional.get().getValue());
    }
}
