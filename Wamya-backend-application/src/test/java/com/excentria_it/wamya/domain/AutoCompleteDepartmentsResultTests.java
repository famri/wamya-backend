package com.excentria_it.wamya.domain;

import static com.excentria_it.wamya.test.data.common.DepartmentTestData.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

public class AutoCompleteDepartmentsResultTests {
	@Test
	void testBuilder() {
		List<AutoCompleteDepartmentsDto> autoCompleteDepartmentDtos = defaultAutoCompleteDepartmentsDtos();
		AutoCompleteDepartmentsResult autoCompleteDepartmentsResult = new AutoCompleteDepartmentsResult.Builder()
				.withTotalElements(2).withContent(autoCompleteDepartmentDtos).build();

		assertEquals(autoCompleteDepartmentDtos, autoCompleteDepartmentsResult.getContent());
		assertEquals(2, autoCompleteDepartmentsResult.getTotalElements());
	}
}
