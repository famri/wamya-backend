package com.excentria_it.wamya.common.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

public class PushTemplateTests {
	@Test
	void testPROPOSAL_REJECTEDTemplateParams() {

		List<String> supposedTemplateParams = List.of("departure_place_name", "arrival_place_name",
				"journey_request_datetime");
		List<String> templateParams = PushTemplate.PROPOSAL_REJECTED.getTemplateParams();
		assertEquals(supposedTemplateParams.size(), templateParams.size());
		assertThat(supposedTemplateParams.containsAll(templateParams));
	}

	@Test
	void testPROPOSAL_ACCEPTEDTemplateParams() {

		List<String> supposedTemplateParams = List.of("departure_place_name", "arrival_place_name",
				"journey_request_datetime");
		List<String> templateParams = PushTemplate.PROPOSAL_ACCEPTED.getTemplateParams();
		assertEquals(supposedTemplateParams.size(), templateParams.size());
		assertThat(supposedTemplateParams.containsAll(templateParams));
	}
}
