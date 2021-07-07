package com.excentria_it.wamya.adapter.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.excentria_it.wamya.adapter.persistence.entity.UserAccountJpaEntity;

public interface UserAccountRepository extends JpaRepository<UserAccountJpaEntity, Long> {

	@Query("SELECT u FROM UserAccountJpaEntity u  WHERE u.icc.value = :internationalCallingCode AND u.mobileNumber = :mobileNumber")
	Optional<UserAccountJpaEntity> findByMobilePhoneNumber(
			@Param("internationalCallingCode") String internationalCallingCode,
			@Param("mobileNumber") String mobileNumber);

	Optional<UserAccountJpaEntity> findByEmail(String email);

	@Query("SELECT u FROM UserAccountJpaEntity u LEFT JOIN FETCH u.preferences p WHERE u.email = :email")
	Optional<UserAccountJpaEntity> findByEmailWithUserPreferences(@Param("email") String email);

	@Query("SELECT u FROM UserAccountJpaEntity u LEFT JOIN FETCH u.preferences p WHERE u.icc.value = :internationalCallingCode AND u.mobileNumber = :mobileNumber")
	Optional<UserAccountJpaEntity> findByMobilePhoneNumberWithUserPreferences(
			@Param("internationalCallingCode") String internationalCallingCode,
			@Param("mobileNumber") String mobileNumber);

	Boolean existsByOauthId(Long userOauthId);
	
	Optional<UserAccountJpaEntity> findByOauthId(Long userOauthId);
	
	@Query("SELECT u.mobileNumberValidationCode FROM UserAccountJpaEntity u WHERE u.email = :email")
	Optional<String> findMobileNumberValidationCodeByEmail(String username);
}
