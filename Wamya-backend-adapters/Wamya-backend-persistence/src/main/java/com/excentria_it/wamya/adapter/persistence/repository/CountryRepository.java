package com.excentria_it.wamya.adapter.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.excentria_it.wamya.adapter.persistence.entity.CountryJpaEntity;
import com.excentria_it.wamya.domain.CountryDto;

public interface CountryRepository extends JpaRepository<CountryJpaEntity, Long> {
	@Query(value = "SELECT new com.excentria_it.wamya.domain.CountryDto(c.id, VALUE(l).name) FROM CountryJpaEntity c JOIN c.localizations l WHERE c.code = ?1 AND KEY(l) = ?2")
	Optional<CountryDto> findByCodeAndLocale(String countryCode, String locale);
}
