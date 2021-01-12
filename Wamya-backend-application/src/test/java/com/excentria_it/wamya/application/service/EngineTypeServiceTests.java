package com.excentria_it.wamya.application.service;

import static com.excentria_it.wamya.test.data.common.EngineTypeTestData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.excentria_it.wamya.application.port.out.LoadEngineTypePort;
import com.excentria_it.wamya.domain.LoadEngineTypesDto;

@ExtendWith(MockitoExtension.class)
public class EngineTypeServiceTests {
	@Mock
	private LoadEngineTypePort loadEngineTypePort;
	@InjectMocks
	private EngineTypeService engineTypeService;

	@Test
	void testLoadAllEngineTypes() {
		// given

		List<LoadEngineTypesDto> engineTypesDtos = defaultLoadEngineTypesDtos();
		given(loadEngineTypePort.loadAllEngineTypes(any(String.class))).willReturn(engineTypesDtos);
		// when
		List<LoadEngineTypesDto> engineTypesDtosResult = engineTypeService.loadAllEngineTypes("en_US");
		// then
		assertEquals(engineTypesDtos, engineTypesDtosResult);
	}
}
