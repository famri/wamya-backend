package com.excentria_it.wamya.application.service;

import java.util.List;

import javax.transaction.Transactional;

import com.excentria_it.wamya.application.port.in.LoadLocalesUseCase;
import com.excentria_it.wamya.application.port.out.LoadLocalesPort;
import com.excentria_it.wamya.common.annotation.UseCase;
import com.excentria_it.wamya.domain.LoadLocalesDto;

import lombok.RequiredArgsConstructor;

@UseCase
@Transactional
@RequiredArgsConstructor
public class LocalesService implements LoadLocalesUseCase {
	private final LoadLocalesPort loadLocalesPort;

	@Override
	public List<LoadLocalesDto> loadAllLocales() {
		return loadLocalesPort.loadAllLocales();
	}

}
