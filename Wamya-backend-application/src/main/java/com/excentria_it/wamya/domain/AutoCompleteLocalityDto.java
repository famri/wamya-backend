package com.excentria_it.wamya.domain;

import lombok.Value;

@Value
public class AutoCompleteLocalityDto {
	private Long id;

	private String name;

	private String delegation;

	private String department;

	private String country;

}
