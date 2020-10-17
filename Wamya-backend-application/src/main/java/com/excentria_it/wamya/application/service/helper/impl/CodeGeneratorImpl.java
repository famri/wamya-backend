package com.excentria_it.wamya.application.service.helper.impl;

import org.springframework.stereotype.Component;

import com.excentria_it.wamya.application.props.CodeGeneratorProperties;
import com.excentria_it.wamya.application.service.helper.CodeGenerator;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
public class CodeGeneratorImpl implements CodeGenerator {

	private CodeGeneratorProperties codeGeneratorProperties;

	@Override
	public String generateNumericCode() {
		return String.format("%0" + codeGeneratorProperties.getLength() + "d",
				(int) (Math.random() * Math.pow(10, codeGeneratorProperties.getLength())));
	}

}
