package com.excentria_it.wamya.adapter.persistence.adapter;

import static com.excentria_it.wamya.test.data.common.ConstructorTestData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.excentria_it.wamya.adapter.persistence.repository.ConstructorRepository;
import com.excentria_it.wamya.domain.ConstructorDto;

@ExtendWith(MockitoExtension.class)
public class ConstructorPersistenceAdapterTests {

	@Mock
	private ConstructorRepository constructorRepository;
	@InjectMocks
	private ConstructorPersistenceAdapter constructorPersistenceAdapter;

	@Test
	void givenConstructorId_WhenLoadConstructorById_ThenSucceed() {

		// given
		ConstructorDto constructorDto = defaultConstructorDto();
		given(constructorRepository.findConstructorById(any(Long.class))).willReturn(Optional.of(constructorDto));

		// when

		Optional<ConstructorDto> constructorDtoOptional = constructorPersistenceAdapter.loadConstructorById(1L);

		assertEquals(constructorDto, constructorDtoOptional.get());

	}
}
