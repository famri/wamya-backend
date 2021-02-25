package com.excentria_it.wamya.adapter.web.adapter;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.validation.constraints.Min;
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

import com.excentria_it.wamya.application.port.in.AutoCompletePlaceForClientUseCase;
import com.excentria_it.wamya.common.annotation.WebAdapter;
import com.excentria_it.wamya.common.utils.LocaleUtils;
import com.excentria_it.wamya.domain.AutoCompletePlaceDto;
import com.excentria_it.wamya.domain.AutoCompletePlaceResult;

import lombok.RequiredArgsConstructor;

@WebAdapter
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Validated
@RequestMapping(path = "/places", produces = MediaType.APPLICATION_JSON_VALUE)
public class AutoCompletePlaceController {

	private final AutoCompletePlaceForClientUseCase autoCompletePlaceForClientUseCase;

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public AutoCompletePlaceResult autoCompletePlace(@NotEmpty @RequestParam(name = "country") String countryCode,
			@Size(min = 3) @RequestParam(name = "input") String input,
			@RequestParam(name = "limit", defaultValue = "5") @Min(value = 1) Integer limit, Locale locale) {

		Locale supportedLocale = LocaleUtils.getSupporedLocale(locale);

		List<AutoCompletePlaceDto> autoCompletePlaceDtos = autoCompletePlaceForClientUseCase.autoCompletePlace(input,
				countryCode, limit, supportedLocale.toString());

		if (autoCompletePlaceDtos == null || autoCompletePlaceDtos.isEmpty()) {
			return new AutoCompletePlaceResult(0, Collections.<AutoCompletePlaceDto>emptyList());
		}
		return new AutoCompletePlaceResult(autoCompletePlaceDtos.size(), autoCompletePlaceDtos);

	}
}
