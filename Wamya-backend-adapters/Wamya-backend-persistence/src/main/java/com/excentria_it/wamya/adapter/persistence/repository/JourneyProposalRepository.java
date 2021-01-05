package com.excentria_it.wamya.adapter.persistence.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.excentria_it.wamya.adapter.persistence.entity.JourneyProposalJpaEntity;

public interface JourneyProposalRepository extends JpaRepository<JourneyProposalJpaEntity, Long> {

	Page<JourneyProposalJpaEntity> findByJourneyRequest_Id(Long journeyRequestId, Pageable pageable);

	Optional<JourneyProposalJpaEntity> findByIdAndJourneyRequest_Id(Long proposalId, Long journeyRequestId);
}
