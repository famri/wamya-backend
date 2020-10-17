package com.excentria_it.wamya.common.domain;

import com.excentria_it.wamya.common.LoginType;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserLogin {
	private LoginType loginType;
	private String loginValue;
}
