package com.excentria_it.wamya.adapter.web;

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
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

	private static final String DEFAULT_ENCODING_ALGORITHM = "bcrypt";

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeRequests().antMatchers("/**/*").permitAll().antMatchers(HttpMethod.GET, "/login")
				.permitAll();

	}

	@Bean
	public PasswordEncoder delegatingPasswordEncoder() {
		
		Map<String, PasswordEncoder> encoders = new HashMap<>();
		encoders.put(DEFAULT_ENCODING_ALGORITHM, new BCryptPasswordEncoder());
		encoders.put("noop", NoOpPasswordEncoder.getInstance());
		encoders.put("pbkdf2", new Pbkdf2PasswordEncoder());
		encoders.put("scrypt", new SCryptPasswordEncoder());
		encoders.put("sha256", new StandardPasswordEncoder());

		return new DelegatingPasswordEncoder(DEFAULT_ENCODING_ALGORITHM, encoders);

	}
}
