package com.excentria_it.wamya.adapter.persistence.adapter;

import java.util.List;

import com.excentria_it.wamya.adapter.persistence.repository.DepartmentRepository;
import com.excentria_it.wamya.application.port.out.SearchDepartmentPort;
import com.excentria_it.wamya.common.annotation.PersistenceAdapter;
import com.excentria_it.wamya.domain.AutoCompleteDepartmentsDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@PersistenceAdapter
public class DepartmentPersistenceAdapter implements SearchDepartmentPort {

	private final DepartmentRepository departmentRepository;

	@Override
	public List<AutoCompleteDepartmentsDto> searchDepartment(String input, Long countryId, String locale) {

		return departmentRepository.findByCountry_IdAndNameLikeIgnoringCase(countryId, input, locale);
	}

}
