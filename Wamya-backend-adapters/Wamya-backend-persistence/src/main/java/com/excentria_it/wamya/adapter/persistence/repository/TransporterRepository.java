package com.excentria_it.wamya.adapter.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.excentria_it.wamya.adapter.persistence.entity.TransporterJpaEntity;

public interface TransporterRepository extends JpaRepository<TransporterJpaEntity, Long> {

	Optional<TransporterJpaEntity> findByIcc_ValueAndMobileNumber(String internationalCallingCode, String mobileNumber);

	Optional<TransporterJpaEntity> findByEmail(String email);

	@Query("SELECT t FROM TransporterJpaEntity t LEFT JOIN t.vehicules v WHERE  t.email = :email")
	Optional<TransporterJpaEntity> findTransporterWithVehiculesByEmail(@Param("email") String email);

	@Query("SELECT t from TransporterJpaEntity t LEFT JOIN t.vehicules v  WHERE t.icc.value = :internationalCallingCode AND t.mobileNumber = :mobileNumber")
	Optional<TransporterJpaEntity> findTransporterWithVehiculesByMobilePhoneNumber(
			@Param("internationalCallingCode") String internationalCallingCode,
			@Param("mobileNumber") String mobileNumber);
}
