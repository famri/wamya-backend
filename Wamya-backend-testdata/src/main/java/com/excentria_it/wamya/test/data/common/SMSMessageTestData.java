package com.excentria_it.wamya.test.data.common;

import static com.excentria_it.wamya.test.data.common.TestConstants.*;

import java.util.Locale;
import java.util.Map;

import com.excentria_it.wamya.common.domain.SMSMessage;
import com.excentria_it.wamya.common.domain.SMSMessage.SMSMessageBuilder;
import com.excentria_it.wamya.common.domain.SMSTemplate;

public class SMSMessageTestData {

	public static SMSMessageBuilder defaultSMSMessageBuilder() {
		return SMSMessage.builder().to(DEFAULT_CALLABLE_MOBILE_NUMBER).template(SMSTemplate.PHONE_VALIDATION)
				.params(Map.of("validation_code", DEFAULT_VALIDATION_CODE)).locale(new Locale("fr"));
	}
}
