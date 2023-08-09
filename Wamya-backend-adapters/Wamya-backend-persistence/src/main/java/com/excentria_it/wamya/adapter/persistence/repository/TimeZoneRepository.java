package com.excentria_it.wamya.adapter.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.excentria_it.wamya.adapter.persistence.entity.TimeZoneJpaEntity;

public interface TimeZoneRepository extends JpaRepository<TimeZoneJpaEntity, Long> {

}
