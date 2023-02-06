package com.excentria_it.wamya.adapter.persistence.repository;

import com.excentria_it.wamya.adapter.persistence.entity.UserPreferenceJpaEntity;
import com.excentria_it.wamya.domain.UserPreference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

import static com.excentria_it.wamya.domain.InternationalCallingCodeConstants.FRANCE_ICC;

public interface UserPreferenceRepository extends JpaRepository<UserPreferenceJpaEntity, Long> {

    @Query(value = "SELECT new com.excentria_it.wamya.domain.UserPreference(p.userPreferenceId.id, p.userPreferenceId.key, p.value) FROM UserPreferenceJpaEntity p JOIN p.userAccount a WHERE  p.userPreferenceId.key = ?1 AND a.email = ?2")
    Optional<UserPreference> findByKeyAndUserAccountEmail(String preferenceKey, String email);

    @Query(value = "SELECT new com.excentria_it.wamya.domain.UserPreference(p.userPreferenceId.id, p.userPreferenceId.key, p.value) FROM UserPreferenceJpaEntity p JOIN p.userAccount a JOIN a.icc i WHERE  p.userPreferenceId.key = ?1 AND a.mobileNumber = ?3 AND i.value = ?2")
    Optional<UserPreference> findByKeyAndUserAccountMobileNumber(String preferenceKey, String internationalCallingCode,
                                                                 String mobileNumber);

    @Query(value = "SELECT new com.excentria_it.wamya.domain.UserPreference(p.userPreferenceId.id, p.userPreferenceId.key, p.value) FROM UserPreferenceJpaEntity p JOIN p.userAccount a WHERE  p.userPreferenceId.key = ?1 AND a.id = ?2")
    Optional<UserPreference> findByKeyAndUserAccountId(String preferenceKey, Long userAccountId);

    @Query(value = "SELECT new com.excentria_it.wamya.domain.UserPreference(p.userPreferenceId.id, p.userPreferenceId.key, p.value) FROM UserPreferenceJpaEntity p JOIN p.userAccount a WHERE  p.userPreferenceId.key = ?1 AND a.oauthId = ?2")
    Optional<UserPreference> findByKeyAndUserAccountOauthId(String preferenceKey, String oauthId);

    default Optional<UserPreference> findByKeyAndUserAccountSubject(String preferenceKey, String subject) {

        if (subject == null) {
            return Optional.empty();
        }

        if (subject.contains("@")) {
            return findByKeyAndUserAccountEmail(preferenceKey, subject);
        } else if (subject.contains("_") && subject.split("_").length == 2) {

            String[] mobileNumber = subject.split("_");
            Optional<UserPreference> userEntityOptional = findByKeyAndUserAccountMobileNumber(preferenceKey, mobileNumber[0], mobileNumber[1]);
            if (userEntityOptional.isEmpty() && FRANCE_ICC.equals(mobileNumber[0]) && "0".equals(mobileNumber[1].substring(0, 1))) {
                userEntityOptional = findByKeyAndUserAccountMobileNumber(preferenceKey, mobileNumber[0], mobileNumber[1].substring(1));
            }
            return userEntityOptional;

        } else {
            return findByKeyAndUserAccountOauthId(preferenceKey, subject);
        }
    }

}
