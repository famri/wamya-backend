package com.excentria_it.wamya.adapter.persistence.adapter;

import static com.excentria_it.wamya.test.data.common.ConstructorTestData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import com.excentria_it.wamya.adapter.persistence.repository.ConstructorRepository;
import com.excentria_it.wamya.domain.ConstructorDto;
import com.excentria_it.wamya.domain.LoadConstructorsDto;

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
		//then
		then(constructorRepository).should(times(1)).findConstructorById(eq(1L));
		assertEquals(constructorDto, constructorDtoOptional.get());

	}

	@Test
	void testLoadAllConstructors() {

		// given
		List<LoadConstructorsDto> constructorDtos = defaultLoadConstructorsDtos();
		given(constructorRepository.findAllBy(any(Sort.class))).willReturn(constructorDtos);

		// when

		List<LoadConstructorsDto>  constructorDtosResult = constructorPersistenceAdapter.loadAllConstructors();
		//then
		then(constructorRepository).should(times(1)).findAllBy(any(Sort.class));
		
		assertEquals(constructorDtos, constructorDtosResult);

	}
}
