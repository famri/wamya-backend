package com.excentria_it.wamya.adapter.persistence.adapter;

import java.util.Optional;

import com.excentria_it.wamya.adapter.persistence.repository.DepartmentRepository;
import com.excentria_it.wamya.application.port.out.LoadDepartmentPort;
import com.excentria_it.wamya.common.annotation.PersistenceAdapter;
import com.excentria_it.wamya.domain.DepartmentDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@PersistenceAdapter
public class DepartmentPersistenceAdapter implements LoadDepartmentPort {

	private final DepartmentRepository departmentRepository;

	@Override
	public Optional<DepartmentDto> loadDepartmentByName(String departmentName, String locale) {
		return departmentRepository.findByNameAndLocale(departmentName, locale);
	}

}
