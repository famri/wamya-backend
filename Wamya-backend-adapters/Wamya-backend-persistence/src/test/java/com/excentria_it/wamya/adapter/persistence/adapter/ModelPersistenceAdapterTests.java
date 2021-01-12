package com.excentria_it.wamya.adapter.persistence.adapter;

import static com.excentria_it.wamya.test.data.common.ModelTestData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import com.excentria_it.wamya.adapter.persistence.repository.ModelRepository;
import com.excentria_it.wamya.domain.LoadModelsDto;

@ExtendWith(MockitoExtension.class)
public class ModelPersistenceAdapterTests {

	@Mock
	private ModelRepository modelRepository;
	@InjectMocks
	private ModelPersistenceAdapter modelPersistenceAdapter;

	@Test
	void testLoadByConstructorId() {
		// given
		List<LoadModelsDto> loadModelsDtos = defaultLoadModelsDtos();
		given(modelRepository.findByConstructor_Id(any(Long.class), any(Sort.class))).willReturn(loadModelsDtos);
		// when
		List<LoadModelsDto> loadModelsDtosResult = modelPersistenceAdapter.loadByConstructorId(1L);
		// then
		then(modelRepository).should(times(1)).findByConstructor_Id(eq(1L), any(Sort.class));

		assertEquals(loadModelsDtos, loadModelsDtosResult);

	}

}
