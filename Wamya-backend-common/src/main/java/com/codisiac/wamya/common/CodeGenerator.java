package com.codisiac.wamya.common;

public interface CodeGenerator {

	String generateNumericCode();

	void setCodeLength(int codeLength);

	int getCodeLength();
}
