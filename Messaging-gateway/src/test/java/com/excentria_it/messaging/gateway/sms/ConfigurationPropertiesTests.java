package com.excentria_it.messaging.gateway.sms;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class ConfigurationPropertiesTests {
	private ConfigurationProperties configurationProperties = new ConfigurationProperties();

	@Test
	void testSMSGatewayProperties() {
		assertNotNull(configurationProperties.smsGatewayProperties());
	}
}
