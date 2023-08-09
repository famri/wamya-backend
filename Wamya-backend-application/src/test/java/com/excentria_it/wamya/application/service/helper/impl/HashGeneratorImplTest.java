package com.excentria_it.wamya.application.service.helper.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.excentria_it.wamya.common.HashAlgorithm;

@ExtendWith(MockitoExtension.class)
public class HashGeneratorImplTest {

	private HashGeneratorImpl hashGenerator = new HashGeneratorImpl();

	@Test
	void testGenerateHashes_SHA_256() {
		Set<Object> objects = Set.of("O1", "O2", "O3");
		List<String> hashes = hashGenerator.generateHashes(objects, HashAlgorithm.SHA_256);
		
		assertEquals(objects.size(), hashes.size());
		
		for (int i = 0; i < objects.size(); i++) {
			assertEquals(64, hashes.get(i).length());
			System.out.println(hashes.get(i));
		}
	}

	@Test
	void testGenerateHashes_SHA3_256() {
		Set<Object> objects = Set.of("O1", "O2", "O3");
		List<String> hashes = hashGenerator.generateHashes(objects, HashAlgorithm.SHA3_256);
		
		assertEquals(objects.size(), hashes.size());
		
		for (int i = 0; i < objects.size(); i++) {
			assertEquals(64, hashes.get(i).length());
			System.out.println(hashes.get(i));
		}
	}
	
	
	
}
