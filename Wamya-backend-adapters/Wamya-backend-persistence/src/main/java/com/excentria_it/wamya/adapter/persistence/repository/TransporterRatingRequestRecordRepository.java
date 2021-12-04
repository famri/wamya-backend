package com.excentria_it.wamya.adapter.persistence.repository;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.excentria_it.wamya.adapter.persistence.entity.TransporterRatingRequestRecordJpaEntity;
import com.excentria_it.wamya.domain.TransporterRatingRequestStatus;

public interface TransporterRatingRequestRecordRepository
		extends JpaRepository<TransporterRatingRequestRecordJpaEntity, Long> {

	Optional<TransporterRatingRequestRecordJpaEntity> findByHashAndStatusAndClient_Id(String hash, TransporterRatingRequestStatus status, Long clientId);

	Set<TransporterRatingRequestRecordJpaEntity> findByStatusAndRevivalsLessThan(TransporterRatingRequestStatus saved,
			Integer maxRevives);

	@Modifying
	@Query(value = "UPDATE TransporterRatingRequestRecordJpaEntity t SET t.revivals = t.revivals + 1 WHERE t.id IN ?1")
	void incrementRevivals(Set<Long> ids);

	@Modifying
	@Query(value = "UPDATE TransporterRatingRequestRecordJpaEntity t SET t.status = ?2   WHERE t.id = ?1")
	void updateStatus(Long ratingRequestId, TransporterRatingRequestStatus status);
}
