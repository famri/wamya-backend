package com.excentria_it.wamya.adapter.persistence.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.excentria_it.wamya.adapter.persistence.entity.GeoPlaceJpaEntity;

public interface GeoPlaceRepository extends JpaRepository<GeoPlaceJpaEntity, Long> {

	Set<GeoPlaceJpaEntity> findByClient_Email(String email);

	Set<GeoPlaceJpaEntity> findByClient_Icc_ValueAndClient_MobileNumber(String icc, String mobileNumber);
}
