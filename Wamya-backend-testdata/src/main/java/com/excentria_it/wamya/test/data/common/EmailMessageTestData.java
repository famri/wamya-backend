package com.excentria_it.wamya.test.data.common;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.excentria_it.wamya.common.domain.EmailMessage;
import com.excentria_it.wamya.common.domain.EmailMessage.EmailMessageBuilder;

public class EmailMessageTestData {

	public static EmailMessageBuilder defaultEmailMessageBuilder() {
		Map<String, String> params = new HashMap<>();
		TestConstants.DEFAULT_EMAIL_TEMPLATE.getTemplateParams().forEach(p -> {
			params.put(p, p + "_value");
		});
		EmailMessageBuilder result = EmailMessage.builder().from(TestConstants.DEFAULT_FROM_EMAIL)
				.to(TestConstants.DEFAULT_EMAIL).subject(TestConstants.DEFAULT_EMAIL_SUBJECT)
				.template(TestConstants.DEFAULT_EMAIL_TEMPLATE).params(params)
				.locale(new Locale(TestConstants.DEFAULT_TEMPLATE_LANGUAGE));

		return result;
	}
}
