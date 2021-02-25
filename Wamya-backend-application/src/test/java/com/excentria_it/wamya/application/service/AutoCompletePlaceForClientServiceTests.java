package com.excentria_it.wamya.application.service;

import static com.excentria_it.wamya.test.data.common.CountryTestData.*;
import static com.excentria_it.wamya.test.data.common.DelegationTestData.*;
import static com.excentria_it.wamya.test.data.common.DepartmentTestData.*;
import static com.excentria_it.wamya.test.data.common.LocalityTestData.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.excentria_it.wamya.application.port.out.LoadCountryPort;
import com.excentria_it.wamya.application.port.out.SearchDelegationPort;
import com.excentria_it.wamya.application.port.out.SearchDepartmentPort;
import com.excentria_it.wamya.application.port.out.SearchLocalityPort;
import com.excentria_it.wamya.application.utils.AutoCompletePlaceMapper;
import com.excentria_it.wamya.common.exception.CountryNotFoundException;
import com.excentria_it.wamya.domain.AutoCompleteDelegationDto;
import com.excentria_it.wamya.domain.AutoCompleteDepartmentDto;
import com.excentria_it.wamya.domain.AutoCompleteLocalityDto;
import com.excentria_it.wamya.domain.AutoCompletePlaceDto;
import com.excentria_it.wamya.domain.CountryDto;

@ExtendWith(MockitoExtension.class)
public class AutoCompletePlaceForClientServiceTests {
	@Mock
	private LoadCountryPort loadCountryPort;
	@Mock
	private SearchLocalityPort searchLocalityPort;
	@Mock
	private SearchDelegationPort searchDelegationPort;
	@Mock
	private SearchDepartmentPort searchDepartmentPort;

	@InjectMocks
	private AutoCompletePlaceForClientService localityService;

	@Test
	void givenValidCountryCodeAndOneWordInput_whenAutoCompleteLocality_ThenSucceed() {

		// given
		// country code is ok
		CountryDto countryDto = defaultCountryDto();
		given(loadCountryPort.loadCountryByCodeAndLocale(any(String.class), any(String.class)))
				.willReturn(Optional.of(countryDto));

		List<AutoCompleteLocalityDto> autoCompleteLocalityDtos = defaultAutoCompleteLocalitiesDtos();

		// locality result
		given(searchLocalityPort.searchLocalityByName(any(String.class), any(Long.class), any(Integer.class),
				any(String.class))).willReturn(autoCompleteLocalityDtos);
		List<AutoCompletePlaceDto> expectedLocalities = autoCompleteLocalityDtos.stream()
				.map(l -> AutoCompletePlaceMapper.mapLocalityToPlace(l)).collect(Collectors.toList());

		// delegation result;
		List<AutoCompleteDelegationDto> autoCompleteDelegationDtos = defaultAutoCompleteDelegationDtos();
		given(searchDelegationPort.searchDelegationByName(any(String.class), any(Long.class), any(Integer.class),
				any(String.class))).willReturn(autoCompleteDelegationDtos);
		List<AutoCompletePlaceDto> expectedDelegations = autoCompleteDelegationDtos.stream()
				.map(dl -> AutoCompletePlaceMapper.mapDelegationToPlace(dl)).collect(Collectors.toList());

		// department result
		List<AutoCompleteDepartmentDto> autoCompleteDepartmentDtos = defaultAutoCompleteDepartmentsDtos();
		given(searchDepartmentPort.searchDepartmentByName(any(String.class), any(Long.class), any(Integer.class),
				any(String.class))).willReturn(autoCompleteDepartmentDtos);

		List<AutoCompletePlaceDto> expectedDepartments = autoCompleteDepartmentDtos.stream()
				.map(d -> AutoCompletePlaceMapper.mapDepartmentToPlace(d)).collect(Collectors.toList());

		List<AutoCompletePlaceDto> expectedResult = new ArrayList<>(
				expectedDepartments.size() + expectedDelegations.size() + expectedLocalities.size());
		expectedResult.addAll(expectedDepartments);
		expectedResult.addAll(expectedDelegations);
		expectedResult.addAll(expectedLocalities);

		// when
		List<AutoCompletePlaceDto> autoCompleteLocalitiesDtosResult = localityService.autoCompletePlace(" thy ", " TN ",
				5, "fr_FR");
		// then
		then(loadCountryPort).should((times(1))).loadCountryByCodeAndLocale("TN", "fr_FR");
		then(searchDepartmentPort).should((times(1))).searchDepartmentByName(eq("thy"), eq(countryDto.getId()), eq(5),
				eq("fr_FR"));
		then(searchDelegationPort).should((times(1))).searchDelegationByName(eq("thy"), eq(countryDto.getId()), eq(5),
				eq("fr_FR"));
		then(searchLocalityPort).should((times(1))).searchLocalityByName(eq("thy"), eq(countryDto.getId()), eq(5),
				eq("fr_FR"));

		assertEquals(expectedResult, autoCompleteLocalitiesDtosResult);
	}

