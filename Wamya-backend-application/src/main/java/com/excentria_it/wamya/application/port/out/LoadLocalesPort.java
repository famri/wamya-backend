package com.excentria_it.wamya.application.port.out;

import java.util.List;

import com.excentria_it.wamya.domain.LoadLocalesDto;

public interface LoadLocalesPort {

	List<LoadLocalesDto> loadAllLocales();

}
