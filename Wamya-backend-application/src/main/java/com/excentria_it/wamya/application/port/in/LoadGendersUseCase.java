package com.excentria_it.wamya.application.port.in;

import java.util.List;

import com.excentria_it.wamya.domain.LoadGendersDto;

public interface LoadGendersUseCase {

	List<LoadGendersDto> loadAllGenders(String locale);

}
