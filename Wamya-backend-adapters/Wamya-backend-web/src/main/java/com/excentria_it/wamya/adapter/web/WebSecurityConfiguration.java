package com.excentria_it.wamya.adapter.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import com.excentria_it.wamya.common.annotation.Generated;

@Configuration
@EnableWebSecurity
@Generated
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// http.csrf().disable().authorizeRequests().antMatchers("/**/*").permitAll();

		http.authorizeRequests(authz -> authz.antMatchers("/actuator/**").permitAll()
				.antMatchers(HttpMethod.POST, "/login/**", "/accounts/**").permitAll()
				.antMatchers(HttpMethod.GET, "/journey-requests/**").hasAuthority("SCOPE_journery:read")
				.antMatchers(HttpMethod.POST, "/journey-requests/**").hasAuthority("SCOPE_journery:write")
				.antMatchers(HttpMethod.GET, "/user-profiles/**").hasAuthority("SCOPE_profile:read")
				.antMatchers(HttpMethod.GET, "/journey-proposal/**").hasAuthority("SCOPE_offer:read")
				.antMatchers(HttpMethod.POST, "/validation-codes/sms/send/**").hasAuthority("SCOPE_profile:write")
				.antMatchers(HttpMethod.POST, "/validation-codes/email/send/**").hasAuthority("SCOPE_profile:write")
				.anyRequest().authenticated()).oauth2ResourceServer(oauth2 -> oauth2.jwt());
	}

}
