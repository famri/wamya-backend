package com.excentria_it.wamya.application.service;

import static com.excentria_it.wamya.test.data.common.CountryTestData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.excentria_it.wamya.application.port.out.LoadCountriesPort;
import com.excentria_it.wamya.domain.LoadCountriesDto;

@ExtendWith(MockitoExtension.class)
public class CountryServiceTests {

	@Mock
	private LoadCountriesPort loadCountriesPort;
	@InjectMocks
	private CountryService countryService;

	@Test
	void testLoadAllCountries() {
		// given

		List<LoadCountriesDto> countries = defaultLoadCountriesDtos();
		given(loadCountriesPort.loadAllCountries("fr_FR")).willReturn(countries);
		// when
		List<LoadCountriesDto> result = countryService.loadAllCountries("fr_FR");
		// then
		assertEquals(countries, result);
	}

}