package com.excentria_it.wamya.common.impl;

import com.excentria_it.wamya.common.CodeGenerator;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
public class CodeGeneratorImpl implements CodeGenerator {

	@Getter
	@Setter
	private int codeLength;

	@Override
	public String generateNumericCode() {
		return String.format("%0" + codeLength + "d", (int) (Math.random() * Math.pow(10, codeLength)));
	}

}
