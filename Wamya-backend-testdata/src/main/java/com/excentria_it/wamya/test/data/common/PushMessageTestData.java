package com.excentria_it.wamya.test.data.common;

import java.util.HashMap;
import java.util.Map;

import com.excentria_it.wamya.common.domain.PushMessage;
import com.excentria_it.wamya.common.domain.PushMessage.PushMessageBuilder;
import com.excentria_it.wamya.common.domain.PushTemplate;

public class PushMessageTestData {
	public static PushMessageBuilder proposalAcceptedPushMessageBuilder() {
		Map<String, String> params = new HashMap<>();
		PushTemplate.PROPOSAL_ACCEPTED.getTemplateParams().forEach(p -> {
			params.put(p, p + "_value");

		});
		return PushMessage.builder().to("default-device-registration-token").template(PushTemplate.PROPOSAL_ACCEPTED)
				.params(params).language(TestConstants.DEFAULT_TEMPLATE_LANGUAGE);
	}

	public static PushMessageBuilder proposalRejectedPushMessageBuilder() {
		Map<String, String> params = new HashMap<>();
		PushTemplate.PROPOSAL_REJECTED.getTemplateParams().forEach(p -> {
			params.put(p, p + "_value");

		});
		return PushMessage.builder().to("default-device-registration-token").template(PushTemplate.PROPOSAL_REJECTED)
				.params(params).language(TestConstants.DEFAULT_TEMPLATE_LANGUAGE);
	}
}
