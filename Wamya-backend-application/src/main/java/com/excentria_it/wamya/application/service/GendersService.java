package com.excentria_it.wamya.application.service;

import java.util.List;

import javax.transaction.Transactional;

import com.excentria_it.wamya.application.port.in.LoadGendersUseCase;
import com.excentria_it.wamya.application.port.out.LoadGendersPort;
import com.excentria_it.wamya.common.annotation.UseCase;
import com.excentria_it.wamya.domain.LoadGendersDto;

import lombok.RequiredArgsConstructor;

@UseCase
@Transactional
@RequiredArgsConstructor
public class GendersService implements LoadGendersUseCase {

	private final LoadGendersPort loadGendersPort;

	@Override
	public List<LoadGendersDto> loadAllGenders(String locale) {
		return loadGendersPort.loadAllGenders(locale);

	}

}
