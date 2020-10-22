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
public class EmailMessage {

	private String from;
	private String to;
	private String subject;
	private EmailTemplate template;
	private Map<String, String> params;
	private String language;
	private Map<String, String> attachements;

}
