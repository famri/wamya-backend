package com.excentria_it.wamya.adapter.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.excentria_it.wamya.adapter.persistence.entity.GeoPlaceToLocalityTravelInfoJpaEntity;
import com.excentria_it.wamya.domain.JourneyTravelInfo;

public interface GeoPlaceToLocalityTravelInfoRepository
		extends JpaRepository<GeoPlaceToLocalityTravelInfoJpaEntity, Long> {
	@Query(value = "SELECT g.distance AS distance, g.hours AS hours, g.minutes AS minutes FROM GeoPlaceToLocalityTravelInfoJpaEntity g JOIN g.geoPlace p JOIN g.locality l WHERE p.id = ?1 AND l.id = ?2")
	Optional<JourneyTravelInfo> findByGeoPlace_IdAndLocality_Id(Long geoPlaceId, Long localityId);
}
