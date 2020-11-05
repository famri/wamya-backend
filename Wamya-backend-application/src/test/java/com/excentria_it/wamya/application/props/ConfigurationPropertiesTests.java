package com.excentria_it.wamya.application.props;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class ConfigurationPropertiesTests {

	private ApplicationConfigurationProperties configurationProperties = new ApplicationConfigurationProperties();

	@Test
	void testCodeGeneratorProperties() {
		assertNotNull(configurationProperties.codeGeneratorProperties());
	}

	@Test
	void testServerUrlProperties() {
		assertNotNull(configurationProperties.serverUrlProperties());
	}
}
