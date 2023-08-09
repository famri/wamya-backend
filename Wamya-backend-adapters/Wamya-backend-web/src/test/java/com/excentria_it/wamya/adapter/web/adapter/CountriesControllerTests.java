package com.excentria_it.wamya.adapter.web.adapter;

import static com.excentria_it.wamya.adapter.web.helper.ResponseBodyMatchers.*;
import static com.excentria_it.wamya.test.data.common.CountryTestData.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.excentria_it.wamya.application.port.in.LoadCountriesUseCase;
import com.excentria_it.wamya.common.exception.handlers.RestApiExceptionHandler;
import com.excentria_it.wamya.domain.LoadCountriesDto;
import com.excentria_it.wamya.domain.LoadCountriesResult;

@ActiveProfiles(profiles = { "web-local" })
@Import(value = { CountriesController.class, RestApiExceptionHandler.class })
@WebMvcTest(controllers = CountriesController.class)
public class CountriesControllerTests {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private LoadCountriesUseCase loadCountriesUseCase;

	@Test
	void testLoadAllCountries() throws Exception {
		// given
		List<LoadCountriesDto> countriesDtos = defaultLoadCountriesDtos();
		given(loadCountriesUseCase.loadAllCountries(any(String.class))).willReturn(countriesDtos);

		LoadCountriesResult expectedResult = new LoadCountriesResult(countriesDtos.size(), countriesDtos);
		// when
		mockMvc.perform(get("/countries").param("lang", "fr_FR")).andExpect(status().isOk())
				.andExpect(responseBody().containsObjectAsJson(expectedResult, LoadCountriesResult.class));

		// Then
		then(loadCountriesUseCase).should(times(1)).loadAllCountries(eq("fr_FR"));
	}
	
	@Test
	void testLoadAllCountriesFromEmptyCountryEntities() throws Exception {
		// given
		given(loadCountriesUseCase.loadAllCountries(any(String.class))).willReturn(Collections.emptyList());

		LoadCountriesResult expectedResult = new LoadCountriesResult(0, Collections.emptyList());
		// when
		mockMvc.perform(get("/countries").param("lang", "fr_FR")).andExpect(status().isOk())
				.andExpect(responseBody().containsObjectAsJson(expectedResult, LoadCountriesResult.class));

		// Then
		then(loadCountriesUseCase).should(times(1)).loadAllCountries(eq("fr_FR"));
	}
	
	@Test
	void testLoadAllCountriesFromNullCountryEntities() throws Exception {
		// given
		given(loadCountriesUseCase.loadAllCountries(any(String.class))).willReturn(null);

		LoadCountriesResult expectedResult = new LoadCountriesResult(0, Collections.emptyList());
		// when
		mockMvc.perform(get("/countries").param("lang", "fr_FR")).andExpect(status().isOk())
				.andExpect(responseBody().containsObjectAsJson(expectedResult, LoadCountriesResult.class));

		// Then
		then(loadCountriesUseCase).should(times(1)).loadAllCountries(eq("fr_FR"));
	}
}
