package com.excentria_it.wamya.common.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

public class SMSTemplateTests {
	@Test
	void testPHONE_VALIDATIONTemplateParams() {

		List<String> supposedTemplateParams = List.of("validation_code");
		List<String> templateParams = SMSTemplate.PHONE_VALIDATION.getTemplateParams();
		assertEquals(supposedTemplateParams.size(), templateParams.size());
		assertThat(supposedTemplateParams.containsAll(templateParams));
	}

}
