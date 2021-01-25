package com.excentria_it.wamya.adapter.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.excentria_it.wamya.adapter.persistence.entity.DepartmentJpaEntity;
import com.excentria_it.wamya.domain.AutoCompleteDepartmentsDto;

public interface DepartmentRepository extends JpaRepository<DepartmentJpaEntity, Long> {

	@Query(value = "SELECT new com.excentria_it.wamya.domain.AutoCompleteDepartmentsDto(d.id, VALUE(dl).name, VALUE(cl).name) FROM DepartmentJpaEntity d JOIN d.localizations dl JOIN d.country c JOIN c.localizations cl WHERE c.id = ?1 AND UPPER(d.possibleNames) LIKE UPPER(CONCAT('%', ?2,'%')) AND KEY(dl) = ?3 AND KEY(cl) = ?3")
	List<AutoCompleteDepartmentsDto> findByCountry_IdAndNameLikeIgnoringCase(Long countryId, String input,
			String locale);

}
