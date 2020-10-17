package com.excentria_it.messaging.gateway.sms;

import javax.validation.constraints.NotEmpty;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import lombok.Data;

@ConfigurationProperties(prefix = "sms.gateway")
@Validated
@Data
public class SMSGatewayProperties {
	@NotEmpty
	private String host;
	@NotEmpty
	private String port;
	@NotEmpty
	private String username;
	@NotEmpty
	private String password;
}
