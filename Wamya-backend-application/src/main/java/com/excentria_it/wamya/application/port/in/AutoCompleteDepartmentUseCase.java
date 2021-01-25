package com.excentria_it.wamya.application.port.in;

import java.util.List;

import com.excentria_it.wamya.domain.AutoCompleteDepartmentsDto;

public interface AutoCompleteDepartmentUseCase {

	List<AutoCompleteDepartmentsDto> autoCompleteDepartment(String input, String countryCode, String locale);

}
