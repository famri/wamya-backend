package com.excentria_it.messaging.gateway.sms;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigurationProperties {
	@Bean
	public SMSGatewayProperties smsGatewayProperties() {
		return new SMSGatewayProperties();
	}
}
