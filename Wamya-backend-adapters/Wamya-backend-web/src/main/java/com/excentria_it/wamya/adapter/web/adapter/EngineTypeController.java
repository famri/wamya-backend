package com.excentria_it.wamya.adapter.web.adapter;

import java.util.List;
import java.util.Locale;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.excentria_it.wamya.application.port.in.LoadEngineTypesUseCase;
import com.excentria_it.wamya.common.annotation.WebAdapter;
import com.excentria_it.wamya.common.utils.LocaleUtils;
import com.excentria_it.wamya.domain.LoadEngineTypesDto;

import lombok.RequiredArgsConstructor;

@WebAdapter
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping(path = "/engine-types", produces = MediaType.APPLICATION_JSON_VALUE)
public class EngineTypeController {

	private final LoadEngineTypesUseCase loadEngineTypesUseCase;

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<LoadEngineTypesDto> loadAllEngineTypes(Locale locale) {

		Locale supportedLocale = LocaleUtils.getSupporedLocale(locale);

		return loadEngineTypesUseCase.loadAllEngineTypes(supportedLocale.toString());

	}
}
