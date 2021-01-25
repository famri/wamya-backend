package com.excentria_it.wamya.adapter.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.excentria_it.wamya.adapter.persistence.entity.DelegationJpaEntity;

public interface DelegationRepository extends JpaRepository<DelegationJpaEntity, Long> {

}
