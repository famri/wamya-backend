package com.excentria_it.wamya.test.data.common;

import java.util.Locale;

import com.excentria_it.wamya.common.domain.EmailMessage;
import com.excentria_it.wamya.common.domain.EmailMessage.EmailMessageBuilder;

public class EmailMessageTestData {

	public static EmailMessageBuilder defaultEmailMessageBuilder() {

		return EmailMessage.builder().from(TestConstants.DEFAULT_FROM_EMAIL).to(TestConstants.DEFAULT_EMAIL)
				.subject(TestConstants.DEFAULT_EMAIL_SUBJECT).template(TestConstants.DEFAULT_EMAIL_TEMPLATE)
				.params(TestConstants.DEFAULT_TEMPLATE_PARAMS)
				.locale(new Locale(TestConstants.DEFAULT_TEMPLATE_LANGUAGE));
	}
}
