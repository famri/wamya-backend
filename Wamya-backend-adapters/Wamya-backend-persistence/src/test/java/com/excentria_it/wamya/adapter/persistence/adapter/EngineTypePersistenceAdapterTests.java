package com.excentria_it.wamya.adapter.persistence.adapter;

import static com.excentria_it.wamya.test.data.common.EngineTypeTestData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import com.excentria_it.wamya.adapter.persistence.repository.EngineTypeRepository;
import com.excentria_it.wamya.domain.EngineTypeDto;
import com.excentria_it.wamya.domain.LoadEngineTypesDto;

@ExtendWith(MockitoExtension.class)
public class EngineTypePersistenceAdapterTests {
	@Mock
	private EngineTypeRepository engineTypeRepository;
	@InjectMocks
	private EngineTypePersistenceAdapter engineTypePersistenceAdapter;

	@Test
	void testLoadEngineTypeById() {
		// given
		EngineTypeDto engineTypeDto = defaultEngineTypeDto();
		given(engineTypeRepository.findByIdAndLocale(any(Long.class), any(String.class)))
				.willReturn(Optional.of(engineTypeDto));

		// when
		Optional<EngineTypeDto> engineTypeDtoOptionalResult = engineTypePersistenceAdapter.loadEngineTypeById(1L,
				"en_US");

		// then
		then(engineTypeRepository).should(times(1)).findByIdAndLocale(eq(1L), eq("en_US"));
		assertEquals(engineTypeDto.getId(), engineTypeDtoOptionalResult.get().getId());
		assertEquals(engineTypeDto.getCode(), engineTypeDtoOptionalResult.get().getCode());
		assertEquals(engineTypeDto.getDescription(), engineTypeDtoOptionalResult.get().getDescription());
		assertEquals(engineTypeDto.getName(), engineTypeDtoOptionalResult.get().getName());

	}

	@Test
	void testLoadAllEngineTypes() {
		// given
		List<LoadEngineTypesDto> engineTypeDtos = defaultLoadEngineTypesDtos();
		given(engineTypeRepository.findAllByLocale(any(String.class), any(Sort.class))).willReturn(engineTypeDtos);

		// when
		List<LoadEngineTypesDto> engineTypeDtosResult = engineTypePersistenceAdapter.loadAllEngineTypes("en_US");

		// then
		then(engineTypeRepository).should(times(1)).findAllByLocale(eq("en_US"), any(Sort.class));
		assertEquals(engineTypeDtos, engineTypeDtosResult);

	}
}
