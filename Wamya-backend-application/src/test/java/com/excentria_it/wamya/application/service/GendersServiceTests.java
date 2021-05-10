package com.excentria_it.wamya.application.service;

import static com.excentria_it.wamya.test.data.common.GenderTestData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.excentria_it.wamya.application.port.out.LoadGendersPort;
import com.excentria_it.wamya.domain.LoadGendersDto;

@ExtendWith(MockitoExtension.class)
public class GendersServiceTests {
	@Mock
	private LoadGendersPort loadGendersPort;
	@InjectMocks
	private GendersService gendersService;

	@Test
	void testLoadAllGenders() {
		// given

		List<LoadGendersDto> gendersDtos = defaultLoadGendersDtos();
		given(loadGendersPort.loadAllGenders(any(String.class))).willReturn(gendersDtos);
		// when
		List<LoadGendersDto> gendersDtosResult = gendersService.loadAllGenders("en_US");
		// then
		assertEquals(gendersDtos, gendersDtosResult);
	}
}