package com.excentria_it.wamya.application.utils;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class DocumentUrlResolverTests {

	@Test
	void testResolveUrl() {
		String documentUrl = DocumentUrlResolver.resolveUrl(1L,
				"2cf24dba5fb0a30e26e83b2ac5b9e29e1b161e5c1fa7425e73043362938b9824");
		assertEquals("/documents/1?hash=2cf24dba5fb0a30e26e83b2ac5b9e29e1b161e5c1fa7425e73043362938b9824", documentUrl);
	}
}
