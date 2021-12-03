package com.excentria_it.wamya.adapter.persistence.repository;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.excentria_it.wamya.adapter.persistence.entity.TransporterRatingRequestRecordJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.TransporterRatingRequestRecordJpaEntity.TransporterRatingRequestStatus;

public interface TransporterRatingRequestRecordRepository
		extends JpaRepository<TransporterRatingRequestRecordJpaEntity, Long> {

	Optional<TransporterRatingRequestRecordJpaEntity> findByHashAndClient_Id(String hash, Long clientId);

	Set<TransporterRatingRequestRecordJpaEntity> findByStatusAndRevivesLessThan(
			TransporterRatingRequestStatus saved, Integer maxRevives);

	@Modifying
	@Query(value = "UPDATE TransporterRatingRequestRecordJpaEntity t SET t.revivals = t.revivals + 1 WHERE j.id IN ?1")
	void incrementRevivals(Set<Long> ids);
}
