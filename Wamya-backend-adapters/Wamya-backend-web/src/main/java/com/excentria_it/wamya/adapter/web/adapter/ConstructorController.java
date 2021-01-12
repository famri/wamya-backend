package com.excentria_it.wamya.adapter.web.adapter;

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
import com.excentria_it.wamya.domain.LoadConstructorsDto;
import com.excentria_it.wamya.domain.LoadModelsDto;

import lombok.RequiredArgsConstructor;

@WebAdapter
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping(path = "/constructors", produces = MediaType.APPLICATION_JSON_VALUE)
public class ConstructorController {

	private final LoadConstructorsUseCase loadConstructorsUseCase;

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<LoadConstructorsDto> loadAllConstructors() {

		return loadConstructorsUseCase.loadAllConstructors();

	}

	@GetMapping(path = "/{constructorId}/models")
	@ResponseStatus(HttpStatus.OK)
	public List<LoadModelsDto> loadConstructorModels(@PathVariable("constructorId") Long constructorId) {

		return loadConstructorsUseCase.loadConstructorModels(constructorId);

	}
}
