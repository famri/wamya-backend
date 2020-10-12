package com.excentria_it.wamya.common.domain;

import java.util.Locale;
import java.util.Map;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SMSMessage {

	private String to;
	private SMSTemplate template;
	private Map<String, String> params;
	private Locale locale;
}
