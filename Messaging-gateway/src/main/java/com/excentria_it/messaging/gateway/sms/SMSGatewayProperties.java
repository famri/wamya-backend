package com.excentria_it.messaging.gateway.sms;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "sms.gateway")
@Data
public class SMSGatewayProperties {
	private String host;
	private String port;
	private String username;
	private String password;
}
