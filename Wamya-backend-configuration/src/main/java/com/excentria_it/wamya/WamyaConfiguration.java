package com.excentria_it.wamya;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;

import com.excentria_it.wamya.adapter.b2b.rest.B2bRestConfiguration;
import com.excentria_it.wamya.adapter.file.storage.FileStorageConfiguration;
import com.excentria_it.wamya.adapter.messaging.MessagingConfiguration;
import com.excentria_it.wamya.adapter.messaging.WebSocketConfiguration;
import com.excentria_it.wamya.adapter.messaging.WebSocketSecurityConfiguration;
import com.excentria_it.wamya.adapter.persistence.PersistenceConfiguration;
import com.excentria_it.wamya.adapter.web.WebConfiguration;
import com.excentria_it.wamya.adapter.web.WebSecurityConfiguration;
import com.excentria_it.wamya.application.props.CodeGeneratorProperties;
import com.excentria_it.wamya.application.props.PasswordResetProperties;
import com.excentria_it.wamya.application.props.ServerUrlProperties;

@Configuration
@Import(value = { B2bRestConfiguration.class, MessagingConfiguration.class, WebSocketConfiguration.class,
		FileStorageConfiguration.class, PersistenceConfiguration.class, WebConfiguration.class,
		WebSecurityConfiguration.class, WebSocketSecurityConfiguration.class })
@EnableConfigurationProperties(value = { CodeGeneratorProperties.class, ServerUrlProperties.class,
		PasswordResetProperties.class })
public class WamyaConfiguration {

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
