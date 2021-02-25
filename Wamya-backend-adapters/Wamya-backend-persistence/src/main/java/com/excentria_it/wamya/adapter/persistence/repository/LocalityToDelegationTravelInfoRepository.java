package com.excentria_it.wamya.adapter.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.excentria_it.wamya.adapter.persistence.entity.LocalityToDelegationTravelInfoJpaEntity;
import com.excentria_it.wamya.domain.JourneyTravelInfo;

public interface LocalityToDelegationTravelInfoRepository
		extends JpaRepository<LocalityToDelegationTravelInfoJpaEntity, Long> {

	Optional<JourneyTravelInfo> findByLocality_IdAndDelegation_Id(Long departurePlaceId, Long arrivalPlaceId);

}
