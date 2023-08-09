package com.excentria_it.wamya.application.service.helper.impl;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.excentria_it.wamya.common.HashAlgorithm;

@RunWith(PowerMockRunner.class)
public class HashGeneratorImplJUnit4Test {

	private HashGeneratorImpl hashGenerator = new HashGeneratorImpl();

	@Test
	@PrepareForTest(HashAlgorithm.class)
	public void testGenerateHashes() throws Exception {

		HashAlgorithm unknownHashAlgorithm = PowerMockito.mock(HashAlgorithm.class);
		PowerMockito.when(unknownHashAlgorithm.ordinal()).thenReturn(3);
		PowerMockito.when(unknownHashAlgorithm.getAlgorithmName()).thenReturn("MY_ALGORITHM");
		PowerMockito.mockStatic(HashAlgorithm.class);

		HashAlgorithm[] hashAlgorithms = new HashAlgorithm[] { HashAlgorithm.SHA_256, HashAlgorithm.SHA3_256,
				unknownHashAlgorithm };
		PowerMockito.when(HashAlgorithm.values()).thenAnswer((Answer<HashAlgorithm[]>) invocation -> hashAlgorithms);
		// when

		List<String> hashes = hashGenerator.generateHashes(Set.of(1L, 2L, 3L), unknownHashAlgorithm);
		// then
		assertThat(hashes).isEmpty();
	}
}
