package com.excentria_it.wamya.adapter.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.excentria_it.wamya.adapter.persistence.entity.GenderJpaEntity;
import com.excentria_it.wamya.domain.LoadGendersDto;

public interface GenderRepository extends JpaRepository<GenderJpaEntity, Long> {

	@Query(value = "SELECT g.id AS id, VALUE(l).name AS name FROM GenderJpaEntity g JOIN g.localizations l WHERE g.id = ?1 AND KEY(l) = ?2")
	Optional<LoadGendersDto> findByIdAndLocale(Long genderId, String locale);

	@Query(value = "SELECT new com.excentria_it.wamya.domain.LoadGendersDto(g.id, VALUE(l).name) FROM GenderJpaEntity g JOIN g.localizations l WHERE KEY(l) = ?1")
	List<LoadGendersDto> findAllByLocale(String locale, Sort sort);

}
