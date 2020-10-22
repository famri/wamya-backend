package com.excentria_it.wamya.common.domain;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SMSMessage {

	private String to;
	private SMSTemplate template;
	private Map<String, String> params;
	private String language;
}
