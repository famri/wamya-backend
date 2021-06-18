package com.excentria_it.messaging.gateway.push;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class PushTestConfiguration {
	@Bean
	@Profile("localtest")
	public PushRequestReceiver getPushRequestReceiver() {
		return new PushRequestDummyReceiverImpl();
	}
}
