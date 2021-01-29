package com.excentria_it.wamya.adapter.persistence.adapter;

import static com.excentria_it.wamya.test.data.common.LocalityTestData.*;
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

import com.excentria_it.wamya.adapter.persistence.repository.LocalityRepository;
import com.excentria_it.wamya.domain.AutoCompleteLocalitiesDto;

@ExtendWith(MockitoExtension.class)
public class LocalityPersistenceAdapterTests {
	@Mock
	private LocalityRepository localityRepository;

	@InjectMocks
	private LocalityPersistenceAdapter localityPersistenceAdapter;

	@Test
	void givenExistentLocality_WhenSearchLocality_ThenSucceed() {
		List<AutoCompleteLocalitiesDto> dtos = defaultAutoCompleteLocalitiesDtos();
		// given
		given(localityRepository.findByCountry_IdAndNameLikeIgnoringCase(any(Long.class), any(String.class),
				any(String.class))).willReturn(dtos);
		// when
		List<AutoCompleteLocalitiesDto> dtosResult = localityPersistenceAdapter.searchLocality("thy", 1L, "fr_FR");
		// then
		then(localityRepository).should(times(1)).findByCountry_IdAndNameLikeIgnoringCase(1L, "thy", "fr_FR");
		assertEquals(dtos, dtosResult);
	}

}
