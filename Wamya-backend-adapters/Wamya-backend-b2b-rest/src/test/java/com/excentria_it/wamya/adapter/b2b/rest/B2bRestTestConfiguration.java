package com.excentria_it.wamya.adapter.b2b.rest;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@SpringBootConfiguration
@EnableAutoConfiguration
@Import(value = { B2bRestConfiguration.class })
public class B2bRestTestConfiguration {
	@Bean
	public PropertySourcesPlaceholderConfigurer propertiesResolver() {
		return new PropertySourcesPlaceholderConfigurer();
	}
}
