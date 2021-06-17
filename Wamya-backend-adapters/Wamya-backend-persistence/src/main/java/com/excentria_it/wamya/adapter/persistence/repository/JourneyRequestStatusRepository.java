package com.excentria_it.wamya.adapter.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.excentria_it.wamya.adapter.persistence.entity.JourneyRequestStatusJpaEntity;
import com.excentria_it.wamya.domain.JourneyRequestStatusCode;

public interface JourneyRequestStatusRepository extends JpaRepository<JourneyRequestStatusJpaEntity, Long> {
	JourneyRequestStatusJpaEntity findByCode(JourneyRequestStatusCode code);
}
