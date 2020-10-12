package com.excentria_it.wamya.common.domain;

import java.util.Locale;
import java.util.Map;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class EmailMessage {

	private String from;
	private String to;
	private String subject;
	private EmailTemplate template;
	private Map<String, String> params;
	private Locale locale;
	private Map<String, String> attachements;

}
