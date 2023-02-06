package com.excentria_it.wamya.adapter.persistence.adapter;

import com.excentria_it.wamya.adapter.persistence.entity.UserAccountJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.UserPreferenceId;
import com.excentria_it.wamya.adapter.persistence.entity.UserPreferenceJpaEntity;
import com.excentria_it.wamya.adapter.persistence.repository.UserAccountRepository;
import com.excentria_it.wamya.adapter.persistence.repository.UserPreferenceRepository;
import com.excentria_it.wamya.application.port.out.LoadUserPreferencesPort;
import com.excentria_it.wamya.application.port.out.SaveUserPreferencePort;
import com.excentria_it.wamya.common.annotation.PersistenceAdapter;
import com.excentria_it.wamya.domain.UserPreference;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@RequiredArgsConstructor
@PersistenceAdapter
@Slf4j
public class UserPreferencePersistenceAdapter implements SaveUserPreferencePort, LoadUserPreferencesPort {

    private final UserAccountRepository userAccountRepository;
    private final UserPreferenceRepository userPreferenceRepository;

    @Override
    public void saveUserPreference(String key, String value, String subject) {

        Optional<UserAccountJpaEntity> accountOptional = userAccountRepository.findBySubjectWithUserPreferences(subject);

        if (accountOptional.isEmpty()) {
            log.error(String.format("User account not found by username: %s", subject));
            return;
        }

        UserAccountJpaEntity userAccount = accountOptional.get();

        UserPreferenceJpaEntity userPreference = new UserPreferenceJpaEntity(
                new UserPreferenceId(userAccount.getId(), key), value, userAccount);

        userAccount.getPreferences().put(key, userPreference);

        userAccountRepository.save(userAccount);
    }

    @Override
    public Optional<UserPreference> loadUserPreferenceByKeyAndSubject(String preferenceKey, String subject) {
        return userPreferenceRepository.findByKeyAndUserAccountSubject(preferenceKey, subject);
    }

    @Override
    public Optional<UserPreference> loadUserPreferenceByKeyAndUserId(String preferenceKey, Long userId) {
        return userPreferenceRepository.findByKeyAndUserAccountId(preferenceKey, userId);
    }

}
