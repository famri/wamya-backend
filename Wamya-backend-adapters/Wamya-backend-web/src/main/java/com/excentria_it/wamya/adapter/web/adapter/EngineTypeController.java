package com.excentria_it.wamya.adapter.web.adapter;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.excentria_it.wamya.application.port.in.LoadEngineTypesUseCase;
import com.excentria_it.wamya.common.annotation.WebAdapter;
import com.excentria_it.wamya.common.utils.LocaleUtils;
import com.excentria_it.wamya.domain.LoadEngineTypesDto;
import com.excentria_it.wamya.domain.LoadEngineTypesResult;

import lombok.RequiredArgsConstructor;

@WebAdapter
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/engine-types", produces = MediaType.APPLICATION_JSON_VALUE)

public class EngineTypeController {

	private final LoadEngineTypesUseCase loadEngineTypesUseCase;

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public LoadEngineTypesResult loadAllEngineTypes(Locale locale) {

		Locale supportedLocale = LocaleUtils.getSupporedLocale(locale);

		List<LoadEngineTypesDto> loadEngineTypesDtos = loadEngineTypesUseCase
				.loadAllEngineTypes(supportedLocale.toString());

		if (loadEngineTypesDtos == null || loadEngineTypesDtos.isEmpty()) {
			return new LoadEngineTypesResult(0, Collections.emptyList());
		}
		return new LoadEngineTypesResult(loadEngineTypesDtos.size(), loadEngineTypesDtos);

	}
}
