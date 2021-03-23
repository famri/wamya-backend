package com.excentria_it.wamya.adapter.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.excentria_it.wamya.adapter.persistence.entity.UserPreferenceJpaEntity;
import com.excentria_it.wamya.domain.UserPreference;

public interface UserPreferenceRepository extends JpaRepository<UserPreferenceJpaEntity, Long> {

	@Query(value = "SELECT new com.excentria_it.wamya.domain.UserPreference(p.userPreferenceId.id, p.userPreferenceId.key, p.value) FROM UserPreferenceJpaEntity p JOIN p.userAccount a WHERE  p.userPreferenceId.key = ?1 AND a.email = ?2")
	Optional<UserPreference> findByKeyAndUserAccountEmail(String preferenceKey, String email);

	@Query(value = "SELECT new com.excentria_it.wamya.domain.UserPreference(p.userPreferenceId.id, p.userPreferenceId.key, p.value) FROM UserPreferenceJpaEntity p JOIN p.userAccount a JOIN a.icc i WHERE  p.userPreferenceId.key = ?1 AND a.mobileNumber = ?3 AND i.value = ?2")
	Optional<UserPreference> findByKeyAndUserAccountMobileNumber(String preferenceKey, String internationalCallingCode,
			String mobileNumber);

}
