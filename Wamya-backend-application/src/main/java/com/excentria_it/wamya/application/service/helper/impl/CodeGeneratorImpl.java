package com.excentria_it.wamya.application.service.helper.impl;

import java.util.UUID;

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

	@Override
	public String generateUUID() {
		return UUID.randomUUID().toString();
	}

}
