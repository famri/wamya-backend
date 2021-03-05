package com.excentria_it.wamya.adapter.persistence.repository;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.excentria_it.wamya.adapter.persistence.entity.UserAccountJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.UserPreferenceJpaEntity;

public interface UserAccountRepository extends JpaRepository<UserAccountJpaEntity, Long> {

	@Query("SELECT u FROM UserAccountJpaEntity u  WHERE u.icc.value = :internationalCallingCode AND u.mobileNumber = :mobileNumber")
	Optional<UserAccountJpaEntity> findByMobilePhoneNumber(
			@Param("internationalCallingCode") String internationalCallingCode,
			@Param("mobileNumber") String mobileNumber);

	Optional<UserAccountJpaEntity> findByEmail(String email);

	@Query("SELECT p FROM UserAccountJpaEntity u JOIN u.preferences p WHERE u.icc.value = :internationalCallingCode AND u.mobileNumber = :mobileNumber")
	Set<UserPreferenceJpaEntity> loadUserPreferencesByMobilePhoneNumber(
			@Param("internationalCallingCode") String internationalCallingCode,
			@Param("mobileNumber") String mobileNumber);

	@Query("SELECT p FROM UserAccountJpaEntity u JOIN u.preferences p WHERE u.email = :email")
	Set<UserPreferenceJpaEntity> loadUserPreferencesByEmail(@Param("email") String email);

	@Query("SELECT p FROM UserAccountJpaEntity u JOIN u.preferences p WHERE u.icc.value = :internationalCallingCode AND u.mobileNumber = :mobileNumber AND p.key = :key")
	Optional<UserPreferenceJpaEntity> loadUserPreferencesByMobilePhoneNumberAndKey(
			@Param("internationalCallingCode") String internationalCallingCode,
			@Param("mobileNumber") String mobileNumber, @Param("key") String key);

	@Query("SELECT p FROM UserAccountJpaEntity u JOIN u.preferences p WHERE u.email = :email AND p.key = :key")
	Optional<UserPreferenceJpaEntity> loadUserPreferencesByEmailAndKey(@Param("email") String email,
			@Param("key") String key);

}
