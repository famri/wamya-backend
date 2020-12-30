package com.excentria_it.wamya.adapter.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.excentria_it.wamya.adapter.persistence.entity.ClientJpaEntity;

public interface ClientRepository extends JpaRepository<ClientJpaEntity, Long> {

	@Query("SELECT u FROM ClientJpaEntity u WHERE u.icc.value = :internationalCallingCode AND u.mobileNumber = :mobileNumber")
	Optional<ClientJpaEntity> findByMobilePhoneNumber(
			@Param("internationalCallingCode") String internationalCallingCode,
			@Param("mobileNumber") String mobileNumber);

	Optional<ClientJpaEntity> findByEmail(String email);
}
