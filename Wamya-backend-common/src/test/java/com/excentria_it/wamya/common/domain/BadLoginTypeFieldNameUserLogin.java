package com.excentria_it.wamya.common.domain;

import com.excentria_it.wamya.common.LoginType;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BadLoginTypeFieldNameUserLogin {
	private LoginType badLoginTypeFieldName;
	private String loginValue;

}