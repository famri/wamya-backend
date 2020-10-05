package com.excentria_it.wamya.adapter.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserAccountDto {
	private String icc;
	private String mobilePhoneNumber;
	private String password;
	private String passwordConfirmation;
}
