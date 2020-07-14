package com.codisiac.wamya.common.impl;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.codisiac.wamya.common.CodeGenerator;

public class CodeGeneratorImplTest {

	private static final int CODE_LENGTH = 4;

	@Test
	void generateCodeWithCorrectLength() {

		CodeGenerator codeGenerator = new CodeGeneratorImpl(CODE_LENGTH);

		for (int i = 0; i < 100; i++) {
			String code = codeGenerator.generateNumericCode();
			assertThat(code).hasSize(CODE_LENGTH);
			System.out.println(String.format("ITERATION %d ==> CODE = %s", i, code));
		}

	}
}
