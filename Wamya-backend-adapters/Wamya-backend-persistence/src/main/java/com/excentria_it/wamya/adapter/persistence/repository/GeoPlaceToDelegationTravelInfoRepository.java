package com.excentria_it.wamya.adapter.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.excentria_it.wamya.adapter.persistence.entity.GeoPlaceToDelegationTravelInfoJpaEntity;
import com.excentria_it.wamya.domain.JourneyTravelInfo;

public interface GeoPlaceToDelegationTravelInfoRepository
		extends JpaRepository<GeoPlaceToDelegationTravelInfoJpaEntity, Long> {
	@Query(value = "SELECT g.distance AS distance, g.hours AS hours, g.minutes AS minutes FROM GeoPlaceToDelegationTravelInfoJpaEntity g JOIN g.geoPlace p JOIN g.delegation d WHERE p.id = ?1 AND d.id = ?2")
	Optional<JourneyTravelInfo> findByGeoPlace_IdAndDelegation_Id(Long geoPlaceId, Long delegationId);
}
