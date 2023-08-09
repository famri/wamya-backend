package com.excentria_it.wamya.application.port.in;

import java.util.List;

import com.excentria_it.wamya.domain.LoadLocalesDto;

public interface LoadLocalesUseCase {
	List<LoadLocalesDto> loadAllLocales();

}
