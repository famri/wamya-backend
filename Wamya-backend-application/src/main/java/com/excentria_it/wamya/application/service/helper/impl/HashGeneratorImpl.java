package com.excentria_it.wamya.application.service.helper.impl;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.springframework.security.crypto.codec.Hex;
import org.springframework.stereotype.Component;

import com.excentria_it.wamya.application.service.helper.HashGenerator;
import com.excentria_it.wamya.common.HashAlgorithm;

@Component
public class HashGeneratorImpl implements HashGenerator {

	private static final String SALT = "HASH-GENERATOR";

	@Override
	public List<String> generateHashes(Set<?> objects, HashAlgorithm algorithm) {

		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance(algorithm.getAlgorithmName());
			List<String> hashes = new ArrayList<>(objects.size());

			objects.stream().forEach(o -> {

				String originalString = SALT + o.toString();
				final byte[] hashbytes = digest.digest(originalString.getBytes(StandardCharsets.UTF_8));
				String sha3Hex = new String(Hex.encode(hashbytes));
				hashes.add(sha3Hex);
			});

			return hashes;
		} catch (NoSuchAlgorithmException e) {
			return Collections.emptyList();
		}

	}

}
