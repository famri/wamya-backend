package com.excentria_it.wamya.adapter.persistence.repository;

import java.time.Instant;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.excentria_it.wamya.adapter.persistence.entity.PasswordResetRequestJpaEntity;

public interface PasswordResetRequestRepository extends JpaRepository<PasswordResetRequestJpaEntity, UUID> {

	@Modifying(flushAutomatically = true)
	@Query(value = "DELETE FROM PasswordResetRequestJpaEntity p WHERE p.expiryTimestamp <= ?1")
	void batchDeleteExpired(Instant beforeInstant);

}
