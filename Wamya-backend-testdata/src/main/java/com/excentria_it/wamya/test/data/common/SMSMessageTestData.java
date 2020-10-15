package com.excentria_it.wamya.test.data.common;

import static com.excentria_it.wamya.test.data.common.TestConstants.*;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.excentria_it.wamya.common.domain.SMSMessage;
import com.excentria_it.wamya.common.domain.SMSMessage.SMSMessageBuilder;
import com.excentria_it.wamya.common.domain.SMSTemplate;

public class SMSMessageTestData {

	public static SMSMessageBuilder defaultSMSMessageBuilder() {
		Map<String, String> params = new HashMap<>();
		SMSTemplate.PHONE_VALIDATION.getTemplateParams().forEach(p -> {
			params.put(p, p + "_value");

		});
		return SMSMessage.builder().to(DEFAULT_CALLABLE_MOBILE_NUMBER).template(SMSTemplate.PHONE_VALIDATION)
				.params(params).locale(new Locale("fr"));
	}
}
