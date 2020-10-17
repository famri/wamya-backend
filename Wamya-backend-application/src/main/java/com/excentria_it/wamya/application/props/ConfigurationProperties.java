package com.excentria_it.wamya.application.props;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigurationProperties {
	@Bean
	public CodeGeneratorProperties codeGeneratorProperties() {
		return new CodeGeneratorProperties();
	}

	@Bean
	public ServerUrlProperties serverUrlProperties() {
		return new ServerUrlProperties();
	}
}
