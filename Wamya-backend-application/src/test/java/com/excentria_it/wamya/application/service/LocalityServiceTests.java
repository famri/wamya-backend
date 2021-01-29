package com.excentria_it.wamya.application.service;

import static com.excentria_it.wamya.test.data.common.CountryTestData.*;
import static com.excentria_it.wamya.test.data.common.LocalityTestData.*;
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

import com.excentria_it.wamya.application.port.out.LoadCountryPort;
import com.excentria_it.wamya.application.port.out.SearchLocalityPort;
import com.excentria_it.wamya.common.exception.CountryNotFoundException;
import com.excentria_it.wamya.domain.AutoCompleteLocalitiesDto;
import com.excentria_it.wamya.domain.CountryDto;

@ExtendWith(MockitoExtension.class)
public class LocalityServiceTests {
	@Mock
	private SearchLocalityPort searchLocalityPort;
	@Mock
	private LoadCountryPort loadCountryPort;

	@InjectMocks
	private LocalityService localityService;

	@Test
	void givenValidInput_whenAutoCompleteLocality_ThenSucceed() {

		// given
		CountryDto countryDto = defaultCountryDto();
		given(loadCountryPort.loadCountryByCodeAndLocale(any(String.class), any(String.class)))
				.willReturn(Optional.of(countryDto));
		List<AutoCompleteLocalitiesDto> autoCompleteLocalitiesDtos = defaultAutoCompleteLocalitiesDtos();
		given(searchLocalityPort.searchLocality(any(String.class), any(Long.class), any(String.class)))
				.willReturn(autoCompleteLocalitiesDtos);
		// when
		List<AutoCompleteLocalitiesDto> autoCompleteLocalitiesDtosResult = localityService.autoCompleteLocality(" thy ",
				" TN ", "fr_FR");
		// then
		then(loadCountryPort).should((times(1))).loadCountryByCodeAndLocale("TN", "fr_FR");
		then(searchLocalityPort).should((times(1))).searchLocality(eq("thy"), eq(countryDto.getId()), eq("fr_FR"));
		assertEquals(autoCompleteLocalitiesDtos, autoCompleteLocalitiesDtosResult);
	}

	@Test
	void givenInexistentCountryCode_whenAutoCompleteLocality_ThenThrowCountryNotFoundException() {
		// given
		given(loadCountryPort.loadCountryByCodeAndLocale(any(String.class), any(String.class)))
				.willReturn(Optional.empty());
		// when //then
		assertThrows(CountryNotFoundException.class,
				() -> localityService.autoCompleteLocality(" thy ", " TN ", "fr_FR"));
	}
}
