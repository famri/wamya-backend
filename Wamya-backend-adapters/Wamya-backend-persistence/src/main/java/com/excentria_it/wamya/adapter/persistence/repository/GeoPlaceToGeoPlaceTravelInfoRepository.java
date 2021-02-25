package com.excentria_it.wamya.adapter.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.excentria_it.wamya.adapter.persistence.entity.GeoPlaceToGeoPlaceTravelInfoJpaEntity;
import com.excentria_it.wamya.domain.JourneyTravelInfo;

public interface GeoPlaceToGeoPlaceTravelInfoRepository
		extends JpaRepository<GeoPlaceToGeoPlaceTravelInfoJpaEntity, Long> {
	@Query(value = "SELECT g.distance AS distance, g.hours AS hours, g.minutes AS minutes FROM GeoPlaceToGeoPlaceTravelInfoJpaEntity g JOIN g.geoPlaceOne p1 JOIN g.geoPlaceTwo p2 WHERE (p1.id = ?1 AND p2.id = ?2) OR (p1.id = ?2 AND p2.id = ?1)")
	Optional<JourneyTravelInfo> findByGeoPlaceOne_IdAndGeoPlaceTwo_Id(Long placeOneId,
			Long placeTwoId);
}
