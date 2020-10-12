package com.excentria_it.wamya.common.impl;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.excentria_it.wamya.common.CodeGenerator;
import com.excentria_it.wamya.common.impl.CodeGeneratorImpl;

public class CodeGeneratorImplTest {



	@Test
	void generateCodeWithCorrectLength() {

		CodeGenerator codeGenerator = new CodeGeneratorImpl();

		for (int i = 0; i < 100; i++) {
			String code = codeGenerator.generateNumericCode();
			assertThat(code).hasSize(4);
			System.out.println(String.format("ITERATION %d ==> CODE = %s", i, code));
		}

	}
}
