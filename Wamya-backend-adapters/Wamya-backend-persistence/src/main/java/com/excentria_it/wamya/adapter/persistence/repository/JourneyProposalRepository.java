package com.excentria_it.wamya.adapter.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.excentria_it.wamya.adapter.persistence.entity.JourneyProposalJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.JourneyProposalStatusJpaEntity.JourneyProposalStatusCode;

public interface JourneyProposalRepository extends JpaRepository<JourneyProposalJpaEntity, Long> {

	Page<JourneyProposalJpaEntity> findByJourneyRequest_Id(Long journeyRequestId, Pageable pageable);

	Page<JourneyProposalJpaEntity> findByJourneyRequest_IdAndStatus_CodeIn(Long journeyRequestId,
			List<JourneyProposalStatusCode> statusCodes, Pageable pageable);

	Optional<JourneyProposalJpaEntity> findByIdAndJourneyRequest_Id(Long proposalId, Long journeyRequestId);

	@Query(value = "SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM JourneyProposalJpaEntity p WHERE p.id = ?1 AND p.journeyRequest.id = ?2 AND p.status.code = ?3")
	boolean existsByIdAndJourneyRequestIdAndStatusCode(Long id, Long journeyRequestId,
			JourneyProposalStatusCode statusCode);
}
