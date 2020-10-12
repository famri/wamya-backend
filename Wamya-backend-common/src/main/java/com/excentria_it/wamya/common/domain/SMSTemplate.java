package com.excentria_it.wamya.common.domain;

import java.util.List;

public enum SMSTemplate {
	PHONE_VALIDATION(List.of("validation_code"));

	private List<String> templateParams;

	private SMSTemplate(List<String> templateParams) {
		this.templateParams = templateParams;
	}

	public List<String> getTemplateParams() {
		return templateParams;
	}

}
