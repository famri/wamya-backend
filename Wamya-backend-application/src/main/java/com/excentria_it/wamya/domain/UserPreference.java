package com.excentria_it.wamya.domain;

import lombok.Value;

@Value
public class UserPreference {
	private Long id;

	private String key;

	protected String value;
}
