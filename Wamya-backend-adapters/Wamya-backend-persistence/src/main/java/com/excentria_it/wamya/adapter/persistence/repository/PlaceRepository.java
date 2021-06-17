package com.excentria_it.wamya.adapter.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.excentria_it.wamya.adapter.persistence.entity.PlaceId;
import com.excentria_it.wamya.adapter.persistence.entity.PlaceJpaEntity;
import com.excentria_it.wamya.domain.PlaceType;

public interface PlaceRepository extends JpaRepository<PlaceJpaEntity, PlaceId> {
	@Query(value = "SELECT VALUE(l).name FROM PlaceJpaEntity p JOIN p.localizations l WHERE p.placeId.id= ?1 AND p.placeId.type = ?2 AND KEY(l) = ?3")
	String findNameByLocale(Long placeId, PlaceType placeType, String locale);

}
