package com.excentria_it.wamya.adapter.web.adapter;

import static com.excentria_it.wamya.adapter.web.helper.ResponseBodyMatchers.*;
import static com.excentria_it.wamya.test.data.common.LocaleTestData.*;
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

import com.excentria_it.wamya.application.port.in.LoadLocalesUseCase;
import com.excentria_it.wamya.common.exception.handlers.RestApiExceptionHandler;
import com.excentria_it.wamya.domain.LoadLocalesDto;
import com.excentria_it.wamya.domain.LoadLocalesResult;

@ActiveProfiles(profiles = { "web-local" })
@Import(value = { LocalesController.class, RestApiExceptionHandler.class })
@WebMvcTest(controllers = LocalesController.class)
public class LocalesControllerTests {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private LoadLocalesUseCase loadLocalesUseCase;

	@Test
	void testLoadAllLocales() throws Exception {
		// given
		List<LoadLocalesDto> localesDtos = defaultLoadLocalesDtos();
		given(loadLocalesUseCase.loadAllLocales()).willReturn(localesDtos);

		LoadLocalesResult expectedResult = new LoadLocalesResult(localesDtos.size(), localesDtos);
		// when
		mockMvc.perform(get("/locales")).andExpect(status().isOk())
				.andExpect(responseBody().containsObjectAsJson(expectedResult, LoadLocalesResult.class));

		// Then
		then(loadLocalesUseCase).should(times(1)).loadAllLocales();
	}

	@Test
	void testLoadAllLocalesFromEmptyLocales() throws Exception {
		// given
		given(loadLocalesUseCase.loadAllLocales()).willReturn(Collections.emptyList());

		LoadLocalesResult expectedResult = new LoadLocalesResult(0, Collections.emptyList());
		// when
		mockMvc.perform(get("/locales")).andExpect(status().isOk())
				.andExpect(responseBody().containsObjectAsJson(expectedResult, LoadLocalesResult.class));

		// Then
		then(loadLocalesUseCase).should(times(1)).loadAllLocales();
	}

	@Test
	void testLoadAllLocalesFromNullLocales() throws Exception {
		// given
		given(loadLocalesUseCase.loadAllLocales()).willReturn(null);

		LoadLocalesResult expectedResult = new LoadLocalesResult(0, Collections.emptyList());
		// when
		mockMvc.perform(get("/locales")).andExpect(status().isOk())
				.andExpect(responseBody().containsObjectAsJson(expectedResult, LoadLocalesResult.class));

		// Then
		then(loadLocalesUseCase).should(times(1)).loadAllLocales();
	}
}
