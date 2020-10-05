package com.excentria_it.wamya.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class ValidationCodeRequest {

	private long id;
	private ValidationCodeRequestStatus status;

}
