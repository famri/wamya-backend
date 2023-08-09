package com.excentria_it.wamya.application.port.in;

import java.util.List;

import com.excentria_it.wamya.domain.AutoCompletePlaceDto;

public interface AutoCompletePlaceForTransporterUseCase {

	List<AutoCompletePlaceDto> autoCompleteDepartment(String input, String countryCode, Integer limit, String locale);

}
