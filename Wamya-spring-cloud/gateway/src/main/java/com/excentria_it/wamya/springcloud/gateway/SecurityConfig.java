package com.excentria_it.wamya.springcloud.gateway;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.server.resource.web.server.ServerBearerTokenAuthenticationConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;

@EnableWebFluxSecurity
public class SecurityConfig {

	@Bean
	SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) throws Exception {
		http.csrf().disable().authorizeExchange()
				.pathMatchers("/actuator/**", "/eureka/**", "/wamya-backend/login/**", "/wamya-backend/accounts/**",
						"/oauth/**", "/wamya-backend/countries/**", "/wamya-backend/locales/**", "/wamya-backend/genders/**",
						"/wamya-backend/content/**")
				.permitAll().anyExchange().authenticated().and().oauth2ResourceServer().bearerTokenConverter(authenticationConverter()).jwt();
		
		return http.build();
	}

	//this converter allows using access_token as get request parameter in websocket endpoint to authenticate websocket client
	@Bean
	ServerAuthenticationConverter authenticationConverter() {
		ServerBearerTokenAuthenticationConverter authenticationConverter = new ServerBearerTokenAuthenticationConverter();
		authenticationConverter.setAllowUriQueryParameter(true);
		return authenticationConverter;
	}

}