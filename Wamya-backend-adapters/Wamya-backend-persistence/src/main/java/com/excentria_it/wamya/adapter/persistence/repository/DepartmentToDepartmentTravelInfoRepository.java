package com.excentria_it.wamya.adapter.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.excentria_it.wamya.adapter.persistence.entity.DepartmentToDepartmentTravelInfoJpaEntity;
import com.excentria_it.wamya.domain.JourneyTravelInfo;

public interface DepartmentToDepartmentTravelInfoRepository
		extends JpaRepository<DepartmentToDepartmentTravelInfoJpaEntity, Long> {

	@Query(value = "SELECT d.distance AS distance, d.hours AS hours, d.minutes AS minutes FROM DepartmentToDepartmentTravelInfoJpaEntity d JOIN d.departmentOne d1 JOIN d.departmentTwo d2 WHERE (d1.id = ?1 AND d2.id = ?2) OR (d1.id = ?2 AND d2.id = ?1)")
	Optional<JourneyTravelInfo> findByDepartmentOne_IdAndDepartmentTwo_Id(
			Long departmentOneId, Long departmentTwoId);

}
