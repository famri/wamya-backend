package com.excentria_it.wamya.adapter.persistence.adapter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.excentria_it.wamya.adapter.persistence.repository.DepartmentRepository;
import com.excentria_it.wamya.domain.DepartmentDto;

@ExtendWith(MockitoExtension.class)
public class DepartmentPersistenceAdapterTests {
	@Mock
	private DepartmentRepository departmentRepository;
	@InjectMocks
	private DepartmentPersistenceAdapter departmentPersistenceAdapter;

	@Test
	void testLoadDepartmentByName() {
		// given
		DepartmentDto departmentDto = new DepartmentDto(1L, "Department");
		given(departmentRepository.findByNameAndLocale(any(String.class), any(String.class)))
				.willReturn(Optional.of(departmentDto));
		// when
		Optional<DepartmentDto> department = departmentPersistenceAdapter.loadDepartmentByName("Department", "fr_FR");

		// then
		assertEquals(departmentDto, department.get());
	}
}
