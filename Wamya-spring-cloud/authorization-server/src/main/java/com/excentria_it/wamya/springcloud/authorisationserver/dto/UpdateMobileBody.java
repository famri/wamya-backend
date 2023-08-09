package com.excentria_it.wamya.springcloud.authorisationserver.dto;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMobileBody {
	@NotEmpty
	private String icc;
	@NotEmpty
	private String mobileNumber;
	
}