	@Test
	void givenValidCountryCodeAndTwoWordsInput_whenAutoCompleteLocality_ThenSucceed() {
		// given
		// country code is ok
		CountryDto countryDto = defaultCountryDto();
		given(loadCountryPort.loadCountryByCodeAndLocale(any(String.class), any(String.class)))
				.willReturn(Optional.of(countryDto));

		List<AutoCompleteLocalityDto> autoCompleteLocalityDtos = defaultAutoCompleteLocalitiesDtos();

		// locality and delegation result
		given(searchLocalityPort.searchLocalityByNameAndDelegationName(any(String.class), any(String.class),
				any(Long.class), any(Integer.class), any(String.class)))
						.willReturn(autoCompleteLocalityDtos.subList(0, 1));

		List<AutoCompletePlaceDto> expectedLocalitiesAndDelegations = autoCompleteLocalityDtos.subList(0, 1).stream()
				.map(l -> AutoCompletePlaceMapper.mapLocalityToPlace(l)).collect(Collectors.toList());

		// locality and department result;
		given(searchLocalityPort.searchLocalityByNameAndDepartmentName(any(String.class), any(String.class),
				any(Long.class), any(Integer.class), any(String.class)))
						.willReturn(autoCompleteLocalityDtos.subList(1, 2));
		List<AutoCompletePlaceDto> expectedLocalitiesAndDepartments = autoCompleteLocalityDtos.subList(1, 2).stream()
				.map(l -> AutoCompletePlaceMapper.mapLocalityToPlace(l)).collect(Collectors.toList());

		// delegation and department result
		List<AutoCompleteDelegationDto> autoCompleteDelegationDtos = defaultAutoCompleteDelegationDtos();
		given(searchDelegationPort.searchDelegationByNameAndDepartmentName(any(String.class), any(String.class),
				any(Long.class), any(Integer.class), any(String.class))).willReturn(autoCompleteDelegationDtos);

		List<AutoCompletePlaceDto> expectedDelegations = autoCompleteDelegationDtos.stream()
				.map(dl -> AutoCompletePlaceMapper.mapDelegationToPlace(dl)).collect(Collectors.toList());

		List<AutoCompletePlaceDto> expectedResult = new ArrayList<>(expectedDelegations.size()
				+ expectedLocalitiesAndDepartments.size() + expectedLocalitiesAndDelegations.size());

		expectedResult.addAll(expectedDelegations);
		expectedResult.addAll(expectedLocalitiesAndDelegations);
		expectedResult.addAll(expectedLocalitiesAndDepartments);

		// when
		List<AutoCompletePlaceDto> autoCompleteLocalitiesDtosResult = localityService
				.autoCompletePlace(" Cité El Moez 1 , Sfa ", " TN ", 5, "fr_FR");
		// then
		then(loadCountryPort).should((times(1))).loadCountryByCodeAndLocale("TN", "fr_FR");
		then(searchLocalityPort).should((times(1))).searchLocalityByNameAndDelegationName(eq("Cité El Moez 1"),
				eq("Sfa"), eq(countryDto.getId()), eq(5), eq("fr_FR"));
		then(searchLocalityPort).should((times(1))).searchLocalityByNameAndDepartmentName(eq("Cité El Moez 1"),
				eq("Sfa"), eq(countryDto.getId()), eq(5), eq("fr_FR"));
		then(searchDelegationPort).should((times(1))).searchDelegationByNameAndDepartmentName(eq("Cité El Moez 1"),
				eq("Sfa"), eq(countryDto.getId()), eq(5), eq("fr_FR"));

		assertEquals(expectedResult, autoCompleteLocalitiesDtosResult);
	}

