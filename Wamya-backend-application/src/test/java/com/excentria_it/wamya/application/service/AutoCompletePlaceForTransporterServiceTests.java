package com.excentria_it.wamya.application.service;

import static com.excentria_it.wamya.test.data.common.CountryTestData.*;
import static com.excentria_it.wamya.test.data.common.DepartmentTestData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.excentria_it.wamya.application.port.out.LoadCountryPort;
import com.excentria_it.wamya.application.port.out.SearchDepartmentPort;
import com.excentria_it.wamya.application.utils.AutoCompletePlaceMapper;
import com.excentria_it.wamya.common.exception.CountryNotFoundException;
import com.excentria_it.wamya.domain.AutoCompleteDepartmentDto;
import com.excentria_it.wamya.domain.AutoCompletePlaceDto;
import com.excentria_it.wamya.domain.CountryDto;

@ExtendWith(MockitoExtension.class)
public class AutoCompletePlaceForTransporterServiceTests {
	@Mock
	private SearchDepartmentPort searchDepartmentPort;
	@Mock
	private LoadCountryPort loadCountryPort;

	@InjectMocks
	private AutoCompletePlaceForTransporterService departmentService;

	@Test
	void givenValidInput_whenAutoCompleteDepartment_ThenSucceed() {

		// given
		CountryDto countryDto = defaultCountryDto();
		given(loadCountryPort.loadCountryByCodeAndLocale(any(String.class), any(String.class)))
				.willReturn(Optional.of(countryDto));

		List<AutoCompleteDepartmentDto> autoCompleteDepartmentsDtos = defaultAutoCompleteDepartmentsDtos();
		given(searchDepartmentPort.searchDepartmentByName(any(String.class), any(Long.class), any(Integer.class),
				any(String.class))).willReturn(autoCompleteDepartmentsDtos);
		List<AutoCompletePlaceDto> expectedResult = autoCompleteDepartmentsDtos.stream()
				.map(d -> AutoCompletePlaceMapper.mapDepartmentToPlace(d)).collect(Collectors.toList());
		// when
		List<AutoCompletePlaceDto> autoCompletePlaceDtosResult = departmentService.autoCompleteDepartment(" Be ",
				" TN ", 5, "fr_FR");
		// then
		then(loadCountryPort).should((times(1))).loadCountryByCodeAndLocale("TN", "fr_FR");
		then(searchDepartmentPort).should((times(1))).searchDepartmentByName(eq("Be"), eq(countryDto.getId()), eq(5),
				eq("fr_FR"));
		assertEquals(expectedResult, autoCompletePlaceDtosResult);
	}

	@Test
	void givenInexistentCountryCode_whenAutoCompleteDepartment_ThenThrowCountryNotFoundException() {
		// given
		given(loadCountryPort.loadCountryByCodeAndLocale(any(String.class), any(String.class)))
				.willReturn(Optional.empty());
		// when //then
		assertThrows(CountryNotFoundException.class,
				() -> departmentService.autoCompleteDepartment(" Be ", " TN ", 5, "fr_FR"));
	}
}
