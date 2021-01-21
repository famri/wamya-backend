package com.excentria_it.wamya.adapter.web.adapter;

import java.util.Collections;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.excentria_it.wamya.application.port.in.LoadConstructorsUseCase;
import com.excentria_it.wamya.common.annotation.WebAdapter;
import com.excentria_it.wamya.domain.LoadConstructorModelsResult;
import com.excentria_it.wamya.domain.LoadConstructorsDto;
import com.excentria_it.wamya.domain.LoadConstructorsResult;
import com.excentria_it.wamya.domain.LoadModelsDto;

import lombok.RequiredArgsConstructor;

@WebAdapter
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping(path = "/constructors", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ConstructorController {

	private final LoadConstructorsUseCase loadConstructorsUseCase;

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public LoadConstructorsResult loadAllConstructors() {

		List<LoadConstructorsDto> loadConstructorsDtos = loadConstructorsUseCase.loadAllConstructors();
		if (loadConstructorsDtos == null || loadConstructorsDtos.isEmpty()) {
			return new LoadConstructorsResult(0, Collections.<LoadConstructorsDto>emptyList());
		}
		return new LoadConstructorsResult(loadConstructorsDtos.size(), loadConstructorsDtos);
	}

	@GetMapping(path = "/{constructorId}/models")
	@ResponseStatus(HttpStatus.OK)
	public LoadConstructorModelsResult loadConstructorModels(@PathVariable("constructorId") Long constructorId) {

		List<LoadModelsDto> loadModelsDtos = loadConstructorsUseCase.loadConstructorModels(constructorId);
		if (loadModelsDtos == null || loadModelsDtos.isEmpty()) {
			return new LoadConstructorModelsResult(0, Collections.<LoadModelsDto>emptyList());
		}
		return new LoadConstructorModelsResult(loadModelsDtos.size(), loadModelsDtos);
	}
}
