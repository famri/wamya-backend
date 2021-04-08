package com.excentria_it.wamya.adapter.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;

import com.excentria_it.wamya.common.annotation.Generated;

@Import(JwtConfiguration.class)
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Generated
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

	// @Autowired
	// Converter<Jwt, JwtAuthenticationToken> authenticationConverter;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//TODO for production enable csrf
		// @formatter:off
		http.csrf().disable().cors()
		.disable().httpBasic().disable().formLogin().disable().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
				.authorizeRequests(authz -> authz.antMatchers("/actuator/**", "/content/**").permitAll()
						.antMatchers(HttpMethod.POST, "/login/**", "/accounts/**").permitAll()
						.antMatchers(HttpMethod.GET, "/countries/**", "/locales/**").permitAll()
						.antMatchers(HttpMethod.GET,"/wamya-ws/**").hasAnyAuthority("SCOPE_journey:write", "SCOPE_offer:write")
						.antMatchers("/user-preferences").hasAnyAuthority("SCOPE_journey:write", "SCOPE_offer:write")
						.antMatchers("/users/me/discussions/**")
						.hasAnyAuthority("SCOPE_journey:write", "SCOPE_offer:write")
						.antMatchers(HttpMethod.GET, "/localities/**").hasAuthority("SCOPE_journey:write")
						.antMatchers(HttpMethod.GET, "/departments/**").hasAuthority("SCOPE_offer:write")
						.antMatchers(HttpMethod.GET, "/profiles/**").hasAuthority("SCOPE_profile:read")
						.antMatchers(HttpMethod.POST, "/profiles/**").hasAuthority("SCOPE_profile:write")
						.antMatchers(HttpMethod.PATCH, "/profiles/**").hasAuthority("SCOPE_profile:write")
						.antMatchers(HttpMethod.POST, "/geo-places/**").hasAnyAuthority("SCOPE_journey:write")
						.antMatchers(HttpMethod.GET, "/geo-places/**").hasAnyAuthority("SCOPE_journey:write")
						.antMatchers(HttpMethod.PATCH, "/journey-requests/{\\d+}/proposals/{\\d+}/**")
						.hasAuthority("SCOPE_offer:read")
						.antMatchers(HttpMethod.POST, "/journey-requests/{\\d+}/proposals/**")
						.hasAuthority("SCOPE_offer:write")
						.antMatchers(HttpMethod.GET, "/journey-requests/{\\d+}/proposals/**")
						.hasAuthority("SCOPE_offer:read").antMatchers(HttpMethod.GET, "/journey-requests/**")
						.hasAuthority("SCOPE_journey:read").antMatchers(HttpMethod.POST, "/journey-requests/**")
						.hasAuthority("SCOPE_journey:write").antMatchers(HttpMethod.GET, "/travel-info/**")
						.hasAuthority("SCOPE_journey:write")
						.antMatchers(HttpMethod.GET, "/constructors/{\\d+}/models/**", "/constructors**")
						.hasAuthority("SCOPE_vehicule:write").antMatchers(HttpMethod.GET, "/engine-types/**")
						.hasAnyAuthority("SCOPE_vehicule:write", "SCOPE_journey:write")
						.antMatchers(HttpMethod.POST, "/users/me/vehicules/**").hasAuthority("SCOPE_vehicule:write")
						.antMatchers(HttpMethod.GET, "/users/me/vehicules/**").hasAuthority("SCOPE_vehicule:read")
						.antMatchers(HttpMethod.GET, "/users/me/journey-requests/**")
						.hasAuthority("SCOPE_journey:write")
						.antMatchers(HttpMethod.POST, "/validation-codes/sms/send/**")
						.hasAuthority("SCOPE_profile:write")
						.antMatchers(HttpMethod.POST, "/validation-codes/email/send/**")
						.hasAuthority("SCOPE_profile:write").anyRequest().authenticated())
				.oauth2ResourceServer()
				.bearerTokenResolver(bearerTokenResolver())
				.jwt()
		// .jwtAuthenticationConverter(authenticationConverter)
		;

		// @formatter:on

	}
	//this is to allow sending access_token as a URI request parameter for webSocket client authentication
	@Bean
	BearerTokenResolver bearerTokenResolver() {
		DefaultBearerTokenResolver bearerTokenResolver = new DefaultBearerTokenResolver();
		bearerTokenResolver.setAllowUriQueryParameter(true);
		return bearerTokenResolver;
	}

}
