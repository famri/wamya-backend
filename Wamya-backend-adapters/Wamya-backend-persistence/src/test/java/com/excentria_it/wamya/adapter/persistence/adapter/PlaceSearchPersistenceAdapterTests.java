package com.excentria_it.wamya.adapter.persistence.adapter;

import static com.excentria_it.wamya.test.data.common.DelegationTestData.*;
import static com.excentria_it.wamya.test.data.common.DepartmentTestData.*;
import static com.excentria_it.wamya.test.data.common.LocalityTestData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.excentria_it.wamya.adapter.persistence.repository.DelegationRepository;
import com.excentria_it.wamya.adapter.persistence.repository.DepartmentRepository;
import com.excentria_it.wamya.adapter.persistence.repository.LocalityRepository;
import com.excentria_it.wamya.domain.AutoCompleteDelegationDto;
import com.excentria_it.wamya.domain.AutoCompleteDepartmentDto;
import com.excentria_it.wamya.domain.AutoCompleteLocalityDto;

@ExtendWith(MockitoExtension.class)
public class PlaceSearchPersistenceAdapterTests {
	@Mock
	private DepartmentRepository departmentRepository;
	@Mock
	private LocalityRepository localityRepository;
	@Mock
	private DelegationRepository delegationRepository;

	@InjectMocks
	private PlaceSearchPersistenceAdapter placeSearchPersistenceAdapter;

	@Test
	void givenExistentLocality_WhenSearchLocalityByName_ThenSucceed() {
		List<AutoCompleteLocalityDto> dtos = defaultAutoCompleteLocalitiesDtos();
		// given
		given(localityRepository.findByCountry_IdAndNameLikeIgnoringCase(any(Long.class), any(String.class),
				any(String.class), any(Pageable.class))).willReturn(dtos);
		// when
		List<AutoCompleteLocalityDto> dtosResult = placeSearchPersistenceAdapter.searchLocalityByName("cité", 1L, 5,
				"fr_FR");
		// then
		then(localityRepository).should(times(1)).findByCountry_IdAndNameLikeIgnoringCase(eq(1L), eq("cité"),
				eq("fr_FR"), eq(PageRequest.of(0, 5)));

		assertEquals(dtos, dtosResult);
	}

	@Test
	void givenExistentLocality_WhenSearchLocalityByNameAndDelegationName_ThenSucced() {
		List<AutoCompleteLocalityDto> dtos = defaultAutoCompleteLocalitiesDtos();
		// given
		given(localityRepository.findByCountry_IdAndDelegationNameLikeIgnoringCaseAndNameLikeIgnoringCase(
				any(Long.class), any(String.class), any(String.class), any(String.class), any(Pageable.class)))
						.willReturn(dtos);
		// when
		List<AutoCompleteLocalityDto> dtosResult = placeSearchPersistenceAdapter
				.searchLocalityByNameAndDelegationName("Cité El Moez 1", "Thy", 1L, 5, "fr_FR");
		// then
		then(localityRepository).should(times(1))
				.findByCountry_IdAndDelegationNameLikeIgnoringCaseAndNameLikeIgnoringCase(eq(1L), eq("Thy"),
						eq("Cité El Moez 1"), eq("fr_FR"), eq(PageRequest.of(0, 5)));

		assertEquals(dtos, dtosResult);
	}

	@Test
	void givenExistentLocality_WhenSearchLocalityByNameAndDepartmentName_ThenSucced() {
		List<AutoCompleteLocalityDto> dtos = defaultAutoCompleteLocalitiesDtos();
		// given
		given(localityRepository.findByCountry_IdAndDepartmentNameLikeIgnoringCaseAndNameLikeIgnoringCase(
				any(Long.class), any(String.class), any(String.class), any(String.class), any(Pageable.class)))
						.willReturn(dtos);
		// when
		List<AutoCompleteLocalityDto> dtosResult = placeSearchPersistenceAdapter
				.searchLocalityByNameAndDepartmentName("Cité El Moez 1", "Sfa", 1L, 5, "fr_FR");
		// then
		then(localityRepository).should(times(1))
				.findByCountry_IdAndDepartmentNameLikeIgnoringCaseAndNameLikeIgnoringCase(eq(1L), eq("Sfa"),
						eq("Cité El Moez 1"), eq("fr_FR"), eq(PageRequest.of(0, 5)));

		assertEquals(dtos, dtosResult);
	}

