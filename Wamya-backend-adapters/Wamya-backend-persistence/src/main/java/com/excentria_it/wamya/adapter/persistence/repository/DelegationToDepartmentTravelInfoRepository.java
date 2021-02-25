package com.excentria_it.wamya.adapter.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.excentria_it.wamya.adapter.persistence.entity.DelegationToDepartmentTravelInfoJpaEntity;
import com.excentria_it.wamya.domain.JourneyTravelInfo;

public interface DelegationToDepartmentTravelInfoRepository
		extends JpaRepository<DelegationToDepartmentTravelInfoJpaEntity, Long> {

	Optional<JourneyTravelInfo> findByDelegation_IdAndDepartment_Id(Long departurePlaceId, Long arrivalPlaceId);

}
