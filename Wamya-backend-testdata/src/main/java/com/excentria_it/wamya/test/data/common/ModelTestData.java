package com.excentria_it.wamya.test.data.common;

import java.util.List;

import com.excentria_it.wamya.domain.LoadModelsDto;

public class ModelTestData {

	public static final List<LoadModelsDto> loadModelsDtos = List.of(new LoadModelsDto(1L, "Model1", 4.35, 1.83, 1.8),
			new LoadModelsDto(2L, "Model2", 4.35, 1.83, 1.8), new LoadModelsDto(3L, "Model3", 4.35, 1.83, 1.8));

	public static List<LoadModelsDto> defaultLoadModelsDtos() {
		return loadModelsDtos;
	}
}
