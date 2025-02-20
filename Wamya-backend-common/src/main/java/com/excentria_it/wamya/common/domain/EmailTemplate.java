package com.excentria_it.wamya.common.domain;

import java.util.List;
import java.util.Map;

public enum EmailTemplate {

	EMAIL_VALIDATION(List.of("validation_url"), Map.of("logo_wamya", "static/logo_wamya.png")),
	PASSWORD_RESET(List.of("passwordResetUrl"), Map.of("logo_wamya", "static/logo_wamya.png")),
	RECEIVED_MESSAGE(List.of("sender_name", "discussions_url"), Map.of("logo_wamya", "static/logo_wamya.png")),
	RATING_REQUEST(List.of("user_name", "rating_url", "transporter_name", "transporter_photo_url", "departure_place", "arrival_place", "journey_date"), Map.of("logo_wamya", "static/logo_wamya.png"));
	
	private List<String> templateParams;

	private Map<String, String> templateResources;

	private EmailTemplate(List<String> templateParams, Map<String, String> templateResources) {
		this.templateParams = templateParams;
		this.templateResources = templateResources;
	}

	public List<String> getTemplateParams() {
		return templateParams;
	}

	public Map<String, String> getTemplateResources() {
		return templateResources;
	}

}
