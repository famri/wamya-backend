package com.excentria_it.wamya.application.service.helper;

import java.util.List;
import java.util.Set;

import com.excentria_it.wamya.common.HashAlgorithm;

public interface HashGenerator {
	List<String> generateHashes(Set<? extends Object> objects, HashAlgorithm algorithm);
}
