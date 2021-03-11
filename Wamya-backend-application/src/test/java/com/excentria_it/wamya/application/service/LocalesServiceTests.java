package com.excentria_it.wamya.application.service;

import static com.excentria_it.wamya.test.data.common.LocaleTestData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.excentria_it.wamya.application.port.out.LoadLocalesPort;
import com.excentria_it.wamya.domain.LoadLocalesDto;

@ExtendWith(MockitoExtension.class)
public class LocalesServiceTests {

	@Mock
	private LoadLocalesPort loadLocalesPort;
	@InjectMocks
	private LocalesService localesService;

	@Test
	void testLoadAllLocales() {
		// given

		List<LoadLocalesDto> locales = defaultLoadLocalesDtos();
		given(loadLocalesPort.loadAllLocales()).willReturn(locales);
		// when
		List<LoadLocalesDto> result = localesService.loadAllLocales();
		// then
		assertEquals(locales, result);
	}

}
