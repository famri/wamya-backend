package com.excentria_it.wamya.adapter.b2b.rest.props;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class B2bRestConfigurationProperties {
	@Bean
	public AuthServerProperties authServerProperties() {
		return new AuthServerProperties();
	}
}