	@Test
	void givenValidCountryCodeAndThreeWordsInput_whenAutoCompleteLocality_ThenSucceed() {
		// given
		// country code is ok
		CountryDto countryDto = defaultCountryDto();
		given(loadCountryPort.loadCountryByCodeAndLocale(any(String.class), any(String.class)))
				.willReturn(Optional.of(countryDto));

		List<AutoCompleteLocalityDto> autoCompleteLocalityDtos = defaultAutoCompleteLocalitiesDtos();

		// locality and delegation result
		given(searchLocalityPort.searchLocalityByNameAndDelegationNameAndDepartmentName(any(String.class),
				any(String.class), any(String.class), any(Long.class), any(Integer.class), any(String.class)))
						.willReturn(autoCompleteLocalityDtos);

		List<AutoCompletePlaceDto> expectedResult = autoCompleteLocalityDtos.stream()
				.map(l -> AutoCompletePlaceMapper.mapLocalityToPlace(l)).collect(Collectors.toList());

		// when
		List<AutoCompletePlaceDto> autoCompleteLocalitiesDtosResult = localityService
				.autoCompletePlace(" Cité El Moez 1 , Thyna , Sfa ", " TN ", 5, "fr_FR");
		// then
		then(loadCountryPort).should((times(1))).loadCountryByCodeAndLocale("TN", "fr_FR");
		then(searchLocalityPort).should((times(1))).searchLocalityByNameAndDelegationNameAndDepartmentName(
				eq("Cité El Moez 1"), eq("Thyna"), eq("Sfa"), eq(countryDto.getId()), eq(5), eq("fr_FR"));

		assertEquals(expectedResult, autoCompleteLocalitiesDtosResult);
	}

	@Test
	void givenEmptyInput_WhenAutoCompletePlace_ThenReturnEmptyResult() {
		// given

		String input = "";

		// when

		List<AutoCompletePlaceDto> autoCompleteLocalitiesDtosResult = localityService.autoCompletePlace(input, " TN ",
				5, "fr_FR");
		// then
		assertThat(autoCompleteLocalitiesDtosResult).isEmpty();
	}

	@Test
	void givenValidCountryCodeAndEmptyInputPart_WhenAutoCompletePlace_ThenReturnEmptyResult() {
		// given
		// country code is ok
		CountryDto countryDto = defaultCountryDto();
		given(loadCountryPort.loadCountryByCodeAndLocale(any(String.class), any(String.class)))
				.willReturn(Optional.of(countryDto));

		String input = "Cité Thyna 1, ,Sfax";

		// when

		List<AutoCompletePlaceDto> autoCompleteLocalitiesDtosResult = localityService.autoCompletePlace(input, " TN ",
				5, "fr_FR");
		// then
		assertThat(autoCompleteLocalitiesDtosResult).isEmpty();
	}

	@Test
	void givenValidCountryCodeAndFourWordsInput_WhenAutoCompletePlace_ThenReturnEmptyResult(){
		// given
		// country code is ok
		CountryDto countryDto = defaultCountryDto();
		given(loadCountryPort.loadCountryByCodeAndLocale(any(String.class), any(String.class)))
				.willReturn(Optional.of(countryDto));

		String input = "Cité Thyna 1, Thyna , Sfax Sud, Sfax";

		// when

		List<AutoCompletePlaceDto> autoCompleteLocalitiesDtosResult = localityService.autoCompletePlace(input, " TN ",
				5, "fr_FR");
		// then
		assertThat(autoCompleteLocalitiesDtosResult).isEmpty();
	}

	@Test
	void givenInexistentCountryCode_whenAutoCompleteLocality_ThenThrowCountryNotFoundException() {
		// given
		given(loadCountryPort.loadCountryByCodeAndLocale(any(String.class), any(String.class)))
				.willReturn(Optional.empty());
		// when //then
		assertThrows(CountryNotFoundException.class,
				() -> localityService.autoCompletePlace(" thy ", " TN ", 5, "fr_FR"));
	}
}
