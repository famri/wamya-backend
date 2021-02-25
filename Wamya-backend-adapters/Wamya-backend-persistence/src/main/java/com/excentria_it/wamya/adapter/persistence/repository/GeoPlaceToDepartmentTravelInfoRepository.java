package com.excentria_it.wamya.adapter.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.excentria_it.wamya.adapter.persistence.entity.GeoPlaceToDepartmentTravelInfoJpaEntity;
import com.excentria_it.wamya.domain.JourneyTravelInfo;

public interface GeoPlaceToDepartmentTravelInfoRepository
		extends JpaRepository<GeoPlaceToDepartmentTravelInfoJpaEntity, Long> {
	@Query(value = "SELECT g.distance AS distance, g.hours AS hours, g.minutes AS minutes FROM GeoPlaceToDepartmentTravelInfoJpaEntity g JOIN g.geoPlace p JOIN g.department d WHERE p.id = ?1 AND d.id = ?2")
	Optional<JourneyTravelInfo> findByGeoPlace_IdAndDepartment_Id(Long geoPlaceId, Long departmentId);
}
