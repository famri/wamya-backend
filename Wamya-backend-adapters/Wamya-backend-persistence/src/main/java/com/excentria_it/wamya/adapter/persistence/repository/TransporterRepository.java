package com.excentria_it.wamya.adapter.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.excentria_it.wamya.adapter.persistence.entity.TransporterJpaEntity;

public interface TransporterRepository extends JpaRepository<TransporterJpaEntity, Long> {

	@Query("SELECT u FROM TransporterJpaEntity u  WHERE u.icc.value = :internationalCallingCode AND u.mobileNumber = :mobileNumber")
	Optional<TransporterJpaEntity> findByMobilePhoneNumber(
			@Param("internationalCallingCode") String internationalCallingCode,
			@Param("mobileNumber") String mobileNumber);

	Optional<TransporterJpaEntity> findByEmail(String email);

	@Query("SELECT t FROM TransporterJpaEntity t JOIN FETCH t.vehicules v WHERE  t.email = :email")
	TransporterJpaEntity findTransporterWithVehiculesByEmail(@Param("email") String email);

	@Query("SELECT t from TransporterJpaEntity t JOIN FETCH t.vehicules v  WHERE t.icc.value = :internationalCallingCode AND t.mobileNumber = :mobileNumber")
	TransporterJpaEntity findTransporterWithVehiculesByMobilePhoneNumber(
			@Param("internationalCallingCode") String internationalCallingCode,
			@Param("mobileNumber") String mobileNumber);
}
