package com.excentria_it.wamya.application.service.helper.impl;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.excentria_it.wamya.application.props.CodeGeneratorProperties;

@ExtendWith(MockitoExtension.class)
public class CodeGeneratorImplTest {
	@Mock
	private CodeGeneratorProperties codeGeneratorProperties;
	@InjectMocks
	private CodeGeneratorImpl codeGenerator;

	@Test
	void generateCodeWithCorrectLength() {
		given(codeGeneratorProperties.getLength()).willReturn(4);
		for (int i = 0; i < 100; i++) {
			String code = codeGenerator.generateNumericCode();
			assertThat(code).hasSize(4);

		}

	}

	@Test
	void generateUUIDWithCorrectLength() {
		String uuid = codeGenerator.generateUUID();
		String[] tokens = uuid.split("-");
		assertThat(codeGenerator.generateUUID()).hasSize("cbbb422d-4b41-4697-aa16-5fba0c8a665c".length());
		assertThat(tokens).hasSize(5);

	}
}
