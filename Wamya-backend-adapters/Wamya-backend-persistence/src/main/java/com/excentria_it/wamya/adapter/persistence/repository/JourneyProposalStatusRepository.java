package com.excentria_it.wamya.adapter.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.excentria_it.wamya.adapter.persistence.entity.JourneyProposalStatusJpaEntity;
import com.excentria_it.wamya.adapter.persistence.entity.JourneyProposalStatusJpaEntity.JourneyProposalStatusCode;

public interface JourneyProposalStatusRepository extends JpaRepository<JourneyProposalStatusJpaEntity, Long> {

	JourneyProposalStatusJpaEntity findByCode(JourneyProposalStatusCode code);
}
