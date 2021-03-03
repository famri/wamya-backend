package com.excentria_it.wamya.adapter.persistence.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.excentria_it.wamya.adapter.persistence.entity.GeoPlaceJpaEntity;

public interface GeoPlaceRepository extends JpaRepository<GeoPlaceJpaEntity, Long> {

	List<GeoPlaceJpaEntity> findByClient_Email(String email, Sort sort);

	List<GeoPlaceJpaEntity> findByClient_Icc_ValueAndClient_MobileNumber(String icc, String mobileNumber, Sort sort);
}
