package com.excentria_it.wamya.application.service;

import static com.excentria_it.wamya.test.data.common.ConstructorTestData.*;
import static com.excentria_it.wamya.test.data.common.ModelTestData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.excentria_it.wamya.application.port.out.LoadConstructorPort;
import com.excentria_it.wamya.application.port.out.LoadModelPort;
import com.excentria_it.wamya.domain.LoadConstructorsDto;
import com.excentria_it.wamya.domain.LoadModelsDto;

@ExtendWith(MockitoExtension.class)
public class ConstructorServiceTests {
	@Mock
	private LoadConstructorPort loadConstructorPort;

	@Mock
	private LoadModelPort loadModelPort;

	@InjectMocks
	private ConstructorService constructorService;

	@Test
	void testLoadAllConstructors() {
		// given

		List<LoadConstructorsDto> loadConstructorsDtos = defaultLoadConstructorsDtos();
		given(loadConstructorPort.loadAllConstructors()).willReturn(loadConstructorsDtos);
		// when
		List<LoadConstructorsDto> loadConstructorsDtosResult = constructorService.loadAllConstructors();
		
		// then

		assertEquals(loadConstructorsDtos, loadConstructorsDtosResult);
	}

	@Test
	void testLoadConstructorModels() {
		// given

		List<LoadModelsDto> loadModelsDtos = defaultLoadModelsDtos();
		given(loadModelPort.loadByConstructorId(any(Long.class))).willReturn(loadModelsDtos);
		// when
		List<LoadModelsDto> loadModelsDtosResult = constructorService.loadConstructorModels(1L);
		
		// then
		assertEquals(loadModelsDtos, loadModelsDtosResult);
		
	}

}
