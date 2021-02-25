package com.excentria_it.wamya.adapter.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.excentria_it.wamya.adapter.persistence.entity.DepartmentJpaEntity;
import com.excentria_it.wamya.domain.AutoCompleteDepartmentDto;
import com.excentria_it.wamya.domain.DepartmentDto;

public interface DepartmentRepository extends JpaRepository<DepartmentJpaEntity, Long> {

	@Query(value = "SELECT new com.excentria_it.wamya.domain.AutoCompleteDepartmentDto(dp.id, VALUE(dpl).name, VALUE(cl).name) FROM DepartmentJpaEntity dp JOIN dp.localizations dpl JOIN dp.country c JOIN c.localizations cl WHERE c.id = ?1 AND UPPER(dp.possibleNames) LIKE UPPER(CONCAT('%', ?2,'%')) AND KEY(dpl) = ?3 AND KEY(cl) = ?3")
	List<AutoCompleteDepartmentDto> findByCountry_IdAndNameLikeIgnoringCase(Long countryId, String departmentName,
			String locale, Pageable pageable);

	@Query(value = "SELECT new com.excentria_it.wamya.domain.DepartmentDto(d.id, VALUE(dl).name) FROM DepartmentJpaEntity d JOIN d.localizations dl WHERE KEY(dl) = ?2 AND UPPER(VALUE(dl).name) = UPPER(?1)")
	Optional<DepartmentDto> findByNameAndLocale(String name, String locale);
}
