package com.excentria_it.wamya.adapter.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.excentria_it.wamya.adapter.persistence.entity.UserAccountJpaEntity;

public interface UserAccountRepository extends JpaRepository<UserAccountJpaEntity, Long> {

	@Query("select u from UserAccountJpaEntity u " + "where u.icc.value = :internationalCallingCode "
			+ "and u.mobileNumber = :mobileNumber")
	Optional<UserAccountJpaEntity> findByMobilePhoneNumber(
			@Param("internationalCallingCode") String internationalCallingCode,
			@Param("mobileNumber") String mobileNumber);

	
	Optional<UserAccountJpaEntity> findByEmail(String email);

	@Query("select u from UserAccountJpaEntity u " + "where u.icc.value = :internationalCallingCode "
			+ "and u.mobileNumber = :mobileNumber "+" and u.password = :password")
	Optional<UserAccountJpaEntity> findByMobilePhoneNumberAndPassword(
			@Param("internationalCallingCode") String internationalCallingCode,
			@Param("mobileNumber") String mobileNumber, @Param("password") String password);
	
	
	Optional<UserAccountJpaEntity> findByEmailAndPassword(String email, String password);
}
