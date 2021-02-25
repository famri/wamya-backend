package com.excentria_it.wamya.adapter.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.excentria_it.wamya.adapter.persistence.entity.LocalityToLocalityTravelInfoJpaEntity;
import com.excentria_it.wamya.domain.JourneyTravelInfo;

public interface LocalityToLocalityTravelInfoRepository
		extends JpaRepository<LocalityToLocalityTravelInfoJpaEntity, Long> {

	@Query(value = "SELECT l.distance AS distance, l.hours AS hours, l.minutes AS minutes FROM LocalityToLocalityTravelInfoJpaEntity l JOIN l.localityOne l1 JOIN l.localityTwo l2 WHERE (l1.id = ?1 AND l2.id = ?2) OR (l1.id = ?2 AND l2.id = ?1)")
	Optional<JourneyTravelInfo> findByLocalityOne_IdAndLocalityTwo_Id(
			Long localityOneId, Long localityTwoId);

}
