package com.excentria_it.wamya.adapter.b2b.rest.props;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class ConfigurationPropertiesTests {
	private B2bRestConfigurationProperties configurationProperties = new B2bRestConfigurationProperties();

	@Test
	void testAuthServerProperties() {
		assertNotNull(configurationProperties.authServerProperties());
	}
}