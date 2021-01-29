package com.excentria_it.wamya.adapter.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.excentria_it.wamya.adapter.persistence.entity.LocalityJpaEntity;
import com.excentria_it.wamya.domain.AutoCompleteLocalitiesDto;

public interface LocalityRepository extends JpaRepository<LocalityJpaEntity, Long> {
	@Query(value = "SELECT new com.excentria_it.wamya.domain.AutoCompleteLocalitiesDto(l.id, VALUE(ll).name, VALUE(dll).name, VALUE(dpl).name, VALUE(cl).name) FROM LocalityJpaEntity l JOIN l.localizations ll JOIN l.delegation dl JOIN dl.localizations dll JOIN dl.department d JOIN d.localizations dpl JOIN d.country c JOIN c.localizations cl WHERE c.id = ?1 AND (UPPER(d.possibleNames) LIKE UPPER(CONCAT('%', ?2,'%')) OR UPPER(dl.possibleNames) LIKE UPPER(CONCAT('%', ?2,'%')) OR UPPER(l.possibleNames) LIKE UPPER(CONCAT('%', ?2,'%'))) AND KEY(ll) = ?3 AND KEY(dll) = ?3 AND KEY(dpl) = ?3 AND KEY(cl) = ?3")
	List<AutoCompleteLocalitiesDto> findByCountry_IdAndNameLikeIgnoringCase(Long countryId, String input,
			String locale);

}
