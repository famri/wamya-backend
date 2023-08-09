package com.excentria_it.wamya.adapter.persistence.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.excentria_it.wamya.adapter.persistence.entity.LocalityJpaEntity;
import com.excentria_it.wamya.domain.AutoCompleteLocalityDto;

public interface LocalityRepository extends JpaRepository<LocalityJpaEntity, Long> {
	@Query(value = "SELECT new com.excentria_it.wamya.domain.AutoCompleteLocalityDto(l.id, VALUE(ll).name, VALUE(dll).name, VALUE(dpl).name, VALUE(cl).name) FROM LocalityJpaEntity l JOIN l.localizations ll JOIN l.delegation dl JOIN dl.localizations dll JOIN dl.department dp JOIN dp.localizations dpl JOIN dp.country c JOIN c.localizations cl WHERE c.id = ?1 AND UPPER(l.possibleNames) LIKE UPPER(CONCAT('%', ?2,'%')) AND KEY(ll) = ?3 AND KEY(dll) = ?3 AND KEY(dpl) = ?3 AND KEY(cl) = ?3")
	List<AutoCompleteLocalityDto> findByCountry_IdAndNameLikeIgnoringCase(Long countryId, String input, String locale,
			Pageable pageable);

	@Query(value = "SELECT new com.excentria_it.wamya.domain.AutoCompleteLocalityDto(l.id, VALUE(ll).name, VALUE(dll).name, VALUE(dpl).name, VALUE(cl).name) FROM LocalityJpaEntity l JOIN l.localizations ll JOIN l.delegation dl JOIN dl.localizations dll JOIN dl.department dp JOIN dp.localizations dpl JOIN dp.country c JOIN c.localizations cl WHERE c.id = ?1 AND UPPER(l.possibleNames) LIKE UPPER(CONCAT('%', ?3,'%')) AND UPPER(dl.possibleNames) LIKE UPPER(CONCAT('%', ?2,'%')) AND KEY(ll) = ?4 AND KEY(dll) = ?4 AND KEY(dpl) = ?4 AND KEY(cl) = ?4")
	List<AutoCompleteLocalityDto> findByCountry_IdAndDelegationNameLikeIgnoringCaseAndNameLikeIgnoringCase(
			Long countryId, String delegationName, String localityName, String locale, Pageable page);

	@Query(value = "SELECT new com.excentria_it.wamya.domain.AutoCompleteLocalityDto(l.id, VALUE(ll).name, VALUE(dll).name, VALUE(dpl).name, VALUE(cl).name) FROM LocalityJpaEntity l JOIN l.localizations ll JOIN l.delegation dl JOIN dl.localizations dll JOIN dl.department dp JOIN dp.localizations dpl JOIN dp.country c JOIN c.localizations cl WHERE c.id = ?1 AND UPPER(l.possibleNames) LIKE UPPER(CONCAT('%', ?3,'%')) AND UPPER(dp.possibleNames) LIKE UPPER(CONCAT('%', ?2,'%')) AND KEY(ll) = ?4 AND KEY(dll) = ?4 AND KEY(dpl) = ?4 AND KEY(cl) = ?4")
	List<AutoCompleteLocalityDto> findByCountry_IdAndDepartmentNameLikeIgnoringCaseAndNameLikeIgnoringCase(
			Long countryId, String departmentName, String localityName, String locale, Pageable page);

	@Query(value = "SELECT new com.excentria_it.wamya.domain.AutoCompleteLocalityDto(l.id, VALUE(ll).name, VALUE(dll).name, VALUE(dpl).name, VALUE(cl).name) FROM LocalityJpaEntity l JOIN l.localizations ll JOIN l.delegation dl JOIN dl.localizations dll JOIN dl.department dp JOIN dp.localizations dpl JOIN dp.country c JOIN c.localizations cl WHERE c.id = ?1 AND UPPER(l.possibleNames) LIKE UPPER(CONCAT('%', ?4,'%')) AND UPPER(dl.possibleNames) LIKE UPPER(CONCAT('%', ?3,'%')) AND UPPER(dp.possibleNames) LIKE UPPER(CONCAT('%', ?2,'%')) AND KEY(ll) = ?5 AND KEY(dll) = ?5 AND KEY(dpl) = ?5 AND KEY(cl) = ?5")
	List<AutoCompleteLocalityDto> findByCountry_IdAndDepartmentNameLikeIgnoringCaseAndDelegationNameLikeIgnoringCaseAndNameLikeIgnoringCase(
			Long countryId, String departmentName, String delegationName, String localityName, String locale,
			Pageable page);

}
