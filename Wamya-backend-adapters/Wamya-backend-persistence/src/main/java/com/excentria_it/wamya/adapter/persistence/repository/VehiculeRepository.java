package com.excentria_it.wamya.adapter.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.excentria_it.wamya.adapter.persistence.entity.VehiculeJpaEntity;

public interface VehiculeRepository extends JpaRepository<VehiculeJpaEntity, Long> {

}
