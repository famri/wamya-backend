package com.excentria_it.wamya.common.impl;

import com.excentria_it.wamya.common.CodeGenerator;
import com.excentria_it.wamya.common.props.CodeGeneratorProperties;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class CodeGeneratorImpl implements CodeGenerator {

	private CodeGeneratorProperties codeGeneratorProperties;

	@Override
	public String generateNumericCode() {
		return String.format("%0" + codeGeneratorProperties.getLength() + "d",
				(int) (Math.random() * Math.pow(10, codeGeneratorProperties.getLength())));
	}

}
