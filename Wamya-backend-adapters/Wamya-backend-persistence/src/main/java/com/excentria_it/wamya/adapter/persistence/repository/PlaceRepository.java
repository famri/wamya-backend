package com.excentria_it.wamya.adapter.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.excentria_it.wamya.adapter.persistence.entity.PlaceJpaEntity;

public interface PlaceRepository extends JpaRepository<PlaceJpaEntity, String> {

}
