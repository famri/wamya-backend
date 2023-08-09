package com.excentria_it.wamya.application.port.out;

import java.util.List;

import com.excentria_it.wamya.domain.AutoCompleteDepartmentDto;

public interface SearchDepartmentPort {

	List<AutoCompleteDepartmentDto> searchDepartmentByName(String input, Long countryId, Integer limit,
			String locale);

}
