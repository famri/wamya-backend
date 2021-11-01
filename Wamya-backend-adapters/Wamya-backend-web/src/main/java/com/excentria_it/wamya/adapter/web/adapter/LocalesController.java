package com.excentria_it.wamya.adapter.web.adapter;

import java.util.Collections;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.excentria_it.wamya.application.port.in.LoadLocalesUseCase;
import com.excentria_it.wamya.common.annotation.WebAdapter;
import com.excentria_it.wamya.domain.LoadLocalesDto;
import com.excentria_it.wamya.domain.LoadLocalesResult;

import lombok.RequiredArgsConstructor;

@WebAdapter
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/locales", produces = MediaType.APPLICATION_JSON_VALUE)
public class LocalesController {
	private final LoadLocalesUseCase loadLocalesUseCase;

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public LoadLocalesResult loadAllLocales() {

		List<LoadLocalesDto> loadLocalesDtos = loadLocalesUseCase.loadAllLocales();

		if (loadLocalesDtos == null || loadLocalesDtos.isEmpty()) {
			return new LoadLocalesResult(0, Collections.emptyList());
		}
		return new LoadLocalesResult(loadLocalesDtos.size(), loadLocalesDtos);
	}
}
