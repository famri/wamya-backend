package com.excentria_it.wamya.springcloud.gateway;

import java.util.Map;

import org.springframework.boot.actuate.health.CompositeReactiveHealthContributor;
import org.springframework.boot.actuate.health.ReactiveHealthContributor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
public class HealthCheckConfiguration {

	private WamyaBackendHealthIndicator wamyaBackendHealthIndicator;

	private AuthorizationServerHealthIndicator authorizationServerHealthIndicator;

	@Bean
	ReactiveHealthContributor healthcheckMicroservices() {

		return CompositeReactiveHealthContributor.fromMap(Map.of("wamyaBackendHealthIndicator",
				wamyaBackendHealthIndicator, "authorizationServerHealthIndicator", authorizationServerHealthIndicator));
	}

}