package com.excentria_it.wamya.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class DepartmentDtoTests {
	@Test
	void testBuilder() {
		DepartmentDto department = new DepartmentDto.Builder().withId(1L).withName("dep").build();
		assertEquals(1L, department.getId());
		assertEquals("dep", department.getName());
	}
}
