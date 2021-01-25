package com.excentria_it.wamya.adapter.persistence.adapter;

import static com.excentria_it.wamya.test.data.common.DepartmentTestData.*;
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

import com.excentria_it.wamya.adapter.persistence.repository.DepartmentRepository;
import com.excentria_it.wamya.domain.AutoCompleteDepartmentsDto;

@ExtendWith(MockitoExtension.class)
public class DepartmentPersistenceAdapterTests {
	@Mock
	private DepartmentRepository departmentRepository;

	@InjectMocks
	private DepartmentPersistenceAdapter departmentPersistenceAdapter;

	@Test
	void givenExistentDepartment_WhenSearchDepartment_ThenSucceed() {
		List<AutoCompleteDepartmentsDto> dtos = defaultAutoCompleteDepartmentsDtos();
		// given
		given(departmentRepository.findByCountry_IdAndNameLikeIgnoringCase(any(Long.class), any(String.class),
				any(String.class))).willReturn(dtos);
		// when
		List<AutoCompleteDepartmentsDto> dtosResult = departmentPersistenceAdapter.searchDepartment("Be", 1L, "fr_FR");
		// then
		then(departmentRepository).should(times(1)).findByCountry_IdAndNameLikeIgnoringCase(1L, "Be", "fr_FR");
		assertEquals(dtos, dtosResult);
	}

}
