package com.excentria_it.wamya.adapter.web.adapter;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.excentria_it.wamya.application.port.in.LoadCountriesUseCase;
import com.excentria_it.wamya.common.annotation.WebAdapter;
import com.excentria_it.wamya.common.utils.LocaleUtils;
import com.excentria_it.wamya.domain.LoadCountriesDto;
import com.excentria_it.wamya.domain.LoadCountriesResult;

import lombok.RequiredArgsConstructor;

@WebAdapter
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping(path = "/countries", produces = MediaType.APPLICATION_JSON_VALUE)
public class CountriesController {

	private final LoadCountriesUseCase loadCountriesUseCase;

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public LoadCountriesResult loadAllCountries(Locale locale) {

		Locale supportedLocale = LocaleUtils.getSupporedLocale(locale);

		List<LoadCountriesDto> loadCountriesDtos = loadCountriesUseCase.loadAllCountries(supportedLocale.toString());

		if (loadCountriesDtos == null || loadCountriesDtos.isEmpty()) {
			return new LoadCountriesResult(0, Collections.emptyList());
		}
		return new LoadCountriesResult(loadCountriesDtos.size(), loadCountriesDtos);

	}
}