	@Test
	void givenExistentLocality_WhenSearchLocalityByNameAndDelegationNameAndDepartmentName_ThenSucced() {
		List<AutoCompleteLocalityDto> dtos = defaultAutoCompleteLocalitiesDtos();
		// given
		given(localityRepository
				.findByCountry_IdAndDepartmentNameLikeIgnoringCaseAndDelegationNameLikeIgnoringCaseAndNameLikeIgnoringCase(
						any(Long.class), any(String.class), any(String.class), any(String.class), any(String.class),
						any(Pageable.class))).willReturn(dtos);
		// when
		List<AutoCompleteLocalityDto> dtosResult = placeSearchPersistenceAdapter
				.searchLocalityByNameAndDelegationNameAndDepartmentName("Cité El Moez 1", "Thyna", "Sfa", 1L, 5,
						"fr_FR");
		// then
		then(localityRepository).should(times(1))
				.findByCountry_IdAndDepartmentNameLikeIgnoringCaseAndDelegationNameLikeIgnoringCaseAndNameLikeIgnoringCase(
						eq(1L), eq("Sfa"), eq("Thyna"), eq("Cité El Moez 1"), eq("fr_FR"), eq(PageRequest.of(0, 5)));

		assertEquals(dtos, dtosResult);
	}

	@Test
	void givenExistentDelegation_WhenSearchDelegationByName_ThenSucceed() {
		List<AutoCompleteDelegationDto> dtos = defaultAutoCompleteDelegationDtos();
		// given
		given(delegationRepository.findByCountry_IdAndNameLikeIgnoringCase(any(Long.class), any(String.class),
				any(String.class), any(Pageable.class))).willReturn(dtos);
		// when
		List<AutoCompleteDelegationDto> dtosResult = placeSearchPersistenceAdapter.searchDelegationByName("Thy", 1L, 5,
				"fr_FR");
		// then
		then(delegationRepository).should(times(1)).findByCountry_IdAndNameLikeIgnoringCase(eq(1L), eq("Thy"),
				eq("fr_FR"), eq(PageRequest.of(0, 5)));

		assertEquals(dtos, dtosResult);
	}

	@Test
	void givenExistentDelegation_WhenSearchDelegationByNameAndDepartmentName_ThenSucceed() {
		List<AutoCompleteDelegationDto> dtos = defaultAutoCompleteDelegationDtos();
		// given
		given(delegationRepository.findByCountry_IdAndDepartmentNameLikeIgnoringCaseAndNameLikeIgnoringCase(
				any(Long.class), any(String.class), any(String.class), any(String.class), any(Pageable.class)))
						.willReturn(dtos);
		// when
		List<AutoCompleteDelegationDto> dtosResult = placeSearchPersistenceAdapter
				.searchDelegationByNameAndDepartmentName("Thyna", "Sfa", 1L, 5, "fr_FR");
		// then
		then(delegationRepository).should(times(1))
				.findByCountry_IdAndDepartmentNameLikeIgnoringCaseAndNameLikeIgnoringCase(eq(1L), eq("Sfa"), eq("Thyna"),
						eq("fr_FR"), eq(PageRequest.of(0, 5)));

		assertEquals(dtos, dtosResult);
	}

	@Test
	void givenExistentDepartment_WhenSearchDepartmentByName_ThenSucceed() {
		List<AutoCompleteDepartmentDto> dtos = defaultAutoCompleteDepartmentsDtos();
		// given
		given(departmentRepository.findByCountry_IdAndNameLikeIgnoringCase(any(Long.class), any(String.class),
				any(String.class), any(Pageable.class))).willReturn(dtos);
		// when

		List<AutoCompleteDepartmentDto> dtosResult = placeSearchPersistenceAdapter.searchDepartmentByName("Be", 1L, 5,
				"fr_FR");
		// then
		then(departmentRepository).should(times(1)).findByCountry_IdAndNameLikeIgnoringCase(eq(1L), eq("Be"),
				eq("fr_FR"), eq(PageRequest.of(0, 5)));

		assertEquals(dtos, dtosResult);
	}

}
