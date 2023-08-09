package com.excentria_it.wamya.application.port.out;

import java.util.Optional;

import com.excentria_it.wamya.domain.DepartmentDto;

public interface LoadDepartmentPort {

	Optional<DepartmentDto> loadDepartmentByName(String departmentName, String locale);

}
