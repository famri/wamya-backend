package com.excentria_it.wamya.springcloud.gateway;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebFluxSecurity
public class SecurityConfig {

	@Bean
	SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) throws Exception {
		http.csrf().disable().authorizeExchange()
				.pathMatchers("/actuator/**", "/eureka/**", "/wamya-backend/login/**", "/wamya-backend/accounts/**", "/oauth/token/**")
				.permitAll().anyExchange().authenticated().and().oauth2ResourceServer().jwt();
		return http.build();
	}

}