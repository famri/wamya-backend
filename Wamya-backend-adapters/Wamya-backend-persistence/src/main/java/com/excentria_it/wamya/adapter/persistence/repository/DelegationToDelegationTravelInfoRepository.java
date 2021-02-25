package com.excentria_it.wamya.adapter.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.excentria_it.wamya.adapter.persistence.entity.DelegationToDelegationTravelInfoJpaEntity;
import com.excentria_it.wamya.domain.JourneyTravelInfo;

public interface DelegationToDelegationTravelInfoRepository
		extends JpaRepository<DelegationToDelegationTravelInfoJpaEntity, Long> {

	@Query(value = "SELECT d.distance AS distance, d.hours AS hours, d.minutes AS minutes FROM DelegationToDelegationTravelInfoJpaEntity d JOIN d.delegationOne d1 JOIN d.delegationTwo d2 WHERE (d1.id = ?1 AND d2.id = ?2) OR (d1.id = ?2 AND d2.id = ?1)")
	Optional<JourneyTravelInfo> findByDelegationOne_IdAndDelegationTwo_Id(
			Long delegationOneId, Long delegationTwoId);

}
