package com.excentria_it.wamya.adapter.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.excentria_it.wamya.adapter.persistence.entity.UserAccountJpaEntity;

public interface UserAccountRepository extends JpaRepository<UserAccountJpaEntity, Long> {

	@Query("select u from UserAccountJpaEntity u " + "where u.icc.code = :internationalCallingCode "
			+ "and u.mobileNumber = :mobileNumber")
	Optional<UserAccountJpaEntity> findByMobilePhoneNumber(
			@Param("internationalCallingCode") String internationalCallingCode,
			@Param("mobileNumber") String mobileNumber);

	@Query("select u from UserAccountJpaEntity u " + "where u.email = :email")
	Optional<UserAccountJpaEntity> findByEmail(@Param("email") String email);
}
