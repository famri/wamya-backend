package com.excentria_it.wamya.application.service;

import static com.excentria_it.wamya.test.data.common.CountryTestData.*;
import static com.excentria_it.wamya.test.data.common.DepartmentTestData.*;
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
import com.excentria_it.wamya.application.port.out.SearchDepartmentPort;
import com.excentria_it.wamya.common.exception.CountryNotFoundException;
import com.excentria_it.wamya.domain.AutoCompleteDepartmentsDto;
import com.excentria_it.wamya.domain.CountryDto;

@ExtendWith(MockitoExtension.class)
public class DepartmentServiceTests {
	@Mock
	private SearchDepartmentPort searchDepartmentPort;
	@Mock
	private LoadCountryPort loadCountryPort;

	@InjectMocks
	private DepartmentService departmentService;

	@Test
	void givenValidInput_whenAutoCompleteDepartment_ThenSucceed() {

		// given
		CountryDto countryDto = defaultCountryDto();
		given(loadCountryPort.loadCountryByCodeAndLocale(any(String.class), any(String.class)))
				.willReturn(Optional.of(countryDto));
		List<AutoCompleteDepartmentsDto> autoCompleteDepartmentsDtos = defaultAutoCompleteDepartmentsDtos();
		given(searchDepartmentPort.searchDepartment(any(String.class), any(Long.class), any(String.class)))
				.willReturn(autoCompleteDepartmentsDtos);
		// when
		List<AutoCompleteDepartmentsDto> autoCompleteDepartmentsDtosResult = departmentService
				.autoCompleteDepartment(" Be ", " TN ", "fr_FR");
		// then
		then(loadCountryPort).should((times(1))).loadCountryByCodeAndLocale("TN", "fr_FR");
		then(searchDepartmentPort).should((times(1))).searchDepartment(eq("Be"), eq(countryDto.getId()), eq("fr_FR"));
		assertEquals(autoCompleteDepartmentsDtos, autoCompleteDepartmentsDtosResult);
	}

	@Test
	void givenInexistentCountryCode_whenAutoCompleteDepartment_ThenThrowCountryNotFoundException() {
		// given
		given(loadCountryPort.loadCountryByCodeAndLocale(any(String.class), any(String.class)))
				.willReturn(Optional.empty());
		// when //then
		assertThrows(CountryNotFoundException.class,
				() -> departmentService.autoCompleteDepartment(" Be ", " TN ", "fr_FR"));
	}
}
