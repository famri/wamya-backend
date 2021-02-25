package com.excentria_it.wamya.adapter.persistence.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.excentria_it.wamya.adapter.persistence.entity.DelegationJpaEntity;
import com.excentria_it.wamya.domain.AutoCompleteDelegationDto;

public interface DelegationRepository extends JpaRepository<DelegationJpaEntity, Long> {

	@Query(value = "SELECT new com.excentria_it.wamya.domain.AutoCompleteDelegationDto(dl.id, VALUE(dll).name, VALUE(dpl).name, VALUE(cl).name) FROM DelegationJpaEntity dl JOIN dl.localizations dll JOIN dl.department dp JOIN dp.localizations dpl JOIN dp.country c JOIN c.localizations cl WHERE c.id = ?1 AND UPPER(dl.possibleNames) LIKE UPPER(CONCAT('%', ?2,'%')) AND KEY(dll) = ?3 AND KEY(dpl) = ?3 AND KEY(cl) = ?3")
	List<AutoCompleteDelegationDto> findByCountry_IdAndNameLikeIgnoringCase(Long countryId, String delegationName,
			String locale, Pageable page);

	@Query(value = "SELECT new com.excentria_it.wamya.domain.AutoCompleteDelegationDto(dl.id, VALUE(dll).name, VALUE(dpl).name, VALUE(cl).name) FROM DelegationJpaEntity dl JOIN dl.localizations dll JOIN dl.department dp JOIN dp.localizations dpl JOIN dp.country c JOIN c.localizations cl WHERE c.id = ?1 AND UPPER(dl.possibleNames) LIKE UPPER(CONCAT('%', ?3,'%')) AND UPPER(dp.possibleNames) LIKE UPPER(CONCAT('%', ?2,'%')) AND KEY(dll) = ?4 AND KEY(dpl) = ?4 AND KEY(cl) = ?4")
	List<AutoCompleteDelegationDto> findByCountry_IdAndDepartmentNameLikeIgnoringCaseAndNameLikeIgnoringCase(
			Long countryId, String departmentName, String delegationName, String locale, Pageable page);

}
