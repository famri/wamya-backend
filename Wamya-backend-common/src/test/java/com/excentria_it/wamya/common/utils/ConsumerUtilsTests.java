package com.excentria_it.wamya.common.utils;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

public class ConsumerUtilsTests {
	@Test
	void testWithCounter() {
		List<String> strList = List.of("A", "B", "C");
		List<Integer> intList = new ArrayList<>();
		strList.stream().forEach(ConsumerUtils.withCounter((index, letter) -> {
			intList.add(index);
		}));

		assertEquals(intList, List.of(0, 1, 2));
	}
}
