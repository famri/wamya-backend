package com.excentria_it.wamya.domain;

import static com.excentria_it.wamya.test.data.common.DepartmentTestData.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import com.excentria_it.wamya.application.utils.AutoCompletePlaceMapper;

public class AutoCompletePlaceResultTests {
	@Test
	void testBuilder() {
		List<AutoCompleteDepartmentDto> autoCompleteDepartmentDtos = defaultAutoCompleteDepartmentsDtos();
		List<AutoCompletePlaceDto> places = autoCompleteDepartmentDtos.stream()
				.map(dp -> AutoCompletePlaceMapper.mapDepartmentToPlace(dp)).collect(Collectors.toList());
		AutoCompletePlaceResult autoCompletePlaceResult = new AutoCompletePlaceResult.Builder().withTotalElements(2)
				.withContent(places

				).build();

		assertEquals(places, autoCompletePlaceResult.getContent());
		assertEquals(2, autoCompletePlaceResult.getTotalElements());
	}
}
