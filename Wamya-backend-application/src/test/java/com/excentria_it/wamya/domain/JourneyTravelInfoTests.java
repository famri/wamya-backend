package com.excentria_it.wamya.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class JourneyTravelInfoTests {
	@Test
	void testBuilder() {
		JourneyTravelInfo jti = new JourneyTravelInfo.Builder().withDistance(200000).withHours(2).withMinutes(15)
				.build();
		assertEquals(200000, jti.getDistance());
		assertEquals(2, jti.getHours());
		assertEquals(15, jti.getMinutes());
	}
}
