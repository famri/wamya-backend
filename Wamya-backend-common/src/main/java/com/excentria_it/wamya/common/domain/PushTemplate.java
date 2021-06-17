package com.excentria_it.wamya.common.domain;

import java.util.List;

public enum PushTemplate {
	
	PROPOSAL_REJECTED(List.of("departure_place_name", "arrival_place_name", "journey_request_datetime")),
	PROPOSAL_ACCEPTED(List.of("departure_place_name", "arrival_place_name", "journey_request_datetime"));

	private List<String> templateParams;

	private PushTemplate(List<String> templateParams) {
		this.templateParams = templateParams;
	}

	public List<String> getTemplateParams() {
		return templateParams;
	}

}
