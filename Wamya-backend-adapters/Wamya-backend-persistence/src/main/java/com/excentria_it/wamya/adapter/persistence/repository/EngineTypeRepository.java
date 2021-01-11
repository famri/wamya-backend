package com.excentria_it.wamya.adapter.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.excentria_it.wamya.adapter.persistence.entity.EngineTypeJpaEntity;
import com.excentria_it.wamya.domain.EngineTypeDto;

public interface EngineTypeRepository extends JpaRepository<EngineTypeJpaEntity, Long> {

	@Query(value = "SELECT e.id AS id, e.code AS code, VALUE(l).name AS name,  VALUE(l).description AS description FROM EngineTypeJpaEntity e JOIN e.localizations l WHERE e.id = ?1 AND KEY(l) = ?2")
	Optional<EngineTypeDto> findByIdAndLocale(Long engineTypeId, String locale);

}
