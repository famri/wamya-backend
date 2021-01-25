package com.excentria_it.wamya.application.port.out;

import java.util.List;

import com.excentria_it.wamya.domain.AutoCompleteDepartmentsDto;

public interface SearchDepartmentPort {

	List<AutoCompleteDepartmentsDto> searchDepartment(String input, Long countryId, String language);
	
}
