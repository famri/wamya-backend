package com.excentria_it.wamya.adapter.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.excentria_it.wamya.adapter.persistence.entity.VehicleJpaEntity;

public interface VehicleRepository extends JpaRepository<VehicleJpaEntity, Long> {

}
