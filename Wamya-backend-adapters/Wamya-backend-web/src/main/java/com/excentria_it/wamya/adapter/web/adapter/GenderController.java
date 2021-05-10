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

import com.excentria_it.wamya.application.port.in.LoadGendersUseCase;
import com.excentria_it.wamya.common.annotation.WebAdapter;
import com.excentria_it.wamya.common.utils.LocaleUtils;
import com.excentria_it.wamya.domain.LoadGendersDto;
import com.excentria_it.wamya.domain.LoadGendersResult;

import lombok.RequiredArgsConstructor;


@WebAdapter
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping(path = "/genders", produces = MediaType.APPLICATION_JSON_VALUE)

public class GenderController {

	private final LoadGendersUseCase loadGendersUseCase;

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public LoadGendersResult loadAllGenders(Locale locale) {

		Locale supportedLocale = LocaleUtils.getSupporedLocale(locale);

		List<LoadGendersDto> loadGendersDtos = loadGendersUseCase
				.loadAllGenders(supportedLocale.toString());

		if (loadGendersDtos == null || loadGendersDtos.isEmpty()) {
			return new LoadGendersResult(0, Collections.emptyList());
		}
		return new LoadGendersResult(loadGendersDtos.size(), loadGendersDtos);

	}
}