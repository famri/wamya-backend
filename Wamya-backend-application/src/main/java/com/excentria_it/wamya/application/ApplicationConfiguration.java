package com.excentria_it.wamya.application;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;

import com.excentria_it.wamya.application.props.CodeGeneratorProperties;
import com.excentria_it.wamya.application.props.ServerUrlProperties;

@Configuration
@EnableConfigurationProperties(value = { CodeGeneratorProperties.class, ServerUrlProperties.class })
public class ApplicationConfiguration {

	private static final String DEFAULT_ENCODING_ALGORITHM = "bcrypt";

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
