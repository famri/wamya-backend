package com.excentria_it.wamya.springcloud.authorisationserver;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;

@Configuration
@EnableWebSecurity
class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Bean
	public PasswordEncoder passwordEncoder() {

		String idForEncode = "bcrypt";
		Map<String, PasswordEncoder> encoders = new HashMap<>();
		encoders.put(idForEncode, new BCryptPasswordEncoder());
		encoders.put("noop", NoOpPasswordEncoder.getInstance());
		encoders.put("pbkdf2", new Pbkdf2PasswordEncoder());
		encoders.put("scrypt", new SCryptPasswordEncoder());

		return new DelegatingPasswordEncoder(idForEncode, encoders);

	}

	@Bean
	BearerTokenResolver bearerTokenResolver() {
		DefaultBearerTokenResolver bearerTokenResolver = new DefaultBearerTokenResolver();
		bearerTokenResolver.setAllowUriQueryParameter(true);
		return bearerTokenResolver;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// @formatter:off
		http.authorizeRequests(authz -> authz

				.antMatchers("/actuator/**").permitAll().antMatchers("/h2-console/**").permitAll()
				.antMatchers(HttpMethod.POST, "/oauth/users/**").permitAll()
				.antMatchers(HttpMethod.POST, "/oauth/users/{\\d+}/do-reset-password")
				.hasAuthority("SCOPE_password:write").mvcMatchers("/.well-known/jwks.json").permitAll()
				.mvcMatchers("/favicon.ico").permitAll()).authorizeRequests().anyRequest().authenticated().and()
				.headers().frameOptions().disable().and().csrf().disable().cors().disable().oauth2ResourceServer()
				.jwt().jwkSetUri("http://localhost:9999/.well-known/jwks.json");
		/*
		 * .csrf(csrf -> csrf.ignoringRequestMatchers(
		 * 
		 * request -> "/introspect".equals(request.getRequestURI()), request ->
		 * "/oauth/users".equals(request.getRequestURI()))
		 * 
		 * .ignoringAntMatchers("/h2-console/**"));
		 */

		// @formatter:on
	}

}
