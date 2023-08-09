package com.excentria_it.wamya.adapter.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.excentria_it.wamya.adapter.persistence.entity.LocalityToDepartmentTravelInfoJpaEntity;
import com.excentria_it.wamya.domain.JourneyTravelInfo;

public interface LocalityToDepartmentTravelInfoRepository
		extends JpaRepository<LocalityToDepartmentTravelInfoJpaEntity, Long> {

	Optional<JourneyTravelInfo> findByLocality_IdAndDepartment_Id(Long localityId, Long departmentId);

}
