package com.excentria_it.wamya.adapter.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.excentria_it.wamya.adapter.persistence.entity.CountryJpaEntity;
import com.excentria_it.wamya.domain.CountryDto;

public interface CountryRepository extends JpaRepository<CountryJpaEntity, Long> {
	@Query(value = "SELECT new com.excentria_it.wamya.domain.CountryDto(c.id, VALUE(l).name) FROM CountryJpaEntity c JOIN c.localizations l WHERE c.code = ?1 AND KEY(l) = ?2")
	Optional<CountryDto> findByCodeAndLocale(String countryCode, String locale);

	Optional<CountryJpaEntity> findByCode(String countryCode);

	@Query(value = "SELECT c FROM CountryJpaEntity c JOIN FETCH c.localizations l JOIN FETCH c.icc icc JOIN FETCH c.timeZones tz WHERE KEY(l) = ?1 ORDER BY VALUE(l).name ASC")
	List<CountryJpaEntity> findAllByLocale(String locale);
}
