package com.excentria_it.wamya.common.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

public class EmailTemplateTests {

	@Test
	void testEMAIL_VALIDATIONTemplateParams() {

		List<String> supposedTemplateParams = List.of("validation_url");
		List<String> templateParams = EmailTemplate.EMAIL_VALIDATION.getTemplateParams();
		assertEquals(supposedTemplateParams.size(), templateParams.size());
		assertThat(supposedTemplateParams.containsAll(templateParams));
	}

	@Test
	void testEMAIL_VALIDATIONTemplateResources() {

		Map<String, String> supposedTemplateResources = Map.of("logo_wamya", "static/logo_wamya.png");
		Map<String, String> templateResources = EmailTemplate.EMAIL_VALIDATION.getTemplateResources();

		assertEquals(supposedTemplateResources.keySet().size(), templateResources.keySet().size());
		supposedTemplateResources.forEach((k, v) -> assertEquals(templateResources.get(k), v));

	}
}
