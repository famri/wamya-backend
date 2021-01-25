package com.excentria_it.wamya.adapter.persistence.adapter;

import static com.excentria_it.wamya.test.data.common.CountryTestData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.excentria_it.wamya.adapter.persistence.repository.CountryRepository;
import com.excentria_it.wamya.domain.CountryDto;

@ExtendWith(MockitoExtension.class)
public class CountryPersistenceAdapterTests {
	@Mock
	private CountryRepository countryRepository;
	@InjectMocks
	private CountryPersistenceAdapter countryPersistenceAdapter;

	@Test
	void givenExistentCountryCode_whenLoadCountryByCodeAndLocale_ThenSucceed() {

		// given
		CountryDto countryDto = defaultCountryDto();
		given(countryRepository.findByCodeAndLocale(any(String.class), any(String.class)))
				.willReturn(Optional.of(countryDto));
		// when
		Optional<CountryDto> countryDtoOptional = countryPersistenceAdapter.loadCountryByCodeAndLocale("TN", "fr_FR");
		// then

		then(countryRepository).should(times(1)).findByCodeAndLocale("TN", "fr_FR");
		assertEquals(countryDto, countryDtoOptional.get());

	}
}
