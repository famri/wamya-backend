package com.excentria_it.wamya.adapter.persistence.adapter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.excentria_it.wamya.adapter.persistence.repository.EngineTypeRepository;
import com.excentria_it.wamya.domain.EngineTypeDto;
import static com.excentria_it.wamya.test.data.common.EngineTypeTestData.*;
@ExtendWith(MockitoExtension.class)
public class EngineTypePersistenceAdapterTests {
	@Mock
	private EngineTypeRepository engineTypeRepository;
	@InjectMocks
	private EngineTypePersistenceAdapter engineTypePersistenceAdapter;

	@Test
	void testLoadEngineTypeById() {
		//given
		EngineTypeDto engineTypeDto = defaultEngineTypeDto();
		given(engineTypeRepository.findByIdAndLocale(any(Long.class), any(String.class))).willReturn(Optional.of(engineTypeDto));
		
		//when
		Optional<EngineTypeDto> engineTypeDtoOptionalResult = engineTypePersistenceAdapter.loadEngineTypeById(1L, "en_US");
		
		//then
		then(engineTypeRepository).should(times(1)).findByIdAndLocale(eq(1L), eq( "en_US"));
		assertEquals(engineTypeDto.getId(), engineTypeDtoOptionalResult.get().getId());
		assertEquals(engineTypeDto.getCode(), engineTypeDtoOptionalResult.get().getCode());
		assertEquals(engineTypeDto.getDescription(), engineTypeDtoOptionalResult.get().getDescription());
		assertEquals(engineTypeDto.getName(), engineTypeDtoOptionalResult.get().getName());
		
	}
}
