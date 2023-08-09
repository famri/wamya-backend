package com.excentria_it.wamya.application.port.out;

import java.util.List;

import com.excentria_it.wamya.domain.LoadGendersDto;

public interface LoadGendersPort {

	List<LoadGendersDto> loadAllGenders(String locale);

}
