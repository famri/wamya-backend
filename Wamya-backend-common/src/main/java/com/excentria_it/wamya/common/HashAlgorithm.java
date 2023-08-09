package com.excentria_it.wamya.common;

import com.excentria_it.wamya.common.annotation.Generated;

@Generated
public enum HashAlgorithm {
	SHA_256("SHA-256"), SHA3_256("SHA3-256");

	private String algorithmName;

	HashAlgorithm(String algorithmName) {
		this.algorithmName = algorithmName;
	}

	public String getAlgorithmName() {
		return algorithmName;
	}

}