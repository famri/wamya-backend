package com.excentria_it.wamya.adapter.web.adapter;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.excentria_it.wamya.application.port.in.AutoCompleteLocalityUseCase;
import com.excentria_it.wamya.common.annotation.WebAdapter;
import com.excentria_it.wamya.common.utils.LocaleUtils;
import com.excentria_it.wamya.domain.AutoCompleteLocalitiesDto;
import com.excentria_it.wamya.domain.AutoCompleteLocalitiesResult;

import lombok.RequiredArgsConstructor;

@WebAdapter
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Validated
@RequestMapping(path = "/localities", produces = MediaType.APPLICATION_JSON_VALUE)
public class AutoCompleteLocalityController {

	private final AutoCompleteLocalityUseCase autoCompleteLocalityUseCase;

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public AutoCompleteLocalitiesResult autoCompleteLocality(
			@NotEmpty @RequestParam(name = "country") String countryCode,
			@Size(min = 3) @RequestParam(name = "input") String input, Locale locale) {

		Locale supportedLocale = LocaleUtils.getSupporedLocale(locale);

		List<AutoCompleteLocalitiesDto> autoCompleteLocalitiesDtos = autoCompleteLocalityUseCase
				.autoCompleteLocality(input, countryCode, supportedLocale.toString());

		if (autoCompleteLocalitiesDtos == null || autoCompleteLocalitiesDtos.isEmpty()) {
			return new AutoCompleteLocalitiesResult(0, Collections.<AutoCompleteLocalitiesDto>emptyList());
		}
		return new AutoCompleteLocalitiesResult(autoCompleteLocalitiesDtos.size(), autoCompleteLocalitiesDtos);

	}
}
